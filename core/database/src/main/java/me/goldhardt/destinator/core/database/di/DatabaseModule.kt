package me.goldhardt.destinator.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.goldhardt.destinator.core.database.DestinatorDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesDestinatorDatabase(
        @ApplicationContext context: Context,
    ): DestinatorDatabase = Room.databaseBuilder(
        context,
        DestinatorDatabase::class.java,
        "destinator-database",
    ).build()
}