package me.goldhardt.destinator.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.goldhardt.destinator.core.database.model.ItineraryItemEntity

@Dao
interface ItineraryDao {

    @Insert
    suspend fun insertItinerary(
        itineraryItem: ItineraryItemEntity
    ): Long

    @Insert
    suspend fun insertItinerary(
        itineraryItems: List<ItineraryItemEntity>
    ): List<Long>

    @Query("UPDATE itinerary_items SET name = :name, description = :description, latitude = :latitude, longitude = :longitude, visit_time_min = :visitTimeMin, trip_day = :tripDay WHERE id = :id")
    suspend fun updateItineraryItem(
        id: Long,
        name: String,
        description: String,
        latitude: Double,
        longitude: Double,
        visitTimeMin: Int,
        tripDay: Int
    )

    @Query("DELETE FROM itinerary_items WHERE id = :id")
    suspend fun deleteItineraryItem(id: Long)
}
