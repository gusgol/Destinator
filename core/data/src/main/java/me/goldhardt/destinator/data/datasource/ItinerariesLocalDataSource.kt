package me.goldhardt.destinator.data.datasource

import me.goldhardt.destinator.core.common.PhotoSource
import me.goldhardt.destinator.core.database.dao.DestinationDao
import me.goldhardt.destinator.core.database.dao.ItineraryDao
import me.goldhardt.destinator.core.database.dao.PhotoDao
import me.goldhardt.destinator.core.database.model.DestinationEntity
import me.goldhardt.destinator.core.database.model.ItineraryDayEntity
import me.goldhardt.destinator.core.database.model.ItineraryDayWithItems
import me.goldhardt.destinator.core.database.model.ItineraryItemEntity
import me.goldhardt.destinator.core.database.model.PhotoEntity
import me.goldhardt.destinator.core.places.PlacesDataSource
import me.goldhardt.destinator.data.model.itinerary.AICreatedItineraryItem
import me.goldhardt.destinator.data.model.itinerary.AIGenerateItineraryResponse
import javax.inject.Inject

class ItinerariesLocalDataSource @Inject constructor(
    private val destinationsDao: DestinationDao,
    private val itineraryDao: ItineraryDao,
    private val photoDao: PhotoDao,
    private val placesDataSource: PlacesDataSource,
) : ItinerariesDataSource {

    override suspend fun createItinerary(destinationItinerary: AIGenerateItineraryResponse): Long {
        val destinationId = insertDestination(destinationItinerary)
        insertItineraryDays(destinationId, destinationItinerary.itinerary)
        return destinationId
    }

    /**
     * Updates the order of an itinerary item.
     * - If the offset is 0, no action is taken.
     * - If the [ItineraryItemEntity] is at the first position within its day, the item is moved to the previous day.
     * - If the [ItineraryItemEntity] is at the last position within its day, the item is moved to the next day.
     *
     * @param itineraryDayId The ID of the itinerary day.
     * @param itineraryItemId The ID of the itinerary item.
     * @param offset The offset to move the item by.
     */
    override suspend fun updateItineraryItemOrder(
        itineraryDayId: Long,
        itineraryItemId: Long,
        offset: Int
    ) {
        if (offset == 0) return

        val itineraryDay = itineraryDao.getItineraryDayById(itineraryDayId) ?: return
        val itineraryItem =
            itineraryDay.itineraryItems.find { it.itineraryItem.id == itineraryItemId }?.itineraryItem
                ?: return

        val newOrder = itineraryItem.order + offset

        when {
            newOrder < 0 -> moveToPreviousDay(itineraryDay, itineraryItem)
            newOrder >= itineraryDay.itineraryItems.size -> moveToNextDay(
                itineraryDay,
                itineraryItem
            )

            else -> swapItems(itineraryDay, itineraryItem, newOrder)
        }
    }

    private suspend fun moveToPreviousDay(
        itineraryDay: ItineraryDayWithItems,
        itineraryItem: ItineraryItemEntity
    ) {
        if (itineraryDay.itineraryDay.day == 1) return

        val previousDay =
            itineraryDao.getItineraryByDay(itineraryDay.itineraryDay.day - 1) ?: return

        val updatedItineraryItem = itineraryItem.copy(
            itineraryDayId = previousDay.itineraryDay.id,
            order = previousDay.itineraryItems.size
        )

        itineraryDao.updateItinerary(updatedItineraryItem)
        updateItineraryOrders(itineraryDay, itineraryItem.id)
    }

    private suspend fun moveToNextDay(
        itineraryDay: ItineraryDayWithItems,
        itineraryItem: ItineraryItemEntity
    ) {
        val nextDay = itineraryDao.getItineraryByDay(itineraryDay.itineraryDay.day + 1) ?: return

        val updatedItineraryItem = itineraryItem.copy(
            itineraryDayId = nextDay.itineraryDay.id,
            order = 0
        )

        itineraryDao.updateItinerary(updatedItineraryItem)
        updateItineraryOrders(itineraryDay, itineraryItem.id)
        updateNextDayOrders(nextDay)
    }

    private suspend fun swapItems(
        itineraryDay: ItineraryDayWithItems,
        itineraryItem: ItineraryItemEntity,
        newOrder: Int
    ) {
        val swapItem =
            itineraryDay.itineraryItems.find { it.itineraryItem.order == newOrder }?.itineraryItem
                ?: return

        itineraryDao.swapItineraryItems(
            swapItem.copy(order = itineraryItem.order),
            itineraryItem.copy(order = newOrder)
        )
    }

    private suspend fun updateItineraryOrders(
        itineraryDay: ItineraryDayWithItems,
        itineraryItemId: Long
    ) {
        val updatedItineraries = itineraryDay.itineraryItems
            .filter { it.itineraryItem.id != itineraryItemId }
            .mapIndexed { index, itineraryItemWithPhotos ->
                itineraryItemWithPhotos.itineraryItem.copy(order = index)
            }

        itineraryDao.updateItineraries(updatedItineraries)
    }

    private suspend fun updateNextDayOrders(nextDay: ItineraryDayWithItems) {
        val nextDayUpdatedItineraries =
            nextDay.itineraryItems.mapIndexed { index, itineraryItemWithPhotos ->
                itineraryItemWithPhotos.itineraryItem.copy(order = index + 1)
            }

        itineraryDao.updateItineraries(nextDayUpdatedItineraries)
    }

    private suspend fun insertDestination(destinationItinerary: AIGenerateItineraryResponse): Long {
        val from = destinationItinerary.itinerary.minOf { it.date }
        val to = destinationItinerary.itinerary.maxOf { it.date }

        val destinationPlace = placesDataSource.getPlace(
            query = destinationItinerary.city,
            latitude = destinationItinerary.latitude,
            longitude = destinationItinerary.longitude
        )

        return destinationsDao.insertDestination(
            DestinationEntity(
                city = destinationItinerary.city,
                country = destinationItinerary.country,
                from = from,
                to = to,
                longitude = destinationItinerary.longitude,
                latitude = destinationItinerary.latitude,
                thumbnail = destinationPlace?.photosReferences?.random().orEmpty()
            )
        )
    }

    private suspend fun insertItineraryDays(
        destinationId: Long,
        itineraryItems: List<AICreatedItineraryItem>
    ) {
        itineraryItems.groupBy { it.tripDay }.map { (day, items) ->
            val itineraryDay = ItineraryDayEntity(
                destinationId = destinationId,
                date = items.first().date,
                day = day
            )
            val itineraryDayId = itineraryDao.insertItineraryDay(itineraryDay)
            insertItineraryItems(destinationId, itineraryDayId, items)
        }
    }

    private suspend fun insertItineraryItems(
        destinationId: Long,
        itineraryDayId: Long,
        itineraryItems: List<AICreatedItineraryItem>
    ) {
        itineraryItems.mapIndexed { index, item ->
            val place = placesDataSource.getPlace(item.name, item.latitude, item.longitude)
            ItineraryItemEntity(
                destinationId = destinationId,
                itineraryDayId = itineraryDayId,
                order = index,
                name = item.name,
                description = item.description,
                latitude = place?.latitude ?: item.latitude,
                longitude = place?.longitude ?: item.longitude,
                visitTimeMin = item.visitTimeMin,
                iconUrl = place?.iconUrl,
                metadataSourceId = place?.sourceId
            ).also {
                val itineraryItemId = itineraryDao.insertItinerary(itineraryItem = it)
                insertPhoto(itineraryItemId, place?.photosReferences.orEmpty())
            }
        }
    }

    private suspend fun insertPhoto(
        itineraryItemId: Long,
        references: List<String>
    ) {
        references.map { ref ->
            PhotoEntity(
                parentId = itineraryItemId,
                reference = ref,
                source = PhotoSource.GOOGLE_PLACES
            )
        }.let { photos ->
            photoDao.insertPhotos(photos)
        }
    }
}