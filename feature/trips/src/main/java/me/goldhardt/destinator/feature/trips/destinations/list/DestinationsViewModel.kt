package me.goldhardt.destinator.feature.trips.destinations.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.goldhardt.destinator.data.model.destination.Destination
import me.goldhardt.destinator.data.repository.DestinationsRepository
import javax.inject.Inject

sealed interface DestinationsUiState {
    data object Loading : DestinationsUiState
    data class Success(
        val destinations: List<Destination>
    ) : DestinationsUiState
    data object Empty : DestinationsUiState
    data object Failed : DestinationsUiState
}

@HiltViewModel
class DestinationsViewModel @Inject constructor(
    private val destinationsRepository: DestinationsRepository
) : ViewModel() {

    val uiState: StateFlow<DestinationsUiState> =
        destinationsRepository.getDestinations()
            .map { destinations ->
                if (destinations.isEmpty()) {
                    DestinationsUiState.Empty
                } else {
                    DestinationsUiState.Success(destinations)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DestinationsUiState.Loading,
            )
}