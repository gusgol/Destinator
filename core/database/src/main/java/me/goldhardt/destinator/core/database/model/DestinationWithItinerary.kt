package me.goldhardt.destinator.core.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class DestinationWithItinerary(
    @Embedded val destination: DestinationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "destination_id"
    )
    val itinerary: List<ItineraryItemEntity>
)