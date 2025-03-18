package me.goldhardt.destinator.data.datasource

import me.goldhardt.destinator.data.model.places.InterestPlace
import me.goldhardt.destinator.data.model.places.PlaceType

interface InterestPlaceDataSource {
    suspend fun insertInterestPlace(
        interestPlace: InterestPlace,
    ): Long
    suspend fun deleteInterestPlace(interestPlace: InterestPlace)
} 