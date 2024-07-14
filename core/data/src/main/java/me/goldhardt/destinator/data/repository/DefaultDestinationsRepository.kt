package me.goldhardt.destinator.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.goldhardt.destinator.core.database.dao.DestinationDao
import me.goldhardt.destinator.core.database.dao.ItineraryDao
import me.goldhardt.destinator.core.database.model.DestinationEntity
import me.goldhardt.destinator.core.database.model.ItineraryItemEntity
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
    ): Result<Unit> {
        val itineraryResult = generateItineraryRepository.generateItinerary(city, fromMs, toMs, tripStyleList)
        itineraryResult.getOrNull()?.let { itinerary ->
            val destinationId =
                destinationsDao.insertDestination(
                    DestinationEntity(
                        city = itinerary.city,
                        country = itinerary.country,
                        from = fromMs,
                        to = toMs,
                        longitude = itinerary.longitude,
                        latitude = itinerary.latitude,
                        thumbnail = ""
                    )
                )
            itineraryDao.insertItinerary(
                itinerary.itinerary.mapIndexed { index, item ->
                    ItineraryItemEntity(
                        destinationId = destinationId,
                        order = index,
                        date = item.date,
                        name = item.name,
                        description = item.description,
                        latitude = item.latitude,
                        longitude = item.longitude,
                        visitTimeMin = item.visitTimeMin,
                        thumbnail = ""
                    )
                }
            )
            return Result.success(Unit)
        } ?: return Result.failure(itineraryResult.exceptionOrNull() ?: IOException("Failed to generate itinerary"))
    }
}