package me.goldhardt.destinator.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.goldhardt.destinator.core.database.dao.DestinationDao
import me.goldhardt.destinator.core.database.dao.ItineraryDao
import me.goldhardt.destinator.core.database.model.DestinationEntity
import me.goldhardt.destinator.core.database.model.ItineraryItemEntity
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
    private val generateItineraryRepository: GenerateItineraryRepository
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
            val destinationId =
                destinationsDao.insertDestination(
                    DestinationEntity(
                        city = destinationItinerary.city,
                        country = destinationItinerary.country,
                        from = from,
                        to = to,
                        longitude = destinationItinerary.longitude,
                        latitude = destinationItinerary.latitude,
                        thumbnail = ""
                    )
                )
            itineraryDao.insertItinerary(
                destinationItinerary.itinerary.mapIndexed { index, item ->
                    ItineraryItemEntity(
                        destinationId = destinationId,
                        order = index,
                        date = item.date,
                        name = item.name,
                        description = item.description,
                        latitude = item.latitude,
                        longitude = item.longitude,
                        visitTimeMin = item.visitTimeMin,
                        tripDay = item.tripDay,
                        thumbnail = ""
                    )
                }
            )
            return Result.success(destinationId)
        } ?: return Result.failure(result.exceptionOrNull() ?: IOException("Failed to generate itinerary"))
    }

    override fun getDestination(destinationId: Long): Flow<Destination?> =
        destinationsDao.getDestination(destinationId)
            .map { it?.toDestination() }

    override fun getDestinations(): Flow<List<Destination>> =
        destinationsDao.getDestinationsWithItinerary()
            .map { list ->
                list.map { it.toDestination() }
            }
}