package me.goldhardt.destinator.data.model.validation

import kotlinx.serialization.Serializable

@Serializable
data class ValidateDestinationResponse(
    val result: ValidateDestinationResult,
    val options: List<ValidateDestinationOption>
)

@Suppress("EnumEntryName")
@Serializable
enum class ValidateDestinationResult {
    success, multiple, non_existent
}

@Serializable
data class ValidateDestinationOption(
    val city: String,
    val state: String,
    val country: String,
)