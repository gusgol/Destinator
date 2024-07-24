package me.goldhardt.destinator.data.model.itinerary

import me.goldhardt.destinator.core.database.model.ItineraryItemWithPhotos

class ItineraryItem(
    val order: Int,
    val date: String,
    val name: String,
    val description: String,
    val longitude: Double,
    val latitude: Double,
    val visitTimeMin: Int,
    val tripDay: Int,
    val iconUrl: String?,
    val metadataSourceId: String?,
    val photos: List<String> = emptyList()
)

fun ItineraryItemWithPhotos.toItineraryItem(): ItineraryItem =
    ItineraryItem(
        order = itineraryItem.order,
        date = itineraryItem.date,
        name = itineraryItem. name,
        description = itineraryItem.description,
        longitude = itineraryItem.longitude,
        latitude = itineraryItem.latitude,
        visitTimeMin = itineraryItem.visitTimeMin,
        tripDay = itineraryItem.tripDay,
        iconUrl = itineraryItem.iconUrl,
        metadataSourceId = itineraryItem.metadataSourceId,
        photos = photos.map { it.url }
    )
