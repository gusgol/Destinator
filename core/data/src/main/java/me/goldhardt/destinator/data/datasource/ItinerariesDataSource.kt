package me.goldhardt.destinator.data.datasource

import me.goldhardt.destinator.data.model.itinerary.AIGenerateItineraryResponse

interface ItinerariesDataSource {
    suspend fun createItinerary(
        destinationItinerary: AIGenerateItineraryResponse
    ): Long

    suspend fun updateItineraryItemOrder(
        itineraryDayId: Long,
        itineraryItemId: Long,
        offset: Int
    )
}