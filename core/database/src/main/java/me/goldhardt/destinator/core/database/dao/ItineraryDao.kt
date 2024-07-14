package me.goldhardt.destinator.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import me.goldhardt.destinator.core.database.model.ItineraryItemEntity

@Dao
interface ItineraryDao {

    @Insert
    suspend fun insertItinerary(
        itineraryItems: List<ItineraryItemEntity>
    )
}