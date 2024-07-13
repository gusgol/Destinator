package me.goldhardt.destinator.data.repository

import me.goldhardt.destinator.data.model.ItineraryItem

interface GenerateItineraryRepository {
    suspend fun generateItinerary(
        city: String,
        fromMs: Long,
        toMs: Long,
        tripStyleList: List<String>
    ): Result<List<ItineraryItem>>
}