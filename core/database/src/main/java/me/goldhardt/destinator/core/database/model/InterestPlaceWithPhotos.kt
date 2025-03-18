package me.goldhardt.destinator.core.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class InterestPlaceWithPhotos(
    @Embedded val interestPlace: InterestPlaceEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "parent_id",
    )
    val photos: List<PhotoEntity>
)