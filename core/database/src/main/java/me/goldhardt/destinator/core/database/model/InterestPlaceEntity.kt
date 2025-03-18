package me.goldhardt.destinator.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "interest_places",
)
data class InterestPlaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "destination_id") val destinationId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "icon_url") val iconUrl: String?,
    @ColumnInfo(name = "metadata_source_id") val metadataSourceId: String?,
)