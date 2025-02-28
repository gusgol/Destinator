package me.goldhardt.destinator.feature.trips.destinations.addplace

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.goldhardt.destinator.core.designsystem.components.LoadingScreen
import me.goldhardt.destinator.core.places.ui.searchplaces.SearchPlaces
import me.goldhardt.destinator.feature.trips.R

@Composable
fun AddPlace(
    viewModel: AddPlaceViewModel = hiltViewModel(),
    onPlaceAdded: () -> Unit,
) {
    val destination by viewModel.destination.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        destination?.let {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                SearchPlaces(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    onPlaceClick = viewModel::addPlace,
                )
                /**
                 *  Right now saving is super fast. But in case we need to add a saving screen:
                 *
                 *        if (uiState == AddPlaceUiState.Saving) {
                 *            // composable
                 *        }
                 */

                if (uiState == AddPlaceUiState.Saved) {
                    LaunchedEffect(uiState) {
                        onPlaceAdded()
                    }
                }
            }
        } ?: run {
            LoadingScreen(R.string.title_loading)
        }
    }
}
