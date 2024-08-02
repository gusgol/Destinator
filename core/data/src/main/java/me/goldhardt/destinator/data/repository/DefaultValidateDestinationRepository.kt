package me.goldhardt.destinator.data.repository

import me.goldhardt.destinator.data.datasource.ValidateDestinationDataSource
import me.goldhardt.destinator.data.model.validation.ValidateDestinationResponse
import javax.inject.Inject

class DefaultValidateDestinationRepository @Inject constructor(
    private val validateDestinationDataSource: ValidateDestinationDataSource
) : ValidateDestinationRepository{
    override suspend fun validate(query: String): Result<ValidateDestinationResponse> {
        return validateDestinationDataSource.validate(query)
    }
}