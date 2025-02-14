package me.goldhardt.destinator.feature.trips.destinations.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.goldhardt.destinator.data.extensions.formatDate
import me.goldhardt.destinator.data.model.itinerary.ItineraryItem
import me.goldhardt.destinator.data.repository.DestinationsRepository
import me.goldhardt.destinator.feature.trips.DESTINATION_ID
import javax.inject.Inject

sealed interface EditDestinationUiState {
    data object Loading : EditDestinationUiState
    data class Success(
        val dayItineraries: List<DayItinerary>,
    ) : EditDestinationUiState

    data object Failed : EditDestinationUiState
}

data class DayItinerary(
    val day: Int,
    val date: String,
    val items: List<ItineraryItem>
)

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
                    EditDestinationUiState.Success(groupItineraryByDay(it.itinerary))
                } ?: EditDestinationUiState.Failed
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = EditDestinationUiState.Loading,
            )

    private fun groupItineraryByDay(itinerary: List<ItineraryItem>): List<DayItinerary> {
        return itinerary.groupBy { it.tripDay }
            .map { (day, items) ->
                DayItinerary(
                    day = day,
                    date = formatDate(items.first().date),
                    items = items
                )
            }
            .sortedBy { it.day }
    }
}