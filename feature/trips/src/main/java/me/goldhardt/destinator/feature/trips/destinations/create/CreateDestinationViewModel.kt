package me.goldhardt.destinator.feature.trips.destinations.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.goldhardt.destinator.data.repository.DestinationsRepository
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

@HiltViewModel
class CreateDestinationViewModel @Inject constructor(
    private val destinationsRepository: DestinationsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateDestinationUiState>(CreateDestinationUiState.Creating())
    val uiState: StateFlow<CreateDestinationUiState> = _uiState

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
}