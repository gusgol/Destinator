package me.goldhardt.destinator.core.places.ui.searchplaces

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.goldhardt.destinator.core.designsystem.components.PlacePhoto
import me.goldhardt.destinator.core.designsystem.theme.DestinatorTheme
import me.goldhardt.destinator.core.places.PlaceMetadata
import me.goldhardt.destinator.core.places.R

@Composable
fun SearchPlaces(
    latitude: Double,
    longitude: Double,
    viewModel: SearchPlacesViewModel = hiltViewModel(),
    onPlaceClick: (PlaceMetadata) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchPlaces(
        uiState = uiState,
        onSearch = { viewModel.searchPlaces(it, latitude, longitude) },
        onPlaceClick = onPlaceClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchPlaces(
    uiState: AddPlaceUiState,
    onSearch: (String) -> Unit,
    onPlaceClick: (PlaceMetadata) -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val backgroundColor = MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.5f)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth(),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchQuery,
                        onSearch = {
                            onSearch(it)
                        },
                        onQueryChange = {
                            searchQuery = it
                        },
                        expanded = false,
                        onExpandedChange = {},
                        placeholder = {
                            Text(
                                stringResource(R.string.title_search),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    )
                },
                expanded = false,
                onExpandedChange = {},
                colors = SearchBarDefaults.colors(
                    containerColor = backgroundColor,
                ),
            ) {}
        }
        when (uiState) {
            AddPlaceUiState.Idle -> IdleState()
            AddPlaceUiState.Loading -> LoadingState()
            is AddPlaceUiState.Success -> {
                val places = uiState.places
                if (places.isEmpty()) {
                    EmptyState()
                } else {
                    PlacesList(places, onPlaceClick)
                }
            }
            AddPlaceUiState.Failed -> ErrorState()
        }
    }
}


@Composable
private fun IdleState() {
    EmptyStateMessage(
        icon = Icons.Outlined.Search,
        message = stringResource(R.string.title_add_place_idle)
    )
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState() {
    EmptyStateMessage(
        icon = Icons.Outlined.LocationOn,
        message = stringResource(R.string.title_add_place_no_results)
    )
}

@Composable
private fun ErrorState() {
    EmptyStateMessage(
        icon = Icons.Filled.Warning,
        message = stringResource(R.string.error_add_place_search)
    )
}

@Composable
private fun EmptyStateMessage(
    icon: ImageVector,
    message: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(message)
    }
}

@Composable
private fun PlacesList(
    places: List<PlaceMetadata>,
    onPlaceClick: (PlaceMetadata) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(places) { place ->
            PlaceItem(place) {
                onPlaceClick(place)
            }
        }
    }
}

@Composable
private fun PlaceItem(
    place: PlaceMetadata,
    onPlaceClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlaceClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                place.address?.let { address ->
                    Text(
                        text = address,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }
                Text(
                    text = place.displayName.orEmpty(),
                    style = MaterialTheme.typography.titleMedium
                )
                place.description?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            PlacePhoto(
                photoReference = place.photosReferences?.firstOrNull().orEmpty(),
                maxWidthPx = 200,
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlacesListPreview() {
    DestinatorTheme {
        SearchPlaces(
            onSearch = {},
            onPlaceClick = {},
            uiState = AddPlaceUiState.Success(
                listOf(
                    PlaceMetadata(
                        sourceId = "1",
                        iconUrl = null,
                        latitude = 0.0,
                        longitude = 0.0,
                        photosReferences = null,
                        displayName = "Eiffel Tower",
                        description = "Designed by Gustave Eiffel in 1887",
                        address = "Address 1"
                    ),
                    PlaceMetadata(
                        sourceId = "2",
                        iconUrl = null,
                        latitude = 0.0,
                        longitude = 0.0,
                        photosReferences = null,
                        displayName = "Place 2",
                        description = "Description 2",
                        address = "Address 2"
                    )
                )
            )
        )
    }
}