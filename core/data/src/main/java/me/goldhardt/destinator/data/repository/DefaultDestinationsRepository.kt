package me.goldhardt.destinator.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.goldhardt.destinator.core.database.dao.DestinationDao
import me.goldhardt.destinator.data.datasource.ItinerariesDataSource
import me.goldhardt.destinator.data.model.destination.Destination
import me.goldhardt.destinator.data.model.destination.toDestination
import me.goldhardt.destinator.data.model.itinerary.AIGenerateItineraryResponse
import java.io.IOException
import javax.inject.Inject

/**
 * Default implementation of [DestinationsRepository]
 */
class DefaultDestinationsRepository @Inject constructor(
    private val destinationsDao: DestinationDao,
    private val generateItineraryRepository: GenerateItineraryRepository,
    private val itinerariesDataSource: ItinerariesDataSource,
) : DestinationsRepository {

    override suspend fun createDestination(
        city: String,
        fromMs: Long,
        toMs: Long,
        tripStyleList: List<String>
    ): Result<Long> {
        val result = generateItineraryRepository.generateItinerary(city, fromMs, toMs, tripStyleList)
        result.getOrNull()?.let { destinationItinerary ->
            return runCatching {
                val destinationId = itinerariesDataSource.createItinerary(destinationItinerary)
                Result.success(destinationId)
            }.getOrElse {
                failure(result, "Failed to save itinerary")
            }
        } ?: return failure(result, "Failed to generate itinerary")
    }

    override fun getDestination(destinationId: Long): Flow<Destination?> =
        destinationsDao.getDestination(destinationId)
            .map {
                it?.toDestination()
            }

    override fun getDestinations(): Flow<List<Destination>> =
        destinationsDao.getDestinationsWithItinerary()
            .map { list ->
                list.map {
                    it.toDestination()
                }.sortedBy {  it.status.ordinal }
            }

    private fun failure(
        result: Result<AIGenerateItineraryResponse>,
        errorMessage: String
    ): Result<Long> =
        Result.failure(result.exceptionOrNull() ?: IOException(errorMessage))
}