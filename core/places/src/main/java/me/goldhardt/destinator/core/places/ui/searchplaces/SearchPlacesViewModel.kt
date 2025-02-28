package me.goldhardt.destinator.core.places.ui.searchplaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.goldhardt.destinator.core.places.PlaceMetadata
import me.goldhardt.destinator.core.places.repository.PlacesRepository
import javax.inject.Inject

sealed interface AddPlaceUiState {
    data object Idle : AddPlaceUiState
    data object Loading : AddPlaceUiState
    data class Success(
        val places: List<PlaceMetadata> = emptyList(),
    ) : AddPlaceUiState

    data object Failed : AddPlaceUiState
}

@HiltViewModel
class SearchPlacesViewModel @Inject constructor(
    private val repository: PlacesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddPlaceUiState>(AddPlaceUiState.Idle)
    val uiState: StateFlow<AddPlaceUiState> = _uiState

    fun searchPlaces(query: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = AddPlaceUiState.Loading
            try {
                val places = repository.searchPlaces(query, latitude, longitude)
                _uiState.value = AddPlaceUiState.Success(places)
            } catch (e: Exception) {
                _uiState.value = AddPlaceUiState.Failed
            }
        }
    }
}