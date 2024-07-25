package me.goldhardt.destinator.feature.trips.destinations.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.goldhardt.destinator.core.designsystem.components.PlacePhoto
import me.goldhardt.destinator.core.designsystem.components.SubtleHorizontalDivider
import me.goldhardt.destinator.data.extensions.formatDates
import me.goldhardt.destinator.data.model.destination.Destination
import me.goldhardt.destinator.feature.trips.R

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
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            PlacePhoto(
                imageUrl = destination.thumbnail,
                maxWidthPx = 600,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1.2f),
                ) {
                    Text(
                        text = "${destination.city}, ${destination.country}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Rounded.DateRange,
                            contentDescription = "Icon Description",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = formatDates(destination.from, destination.to),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            letterSpacing = 0.sp,
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    SubtleHorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.title_total),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                        Text(
                            text = stringResource(
                                R.string.title_s_places,
                                destination.itinerary.count()
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                        )

                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .height(100.dp)
                ) {

                }
            }

        }
    }
}