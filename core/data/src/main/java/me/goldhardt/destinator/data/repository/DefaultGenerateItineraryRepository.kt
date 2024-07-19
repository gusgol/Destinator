package me.goldhardt.destinator.data.repository

import android.util.Log
import me.goldhardt.destinator.data.datasource.GenerateItineraryDataSource
import me.goldhardt.destinator.data.extensions.toISO8601
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
        Log.i(
            "DefaultGenerateItineraryRepository",
            "Generating itinerary for: $city $fromMs (${fromMs.toISO8601()}) $toMs (${toMs.toISO8601()}) $tripStyleList"
        )
        return generateItineraryDataSource.generateItinerary(
            city = city,
            from = fromMs.toISO8601(),
            to = toMs.toISO8601(),
            tripStyle = tripStyleList.joinToString()
        )
    }
}