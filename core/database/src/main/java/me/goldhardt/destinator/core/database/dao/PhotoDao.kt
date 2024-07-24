package me.goldhardt.destinator.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import me.goldhardt.destinator.core.database.model.PhotoEntity

@Dao
interface PhotoDao {
    @Insert
    suspend fun insertPhotos(
        photos: List<PhotoEntity>
    )
}