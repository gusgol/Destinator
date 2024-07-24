package me.goldhardt.destinator.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import me.goldhardt.destinator.core.database.dao.DestinationDao
import me.goldhardt.destinator.core.database.dao.ItineraryDao
import me.goldhardt.destinator.core.database.dao.PhotoDao
import me.goldhardt.destinator.core.database.model.DestinationEntity
import me.goldhardt.destinator.core.database.model.ItineraryItemEntity
import me.goldhardt.destinator.core.database.model.PhotoEntity

@Database(
    entities = [
        DestinationEntity::class,
        ItineraryItemEntity::class,
        PhotoEntity::class,
    ],
    version = 1
)
abstract class DestinatorDatabase : RoomDatabase() {
    abstract fun destinationDao(): DestinationDao
    abstract fun itineraryDao(): ItineraryDao
    abstract fun photoDao(): PhotoDao
}
