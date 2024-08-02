package me.goldhardt.destinator.data.repository

import me.goldhardt.destinator.data.model.validation.ValidateDestinationResponse

interface ValidateDestinationRepository {
    suspend fun validate(query: String): Result<ValidateDestinationResponse>
}