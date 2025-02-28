package me.goldhardt.destinator.core.places.repository

import me.goldhardt.destinator.core.places.PlaceMetadata
import me.goldhardt.destinator.core.places.PlacesDataSource
import javax.inject.Inject

class DefaultPlacesRepository @Inject constructor(
    private val dataSource: PlacesDataSource
) : PlacesRepository {
    override suspend fun searchPlaces(
        query: String,
        latitude: Double,
        longitude: Double
    ): List<PlaceMetadata> =
        dataSource.getPlaces(query, latitude, longitude)
}