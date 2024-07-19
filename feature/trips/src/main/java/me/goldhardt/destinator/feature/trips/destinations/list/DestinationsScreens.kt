package me.goldhardt.destinator.feature.trips.destinations.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.goldhardt.destinator.data.model.destination.Destination

@Composable
fun DestinationsRoute(
    destinationsViewModel: DestinationsViewModel = hiltViewModel(),
    onDestinationClick: (Long) -> Unit,
    onCreateTripClick: () -> Unit,
) {
    val uiState by destinationsViewModel.uiState.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        DestinationsList(uiState, onDestinationClick)
        ExtendedFloatingActionButton(
            text = {
                Text("Add Trip")
            },
            onClick = onCreateTripClick,
            icon = {
                Icon(
                    painter = painterResource(me.goldhardt.destinator.core.designsystem.R.drawable.ic_travel),
                    contentDescription = "Add Trip"
                )
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
fun DestinationsList(
    uiState: DestinationsUiState,
    onClick: (Long) -> Unit
) {
    when (uiState) {
        DestinationsUiState.Loading -> {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
        is DestinationsUiState.Success -> {
            DestinationsList(destinations = uiState.destinations, onClick)
        }
        DestinationsUiState.Empty -> {
            Text(
                text = "No destinations found",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
        DestinationsUiState.Failed -> {
            Text(
                text = "Failed to load destinations",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun DestinationsList(
    destinations: List<Destination>,
    onClick: (Long) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(destinations) { destination ->
            DestinationListItem(destination, onClick)
        }
    }
}

@Composable
fun DestinationListItem(
    destination: Destination,
    onClick: (Long) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(destination.id) }
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            Text(
                text = "${destination.city}, ${destination.country}",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "$${destination.from} - ${destination.to}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}