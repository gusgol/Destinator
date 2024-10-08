package me.goldhardt.destinator.feature.trips.destinations.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.goldhardt.destinator.data.model.validation.ValidateDestinationResult
import me.goldhardt.destinator.data.repository.DestinationsRepository
import me.goldhardt.destinator.data.repository.ValidateDestinationRepository
import me.goldhardt.destinator.feature.trips.R
import javax.inject.Inject

interface ItineraryRequest {
    val city: String
    val fromMs: Long
    val toMs: Long
    val tripStyle: List<String>
}

sealed class CreateDestinationUiState {
    data class Creating(
        override val city: String = "",
        override val fromMs: Long = 0,
        override val toMs: Long = 0,
        override val tripStyle: List<String> = listOf()
    ) : CreateDestinationUiState(), ItineraryRequest

    data class Processing(
        override val city: String,
        override val fromMs: Long,
        override val toMs: Long,
        override val tripStyle: List<String>
    ) : CreateDestinationUiState(), ItineraryRequest

    data class Generated(
        val destinationId: Long,
        val city: String
    ) : CreateDestinationUiState()

    data class Failed(
        override val city: String,
        override val fromMs: Long,
        override val toMs: Long,
        override val tripStyle: List<String>
    ) : CreateDestinationUiState(), ItineraryRequest
}

sealed class DestinationValidationState {
    data object Loading : DestinationValidationState()
    data object Success : DestinationValidationState()
    data class SuccessMultiple(
        val options: List<String>
    ) : DestinationValidationState()
    data class Invalid(
        val errorMessage: Int
    ) : DestinationValidationState()
}

@HiltViewModel
class CreateDestinationViewModel @Inject constructor(
    private val destinationsRepository: DestinationsRepository,
    private val validateDestinationRepository: ValidateDestinationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateDestinationUiState>(CreateDestinationUiState.Creating())
    val uiState: StateFlow<CreateDestinationUiState> = _uiState

    private val _destinationValidationState =
        MutableStateFlow<DestinationValidationState>(DestinationValidationState.Loading)
    val destinationValidationState: StateFlow<DestinationValidationState> = _destinationValidationState

    fun generate() {
        val itineraryRequest = _uiState.value as? ItineraryRequest ?: return
        viewModelScope.launch {
            _uiState.tryEmit(
                CreateDestinationUiState.Processing(
                    city = itineraryRequest.city,
                    fromMs = itineraryRequest.fromMs,
                    toMs = itineraryRequest.toMs,
                    tripStyle = itineraryRequest.tripStyle
                )
            )
            val result = destinationsRepository.createDestination(
                city = itineraryRequest.city,
                fromMs = itineraryRequest.fromMs,
                toMs = itineraryRequest.toMs,
                tripStyleList = itineraryRequest.tripStyle
            )
            result.getOrNull()?.let {
                _uiState.tryEmit(
                    CreateDestinationUiState.Generated(it, itineraryRequest.city)
                )
            } ?: run {
                _uiState.tryEmit(CreateDestinationUiState.Failed(
                    city = itineraryRequest.city,
                    fromMs = itineraryRequest.fromMs,
                    toMs = itineraryRequest.toMs,
                    tripStyle = itineraryRequest.tripStyle
                ))
            }
        }
    }

    fun setCity(city: String) {
        _uiState.update { currentState ->
            when (currentState) {
                /**
                 * TODO fix this repetition
                 */
                is CreateDestinationUiState.Creating -> currentState.copy(city = city)
                is CreateDestinationUiState.Processing -> currentState.copy(city = city)
                is CreateDestinationUiState.Failed -> currentState.copy(city = city)
                else -> currentState
            }
        }
    }

    fun setDates(fromMs: Long, toMs: Long) {
        _uiState.update { currentState ->
            when (currentState) {
                is CreateDestinationUiState.Creating -> currentState.copy(fromMs = fromMs, toMs = toMs)
                is CreateDestinationUiState.Processing -> currentState.copy(fromMs = fromMs, toMs = toMs)
                is CreateDestinationUiState.Failed -> currentState.copy(fromMs = fromMs, toMs = toMs)
                else -> currentState
            }
        }
    }

    fun setTripStyle(tripStyle: List<String>) {
        _uiState.update { currentState ->
            when (currentState) {
                is CreateDestinationUiState.Creating -> currentState.copy(tripStyle = tripStyle)
                is CreateDestinationUiState.Processing -> currentState.copy(tripStyle = tripStyle)
                is CreateDestinationUiState.Failed -> currentState.copy(tripStyle = tripStyle)
                else -> currentState
            }
        }
    }

    fun validateCity() {
        val state = _uiState.value as? ItineraryRequest ?: run {
            _destinationValidationState.tryEmit(DestinationValidationState.Invalid(R.string.error_generic))
            return
        }
        _destinationValidationState.tryEmit(DestinationValidationState.Loading)
        viewModelScope.launch {
            val result = validateDestinationRepository.validate(state.city)
            result.getOrNull()?.let { response ->
                val updatedState = when (response.result) {
                    ValidateDestinationResult.success -> DestinationValidationState.Success
                    ValidateDestinationResult.multiple -> DestinationValidationState.SuccessMultiple(
                        response.options.map { "${it.city}, ${it.state}, ${it.country}" }
                    )
                    ValidateDestinationResult.non_existent ->
                        DestinationValidationState.Invalid(R.string.error_city_non_existent)
                }
                _destinationValidationState.tryEmit(updatedState)
            }
        }
    }
}