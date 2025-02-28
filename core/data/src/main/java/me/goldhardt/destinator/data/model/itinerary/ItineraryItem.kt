package me.goldhardt.destinator.data.model.itinerary

import me.goldhardt.destinator.core.database.model.ItineraryItemEntity
import me.goldhardt.destinator.core.database.model.ItineraryItemWithPhotos

class ItineraryItem(
    var id: Long,
    var order: Int,
    val name: String,
    val description: String,
    val longitude: Double,
    val latitude: Double,
    val visitTimeMin: Int,
    val iconUrl: String?,
    val metadataSourceId: String?,
    val photos: List<String> = emptyList(),
) {
    companion object {
        private const val MAPS_URI =
            "https://www.google.com/maps/search/?api=1&query=%s&query_place_id=%s"
    }
    val mapProviderUri: String?
        get() = metadataSourceId?.let { sourceId ->
            MAPS_URI.format(name, sourceId)
        }
}

fun ItineraryItemWithPhotos.toItineraryItem(): ItineraryItem =
    ItineraryItem(
        id = itineraryItem.id,
        order = itineraryItem.order,
        name = itineraryItem.name,
        description = itineraryItem.description,
        longitude = itineraryItem.longitude,
        latitude = itineraryItem.latitude,
        visitTimeMin = itineraryItem.visitTimeMin,
        iconUrl = itineraryItem.iconUrl,
        metadataSourceId = itineraryItem.metadataSourceId,
        photos = photos.map { it.reference }
    )

fun ItineraryItem.toItineraryItemEntity(
    destinationId: Long,
    itineraryDayId: Long,
): ItineraryItemEntity =
    ItineraryItemEntity(
        destinationId = destinationId,
        itineraryDayId = itineraryDayId,
        order = order,
        name = name,
        description = description,
        longitude = longitude,
        latitude = latitude,
        visitTimeMin = visitTimeMin,
        iconUrl = iconUrl,
        metadataSourceId = metadataSourceId
    )
