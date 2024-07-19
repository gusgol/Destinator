package me.goldhardt.destinator.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "destinations",
)
data class DestinationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "from_date") val from: String,
    @ColumnInfo(name = "to_date") val to: String,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
)