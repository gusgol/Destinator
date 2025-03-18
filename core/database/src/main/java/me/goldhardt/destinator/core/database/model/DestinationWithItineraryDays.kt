package me.goldhardt.destinator.core.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class DestinationWithItineraryDays(
    @Embedded val destination: DestinationEntity,
    @Relation(
        entity = ItineraryDayEntity::class,
        parentColumn = "id",
        entityColumn = "destination_id"
    )
    val itineraryDays: List<ItineraryDayWithItems>,

    @Relation(
        entity = InterestPlaceEntity::class,
        parentColumn = "id",
        entityColumn = "destination_id"
    )
    val interestPlaces: List<InterestPlaceWithPhotos>,
)