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
import me.goldhardt.destinator.data.model.places.InterestPlace
import me.goldhardt.destinator.data.model.places.PlaceType
import me.goldhardt.destinator.data.repository.DestinationsRepository
import me.goldhardt.destinator.data.repository.InterestPlaceRepository
import me.goldhardt.destinator.feature.trips.DESTINATION_ID
import me.goldhardt.destinator.feature.trips.TYPE
import javax.inject.Inject

@HiltViewModel
class AddInterestPlaceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    destinationsRepository: DestinationsRepository,
    private val interestPlaceRepository: InterestPlaceRepository
) : ViewModel() {

    private val destinationId: Long = checkNotNull(savedStateHandle[DESTINATION_ID])
    private val placeType: PlaceType =
        PlaceType.valueOf(checkNotNull(savedStateHandle[TYPE])) // TODO Check if needed
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
        viewModelScope.launch {
            interestPlaceRepository.insertInterestPlace(
                InterestPlace(
                    id = 0,
                    name = place.displayName.orEmpty(),
                    address = place.address.orEmpty(),
                    latitude = place.latitude ?: 0.0,
                    longitude = place.longitude ?: 0.0,
                    photos = place.photosReferences.orEmpty(),
                    destinationId = destinationId,
                    description = place.description.orEmpty(),
                    type = placeType,
                    iconUrl = place.iconUrl.orEmpty(),
                    metadataSourceId = place.sourceId
                )
            )
            _uiState.emit(AddPlaceUiState.Saved)
        }
    }
}