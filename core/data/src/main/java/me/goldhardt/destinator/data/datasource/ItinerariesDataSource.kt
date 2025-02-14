package me.goldhardt.destinator.data.datasource

import me.goldhardt.destinator.data.model.itinerary.AIGenerateItineraryResponse

interface ItinerariesDataSource {
    suspend fun createItinerary(
        destinationItinerary: AIGenerateItineraryResponse
    ): Long

    suspend fun addPlaceToItinerary(
        destinationId: Long,
        placeName: String,
        placeDescription: String,
        latitude: Double,
        longitude: Double,
        visitTimeMin: Int,
        tripDay: Int
    ): Long

    suspend fun deletePlaceFromItinerary(
        placeId: Long
    )
}
