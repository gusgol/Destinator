package me.goldhardt.destinator.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.goldhardt.destinator.core.database.dao.DestinationDao
import me.goldhardt.destinator.core.database.dao.ItineraryDao
import me.goldhardt.destinator.core.database.dao.PhotoDao
import me.goldhardt.destinator.core.database.model.DestinationEntity
import me.goldhardt.destinator.core.database.model.ItineraryItemEntity
import me.goldhardt.destinator.core.database.model.PhotoEntity
import me.goldhardt.destinator.core.places.PlacesDataSource
import me.goldhardt.destinator.data.model.destination.Destination
import me.goldhardt.destinator.data.model.destination.toDestination
import java.io.IOException
import javax.inject.Inject

/**
 * Default implementation of [DestinationsRepository]
 * TODO Move daos to their own class (or maybe keep them in the Dao itself)
 */
class DefaultDestinationsRepository @Inject constructor(
    private val destinationsDao: DestinationDao,
    private val itineraryDao: ItineraryDao,
    private val photoDao: PhotoDao,
    private val generateItineraryRepository: GenerateItineraryRepository,
    private val placesDataSource: PlacesDataSource,
) : DestinationsRepository {

    override suspend fun createDestination(
        city: String,
        fromMs: Long,
        toMs: Long,
        tripStyleList: List<String>
    ): Result<Long> {
        val result = generateItineraryRepository.generateItinerary(city, fromMs, toMs, tripStyleList)
        result.getOrNull()?.let { destinationItinerary ->
            val from = destinationItinerary.itinerary.minOf { it.date }
            val to = destinationItinerary.itinerary.maxOf { it.date }
            val destinationPlace = placesDataSource.getPlace(
                query = city,
                latitude = destinationItinerary.latitude,
                longitude = destinationItinerary.longitude
            )
            Log.e("DefaultDestinationsRepository", "createDestination: $destinationPlace")
            val destinationId =
                destinationsDao.insertDestination(
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

            destinationItinerary.itinerary.mapIndexed { index, item ->
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
                    place?.photosUrls?.map { url ->
                        PhotoEntity(
                            parentId = itineraryItemId,
                            url = url
                        )
                    }?.let { photos ->
                        photoDao.insertPhotos(photos)
                    }
                }
            }
            return Result.success(destinationId)
        } ?: return Result.failure(result.exceptionOrNull() ?: IOException("Failed to generate itinerary"))
    }

    override fun getDestination(destinationId: Long): Flow<Destination?> =
        destinationsDao.getDestination(destinationId)
            .map {
                it?.toDestination()
            }

    override fun getDestinations(): Flow<List<Destination>> =
        destinationsDao.getDestinationsWithItinerary()
            .map { list ->
                list.map { it.toDestination() }
            }
}