package me.goldhardt.destinator.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.goldhardt.destinator.data.datasource.DefaultInterestPlaceDataSource
import me.goldhardt.destinator.data.datasource.InterestPlaceDataSource
import me.goldhardt.destinator.data.repository.DefaultInterestPlaceRepository
import me.goldhardt.destinator.data.repository.InterestPlaceRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class InterestPlaceModule {

    @Binds
    abstract fun bindInterestPlaceRepository(
        defaultDestinationsRepository: DefaultInterestPlaceRepository
    ): InterestPlaceRepository

    @Binds
    abstract fun bindInterestPlaceDataSource(
        dataSource: DefaultInterestPlaceDataSource
    ): InterestPlaceDataSource
}