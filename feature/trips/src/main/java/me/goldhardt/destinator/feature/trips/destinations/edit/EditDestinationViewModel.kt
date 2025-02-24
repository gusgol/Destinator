package me.goldhardt.destinator.feature.trips.destinations.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.goldhardt.destinator.data.model.itinerary.ItineraryDay
import me.goldhardt.destinator.data.repository.DestinationsRepository
import me.goldhardt.destinator.feature.trips.DESTINATION_ID
import javax.inject.Inject

sealed interface EditDestinationUiState {
    data object Loading : EditDestinationUiState
    data class Success(
        val itineraryDays: List<ItineraryDay>,
    ) : EditDestinationUiState

    data object Failed : EditDestinationUiState
}

@HiltViewModel
class EditDestinationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    destinationsRepository: DestinationsRepository
) : ViewModel() {

    private val destinationId: Long = checkNotNull(savedStateHandle[DESTINATION_ID])

    val uiState: StateFlow<EditDestinationUiState> =
        destinationsRepository.getDestination(destinationId)
            .map { destination ->
                destination?.let {
                    EditDestinationUiState.Success(it.itineraryDays)
                } ?: EditDestinationUiState.Failed
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = EditDestinationUiState.Loading,
            )

}