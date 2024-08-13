package me.goldhardt.destinator.data.datasource

import me.goldhardt.destinator.data.model.itinerary.AIGenerateItineraryResponse

interface ItinerariesDataSource {
    suspend fun createItinerary(
        destinationItinerary: AIGenerateItineraryResponse
    ): Long
}