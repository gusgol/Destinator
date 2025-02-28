package me.goldhardt.destinator.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import me.goldhardt.destinator.core.database.model.ItineraryDayEntity
import me.goldhardt.destinator.core.database.model.ItineraryDayWithItems
import me.goldhardt.destinator.core.database.model.ItineraryItemEntity

@Dao
interface ItineraryDao {

    @Insert
    suspend fun insertItinerary(
        itineraryItem: ItineraryItemEntity
    ): Long

    @Insert
    suspend fun insertItineraryDay(
        itineraryDay: ItineraryDayEntity
    ): Long

    @Transaction
    suspend fun swapItineraryItems(
        itineraryItem1: ItineraryItemEntity,
        itineraryItem2: ItineraryItemEntity
    ) {
        updateItinerary(itineraryItem1)
        updateItinerary(itineraryItem2)
    }

    @Update
    suspend fun updateItinerary(
        itineraryItem: ItineraryItemEntity
    )

    @Update
    suspend fun updateItineraries(itineraryItems: List<ItineraryItemEntity>)

    @Query("SELECT * FROM itinerary_days WHERE id = :itineraryDayId")
    suspend fun getItineraryDayById(
        itineraryDayId: Long
    ): ItineraryDayWithItems?

    @Query("SELECT * FROM itinerary_days WHERE day = :day AND destination_id = :destinationId")
    suspend fun getItineraryByDay(
        destinationId: Long,
        day: Int
    ): ItineraryDayWithItems?
}