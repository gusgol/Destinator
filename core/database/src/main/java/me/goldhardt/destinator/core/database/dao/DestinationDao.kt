package me.goldhardt.destinator.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import me.goldhardt.destinator.core.database.model.DestinationEntity
import me.goldhardt.destinator.core.database.model.DestinationWithItinerary

@Dao
interface DestinationDao {

    @Transaction
    @Query("SELECT * FROM destinations")
    fun getDestinationsWithItinerary(): Flow<List<DestinationWithItinerary>>

    @Insert
    suspend fun insertDestination(destination: DestinationEntity): Long
}