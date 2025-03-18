package me.goldhardt.destinator.data.repository

import me.goldhardt.destinator.data.model.places.InterestPlace

interface InterestPlaceRepository {
    suspend fun insertInterestPlace(interestPlace: InterestPlace): Long
    suspend fun deleteInterestPlace(interestPlace: InterestPlace)
} 