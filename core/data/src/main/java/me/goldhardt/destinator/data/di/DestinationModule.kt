package me.goldhardt.destinator.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.goldhardt.destinator.data.datasource.ItinerariesDataSource
import me.goldhardt.destinator.data.datasource.ItinerariesLocalDataSource
import me.goldhardt.destinator.data.repository.DefaultItinerariesRepository
import me.goldhardt.destinator.data.repository.DefaultDestinationsRepository
import me.goldhardt.destinator.data.repository.DestinationsRepository
import me.goldhardt.destinator.data.repository.ItinerariesRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DestinationModule {

    @Binds
    abstract fun bindDestinationsRepository(
        defaultDestinationsRepository: DefaultDestinationsRepository
    ): DestinationsRepository

    @Binds
    abstract fun bindItinerariesRepository(
        defaultItinerariesRepository: DefaultItinerariesRepository
    ): ItinerariesRepository

    @Binds
    abstract fun bindItinerariesDataSource(
        dataSource: ItinerariesLocalDataSource
    ): ItinerariesDataSource
}