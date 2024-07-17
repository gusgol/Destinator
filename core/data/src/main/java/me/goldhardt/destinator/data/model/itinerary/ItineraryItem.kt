package me.goldhardt.destinator.data.model.itinerary

import me.goldhardt.destinator.core.database.model.ItineraryItemEntity

class ItineraryItem(
    val order: Int,
    val date: Long,
    val name: String,
    val description: String,
    val longitude: Double,
    val latitude: Double,
    val visitTimeMin: Int,
    val thumbnail: String
)

fun ItineraryItemEntity.toItineraryItem(): ItineraryItem =
    ItineraryItem(
        order = order,
        date = date,
        name = name,
        description = description,
        longitude = longitude,
        latitude = latitude,
        visitTimeMin = visitTimeMin,
        thumbnail = thumbnail
    )
