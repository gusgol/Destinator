package me.goldhardt.destinator.data.model.itinerary

import kotlinx.serialization.Serializable

@Serializable
data class AICreatedItineraryItem(
    val date: String,
    val name: String,
    val description: String,
    val longitude: Double,
    val latitude: Double,
    val visitTimeMin: Int,
    val tripDay: Int
)