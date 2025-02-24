package me.goldhardt.destinator.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "itinerary_days",
    foreignKeys = [ForeignKey(
        entity = DestinationEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("destination_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ItineraryDayEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "destination_id") val destinationId: Long,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "day") val day: Int,
)