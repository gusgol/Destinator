package me.goldhardt.destinator.data.datasource

import me.goldhardt.destinator.data.model.validation.ValidateDestinationResponse

interface ValidateDestinationDataSource {
    suspend fun validate(query: String): Result<ValidateDestinationResponse>
}