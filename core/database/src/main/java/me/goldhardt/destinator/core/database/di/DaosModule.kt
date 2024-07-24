package me.goldhardt.destinator.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.goldhardt.destinator.core.database.DestinatorDatabase

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun providesDestinationDao(database: DestinatorDatabase) = database.destinationDao()

    @Provides
    fun providesItineraryDao(database: DestinatorDatabase) = database.itineraryDao()

    @Provides
    fun providesPhotoDao(database: DestinatorDatabase) = database.photoDao()
}