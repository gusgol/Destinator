package me.goldhardt.destinator.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import me.goldhardt.destinator.core.database.model.InterestPlaceEntity

@Dao
interface InterestPlaceDao {

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertInterestPlace(interestPlace: InterestPlaceEntity): Long

    @Delete
    suspend fun deleteInterestPlace(interestPlace: InterestPlaceEntity)
} 