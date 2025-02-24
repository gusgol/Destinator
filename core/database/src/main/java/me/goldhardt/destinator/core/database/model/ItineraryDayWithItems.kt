package me.goldhardt.destinator.core.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class ItineraryDayWithItems(
    @Embedded val itineraryDay: ItineraryDayEntity,
    @Relation(
        entity = ItineraryItemEntity::class,
        parentColumn = "id",
        entityColumn = "itinerary_day_id"
    )
    val itineraryItems: List<ItineraryItemWithPhotos>
)