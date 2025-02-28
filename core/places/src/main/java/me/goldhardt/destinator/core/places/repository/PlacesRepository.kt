package me.goldhardt.destinator.core.places.repository

import me.goldhardt.destinator.core.places.PlaceMetadata

interface PlacesRepository {
    suspend fun searchPlaces(
        query: String,
        latitude: Double,
        longitude: Double,
    ): List<PlaceMetadata>
}