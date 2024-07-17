package me.goldhardt.destinator.feature.trips.destinations.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DestinationDetail(
    destinationDetailViewModel: DestinationDetailViewModel = hiltViewModel()
) {
    val uiState by destinationDetailViewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        DestinationDetailUiState.Failed -> {
            Text(text = "Failed...")
        }
        DestinationDetailUiState.Loading -> {
            Text(text = "Loading...")
        }
        is DestinationDetailUiState.Success -> {
            val destination = (uiState as DestinationDetailUiState.Success).destination
            Text(text = destination.city + "\n\n${destination.itinerary.joinToString(separator = "\n")}")
        }
    }
}