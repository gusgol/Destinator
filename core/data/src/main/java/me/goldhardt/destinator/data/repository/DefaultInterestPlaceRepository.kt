package me.goldhardt.destinator.data.repository

import me.goldhardt.destinator.data.datasource.InterestPlaceDataSource
import me.goldhardt.destinator.data.model.places.InterestPlace
import javax.inject.Inject

class DefaultInterestPlaceRepository @Inject constructor(
    private val interestPlaceDataSource: InterestPlaceDataSource
) : InterestPlaceRepository {

    override suspend fun insertInterestPlace(interestPlace: InterestPlace): Long {
        return interestPlaceDataSource.insertInterestPlace(interestPlace)
    }

    override suspend fun deleteInterestPlace(interestPlace: InterestPlace) {
        interestPlaceDataSource.deleteInterestPlace(interestPlace)
    }
} 