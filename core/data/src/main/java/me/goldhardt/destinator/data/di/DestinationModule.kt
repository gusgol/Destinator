package me.goldhardt.destinator.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.goldhardt.destinator.data.repository.DefaultDestinationsRepository
import me.goldhardt.destinator.data.repository.DestinationsRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DestinationModule {

    @Binds
    abstract fun bindDestinationsRepository(
        defaultDestinationsRepository: DefaultDestinationsRepository
    ): DestinationsRepository
}