package me.goldhardt.destinator.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "photos",
    foreignKeys = [ForeignKey(
        entity = ItineraryItemEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("parent_id"),
        onDelete = ForeignKey.CASCADE
    )]

)
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "parent_id") val parentId: Long,
    @ColumnInfo(name = "parent_type") val parentType: String,
    @ColumnInfo(name = "reference") val reference: String,
    @ColumnInfo(name = "source") val source: String? = null,
)

/**
 * Holds possible values for parentType in [PhotoEntity]
 */
enum class ParentType(val value: String) {
    INTEREST_PLACE("interest_place"),
    ITINERARY_ITEM("itinerary_item");

    companion object {
        fun fromValue(value: String): ParentType {
            return entries.firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Unknown parent type: $value")
        }
    }
}

