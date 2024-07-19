package me.goldhardt.destinator.feature.trips.destinations.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.goldhardt.destinator.data.extensions.toDayMonth
import me.goldhardt.destinator.data.model.destination.Destination
import me.goldhardt.destinator.data.repository.DestinationsRepository
import me.goldhardt.destinator.feature.trips.DESTINATION_ID
import javax.inject.Inject

sealed interface DestinationDetailUiState {
    data object Loading : DestinationDetailUiState
    data class Success(
        val destination: Destination,
        val calendar: List<String>
    ) : DestinationDetailUiState
    data object Failed : DestinationDetailUiState
}

@HiltViewModel
class DestinationDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val destinationsRepository: DestinationsRepository
) : ViewModel() {

    private val destinationId: Long = checkNotNull(savedStateHandle[DESTINATION_ID])

    val uiState: StateFlow<DestinationDetailUiState> =
        destinationsRepository.getDestination(destinationId)
            .map { destination ->
                if (destination == null) {
                    DestinationDetailUiState.Failed
                } else {
                    DestinationDetailUiState.Success(
                        destination = destination,
                        calendar = getCalendarDays(destination)
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DestinationDetailUiState.Loading,
            )

    private fun getCalendarDays(destination: Destination): List<String> {
        return destination
            .itinerary
            .sortedBy { it.date }
            .map { it.date.toDayMonth() }
            .distinct()
    }
}