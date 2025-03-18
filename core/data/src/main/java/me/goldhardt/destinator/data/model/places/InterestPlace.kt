package me.goldhardt.destinator.data.model.places

import me.goldhardt.destinator.core.database.model.InterestPlaceEntity
import me.goldhardt.destinator.core.database.model.InterestPlaceWithPhotos

data class InterestPlace(
    val id: Long,
    val destinationId: Long,
    val name: String,
    val description: String,
    val longitude: Double,
    val latitude: Double,
    val address: String,
    val type: PlaceType,
    val iconUrl: String?,
    val metadataSourceId: String?,
    val photos: List<String> = emptyList()
) {

    companion object {
        private const val MAPS_URI =
            "https://www.google.com/maps/search/?api=1&query=%s&query_place_id=%s"
    }

    /**
     * TODO Repeated code. See [ItineraryItem]
     */
    val mapProviderUri: String?
        get() = metadataSourceId?.let { sourceId ->
            MAPS_URI.format(name, sourceId)
        }
}

fun InterestPlaceWithPhotos.toInterestPlace(): InterestPlace {
    return InterestPlace(
        id = interestPlace.id,
        destinationId = interestPlace.destinationId,
        name = interestPlace.name,
        description = interestPlace.description,
        longitude = interestPlace.longitude,
        latitude = interestPlace.latitude,
        address = interestPlace.address,
        iconUrl = interestPlace.iconUrl,
        metadataSourceId = interestPlace.metadataSourceId,
        photos = photos.map { it.reference },
        type = PlaceType.valueOf(interestPlace.type)
    )
}

fun InterestPlace.toEntity(): InterestPlaceEntity {
    return InterestPlaceEntity(
        id = id,
        destinationId = destinationId,
        name = name,
        description = description,
        longitude = longitude,
        latitude = latitude,
        address = address,
        iconUrl = iconUrl,
        metadataSourceId = metadataSourceId,
        type = type.name
    )
}