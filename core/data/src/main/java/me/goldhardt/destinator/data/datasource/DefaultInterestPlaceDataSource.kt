package me.goldhardt.destinator.data.datasource

import me.goldhardt.destinator.core.common.PhotoSource
import me.goldhardt.destinator.core.database.dao.InterestPlaceDao
import me.goldhardt.destinator.core.database.dao.PhotoDao
import me.goldhardt.destinator.core.database.model.ParentType
import me.goldhardt.destinator.core.database.model.PhotoEntity
import me.goldhardt.destinator.data.model.places.InterestPlace
import me.goldhardt.destinator.data.model.places.toEntity
import javax.inject.Inject

class DefaultInterestPlaceDataSource @Inject constructor(
    private val interestPlaceDao: InterestPlaceDao,
    private val photoDao: PhotoDao
) : InterestPlaceDataSource {

    override suspend fun insertInterestPlace(
        interestPlace: InterestPlace,
    ): Long {
        val placeId = interestPlaceDao.insertInterestPlace(interestPlace.toEntity())
        photoDao.insertPhotos(
            interestPlace.photos.map {
                PhotoEntity(
                    parentId = placeId,
                    parentType = ParentType.INTEREST_PLACE.value,
                    reference = it,
                    source = PhotoSource.GOOGLE_PLACES
                )
            }
        )
        return placeId
    }

    override suspend fun deleteInterestPlace(interestPlace: InterestPlace) {
        interestPlaceDao.deleteInterestPlace(interestPlace.toEntity())
    }
} 