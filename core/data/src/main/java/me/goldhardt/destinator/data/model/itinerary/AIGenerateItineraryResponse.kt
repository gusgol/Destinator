package me.goldhardt.destinator.data.model.itinerary

import kotlinx.serialization.Serializable

@Serializable
data class AIGenerateItineraryResponse(
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val itinerary: List<AICreatedItineraryItem>
)