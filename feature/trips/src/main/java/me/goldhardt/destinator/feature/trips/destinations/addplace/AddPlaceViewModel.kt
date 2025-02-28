package me.goldhardt.destinator.feature.trips.destinations.addplace

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.goldhardt.destinator.core.places.PlaceMetadata
import me.goldhardt.destinator.data.model.destination.Destination
import me.goldhardt.destinator.data.model.itinerary.ItineraryItem
import me.goldhardt.destinator.data.repository.DestinationsRepository
import me.goldhardt.destinator.data.repository.ItinerariesRepository
import me.goldhardt.destinator.feature.trips.DESTINATION_ID
import me.goldhardt.destinator.feature.trips.ITINERARY_DAY_ID
import javax.inject.Inject

sealed interface AddPlaceUiState {
    data object Idle : AddPlaceUiState
    data object Saving : AddPlaceUiState
    data object Saved : AddPlaceUiState
}

@HiltViewModel
class AddPlaceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    destinationsRepository: DestinationsRepository,
    private val itinerariesRepository: ItinerariesRepository
) : ViewModel() {

    private val destinationId: Long = checkNotNull(savedStateHandle[DESTINATION_ID])
    private val itineraryDayId: Long = checkNotNull(savedStateHandle[ITINERARY_DAY_ID])
    val destination: StateFlow<Destination?> =
        destinationsRepository.getDestination(destinationId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null,
            )

    private val _uiState = MutableStateFlow<AddPlaceUiState>(AddPlaceUiState.Idle)
    val uiState: StateFlow<AddPlaceUiState> = _uiState

    fun addPlace(place: PlaceMetadata) {
        _uiState.value = AddPlaceUiState.Saving
        val destination = destination.value ?: return
        val order = destination.itineraryDays.find {
            it.id == itineraryDayId
        }?.items?.size ?: Int.MAX_VALUE

        viewModelScope.launch {
            itinerariesRepository.insertItinerary(
                destinationId,
                itineraryDayId,
                ItineraryItem(
                    id = 0, // Temp Id - won't be used to generate the entity
                    name = place.displayName.orEmpty(),
                    order = order,
                    description = place.description ?: place.address.orEmpty(),
                    longitude = place.longitude ?: destination.longitude,
                    latitude = place.latitude ?: destination.latitude,
                    visitTimeMin = 60,
                    iconUrl = place.iconUrl,
                    metadataSourceId = place.sourceId,
                    photos = place.photosReferences.orEmpty()
                )
            )
            _uiState.value = AddPlaceUiState.Saved
        }
    }
}