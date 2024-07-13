package me.goldhardt.destinator.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.goldhardt.destinator.data.datasource.GenerateItineraryAIDataSource
import me.goldhardt.destinator.data.datasource.GenerateItineraryDataSource
import me.goldhardt.destinator.data.repository.DefaultGenerateItineraryRepository
import me.goldhardt.destinator.data.repository.GenerateItineraryRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class GenerateItineraryModule {

    @Binds
    abstract fun bindGenerateItineraryDataSource(
        generateItineraryAiDataSource: GenerateItineraryAIDataSource
    ): GenerateItineraryDataSource

    @Binds
    abstract fun bingGenerateItineraryRepository(
        defaultGenerateItineraryRepository: DefaultGenerateItineraryRepository
    ): GenerateItineraryRepository
}