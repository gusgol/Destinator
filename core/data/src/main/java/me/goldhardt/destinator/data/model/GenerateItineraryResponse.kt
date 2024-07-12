package me.goldhardt.destinator.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerateItineraryResponse(
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val itinerary: List<ItineraryItem>
)