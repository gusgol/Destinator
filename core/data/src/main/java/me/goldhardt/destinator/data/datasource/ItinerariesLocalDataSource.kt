package me.goldhardt.destinator.data.datasource

import me.goldhardt.destinator.core.database.dao.DestinationDao
import me.goldhardt.destinator.core.database.dao.ItineraryDao
import me.goldhardt.destinator.core.database.dao.PhotoDao
import me.goldhardt.destinator.core.database.model.DestinationEntity
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
        insertItineraryItems(destinationId, destinationItinerary.itinerary)
        return destinationId
    }

    override suspend fun addPlaceToItinerary(
        destinationId: Long,
        placeName: String,
        placeDescription: String,
        latitude: Double,
        longitude: Double,
        visitTimeMin: Int,
        tripDay: Int
    ): Long {
        val place = placesDataSource.getPlace(placeName, latitude, longitude)
        val itineraryItem = ItineraryItemEntity(
            destinationId = destinationId,
            order = 0, // You may want to set the correct order
            date = "", // You may want to set the correct date
            name = placeName,
            description = placeDescription,
            latitude = place?.latitude ?: latitude,
            longitude = place?.longitude ?: longitude,
            visitTimeMin = visitTimeMin,
            tripDay = tripDay,
            iconUrl = place?.iconUrl,
            metadataSourceId = place?.sourceId
        )
        val itineraryItemId = itineraryDao.insertItinerary(itineraryItem)
        insertPhoto(itineraryItemId, place?.photosUrls.orEmpty())
        return itineraryItemId
    }

    override suspend fun deletePlaceFromItinerary(placeId: Long) {
        itineraryDao.deleteItineraryItem(placeId)
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
                thumbnail = destinationPlace?.photosUrls?.random().orEmpty()
            )
        )
    }

    private suspend fun insertItineraryItems(
        destinationId: Long,
        itineraryItems: List<AICreatedItineraryItem>
    ) {
        itineraryItems.mapIndexed { index, item ->
            val place = placesDataSource.getPlace(item.name, item.latitude, item.longitude)
            ItineraryItemEntity(
                destinationId = destinationId,
                order = index,
                date = item.date,
                name = item.name,
                description = item.description,
                latitude = place?.latitude ?: item.latitude,
                longitude = place?.longitude ?: item.longitude,
                visitTimeMin = item.visitTimeMin,
                tripDay = item.tripDay,
                iconUrl = place?.iconUrl,
                metadataSourceId = place?.sourceId
            ).also {
                val itineraryItemId = itineraryDao.insertItinerary(itineraryItem = it)
                insertPhoto(itineraryItemId, place?.photosUrls.orEmpty())
            }
        }
    }

    private suspend fun insertPhoto(
        itineraryItemId: Long,
        photosUrls: List<String>
    ) {
        photosUrls.map { url ->
            PhotoEntity(
                parentId = itineraryItemId,
                url = url
            )
        }.let { photos ->
            photoDao.insertPhotos(photos)
        }
    }
}
