package me.goldhardt.destinator.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.goldhardt.destinator.data.datasource.ValidateDestinationAIDataSource
import me.goldhardt.destinator.data.datasource.ValidateDestinationDataSource
import me.goldhardt.destinator.data.repository.DefaultValidateDestinationRepository
import me.goldhardt.destinator.data.repository.ValidateDestinationRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class ValidateDestinationModule {

    @Binds
    abstract fun bindValidateDestinationDataSource(
        validateDestinationDataSource: ValidateDestinationAIDataSource
    ): ValidateDestinationDataSource

    @Binds
    abstract fun bindValidateDestinationRepository(
        defaultValidateDestinationRepository: DefaultValidateDestinationRepository
    ): ValidateDestinationRepository
}