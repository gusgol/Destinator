package me.goldhardt.destinator.data.datasource

import me.goldhardt.destinator.data.model.itinerary.AIGenerateItineraryResponse
import me.goldhardt.destinator.data.model.itinerary.ItineraryItem

interface ItinerariesDataSource {

    suspend fun createItinerary(
        destinationItinerary: AIGenerateItineraryResponse
    ): Long

    suspend fun insertItinerary(
        destinationId: Long,
        itineraryDayId: Long,
        itinerary: ItineraryItem
    ): Long

    suspend fun updateItineraryItemOrder(
        itineraryDayId: Long,
        itineraryItemId: Long,
        offset: Int
    )
}