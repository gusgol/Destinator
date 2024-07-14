package me.goldhardt.destinator.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "itinerary_items",
    foreignKeys = [ForeignKey(
        entity = DestinationEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("destination_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ItineraryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "destination_id") val destinationId: Long,
    @ColumnInfo(name = "order") val order: Int,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "visit_time_min") val visitTimeMin: Int,
    @ColumnInfo(name = "thumbnail") val thumbnail: String
)