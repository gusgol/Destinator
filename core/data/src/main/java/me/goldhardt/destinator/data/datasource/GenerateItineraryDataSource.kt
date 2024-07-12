package me.goldhardt.destinator.data.datasource

import me.goldhardt.destinator.data.model.ItineraryItem

interface GenerateItineraryDataSource {
    /**
     * Returns the itinerary items
     */
    suspend fun generateItinerary(
        city: String,
        from: String,
        to: String,
        tripStyle: String
    ): Result<List<ItineraryItem>>
}