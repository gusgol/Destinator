package me.goldhardt.destinator.core.places.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.goldhardt.destinator.core.places.PlacesDataSource
import me.goldhardt.destinator.core.places.googleplaces.GooglePlacesDataSource
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object PlacesModule {
    @Provides
    @Singleton
    fun providePlacesDataSource(@ApplicationContext context: Context): PlacesDataSource {
        return GooglePlacesDataSource(context)
    }
}