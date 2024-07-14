package me.goldhardt.destinator.data.repository

import me.goldhardt.destinator.data.datasource.GenerateItineraryDataSource
import me.goldhardt.destinator.data.extensions.toUTC
import me.goldhardt.destinator.data.model.itinerary.AICreatedItineraryItem
import me.goldhardt.destinator.data.model.itinerary.AIGenerateItineraryResponse
import javax.inject.Inject

class DefaultGenerateItineraryRepository @Inject constructor(
    private val generateItineraryDataSource: GenerateItineraryDataSource
) : GenerateItineraryRepository {

    override suspend fun generateItinerary(
        city: String,
        fromMs: Long,
        toMs: Long,
        tripStyleList: List<String>
    ): Result<AIGenerateItineraryResponse> {
        return generateItineraryDataSource.generateItinerary(
            city = city,
            from = fromMs.toUTC(),
            to = toMs.toUTC(),
            tripStyle = tripStyleList.joinToString()
        )
    }
}