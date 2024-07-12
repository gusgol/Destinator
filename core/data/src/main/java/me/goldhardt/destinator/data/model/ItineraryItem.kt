package me.goldhardt.destinator.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ItineraryItem(
    val date: Long,
    val name: String,
    val description: String,
    val longitude: Double,
    val latitude: Double,
    val visitTimeMin: Int
)