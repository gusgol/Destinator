package me.goldhardt.destinator.core.places.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.goldhardt.destinator.core.places.PlacesDataSource
import me.goldhardt.destinator.core.places.googleplaces.GooglePlacesDataSource
import me.goldhardt.destinator.core.places.repository.DefaultPlacesRepository
import me.goldhardt.destinator.core.places.repository.PlacesRepository
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

@Module
@InstallIn(SingletonComponent::class)
abstract class PlacesBindingModule {
    @Binds
    abstract fun bindPlacesRepository(
        defaultPlacesRepository: DefaultPlacesRepository
    ): PlacesRepository
}