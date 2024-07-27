package me.goldhardt.destinator.feature.trips.destinations.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import me.goldhardt.destinator.core.designsystem.components.ElevatedIcon
import me.goldhardt.destinator.core.designsystem.components.PlacePhotos
import me.goldhardt.destinator.core.designsystem.components.SubtleHorizontalDivider
import me.goldhardt.destinator.core.designsystem.components.SubtleVerticalDivider
import me.goldhardt.destinator.core.designsystem.theme.DestinatorTheme
import me.goldhardt.destinator.data.model.itinerary.ItineraryItem
import me.goldhardt.destinator.feature.trips.R
import java.time.Duration
import java.time.LocalTime

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
            DestinationDetail(uiState = uiState as DestinationDetailUiState.Success)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationDetail(
    uiState: DestinationDetailUiState.Success
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val selectedItems = uiState.destination.itinerary.filter {
        it.tripDay == uiState.calendar[selectedTab].day
    }
    Column {
        DestinationMap(
            LatLng(uiState.destination.latitude, uiState.destination.longitude),
            selectedItems,
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.div(2.4).dp)
        )
        SecondaryScrollableTabRow(
            selectedTabIndex = selectedTab,
            divider = {
            },
            indicator = {
            }
        ) {
            uiState.calendar.forEachIndexed { index, tripDay ->
                val isSelected = selectedTab == index
                val tabBackgroundColor = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                }
                Tab(
                    selected = isSelected,
                    onClick = { selectedTab = index },
                    selectedContentColor = MaterialTheme.colorScheme.surface,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .background(color = tabBackgroundColor, shape = RoundedCornerShape(4.dp))
                        .width(120.dp)
                        .height(80.dp),
                    text = {
                        Column {
                            Text(
                                text = stringResource(R.string.title_trip_day, tripDay.day),
                                maxLines = 1,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = tripDay.date,
                                maxLines = 1,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                )
            }
        }
        DayItinerary(
            items = selectedItems
        )
    }
}

@Composable
fun DestinationMap(
    destinationCoordinates: LatLng,
    items: List<ItineraryItem>,
    modifier: Modifier = Modifier,

    ) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(destinationCoordinates, 15f)
    }
    val boundsBuilder = LatLngBounds.builder()
    items.forEach { item ->
        boundsBuilder.include(LatLng(item.latitude, item.longitude))
    }
    val bounds = boundsBuilder.build()

    LaunchedEffect(bounds) {
        cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(bounds, 200), 1_000)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
    ) {
        items.forEach { item ->
            Marker(
                state = MarkerState(position = LatLng(item.latitude, item.longitude)),
                title = item.name,
            )
        }
    }
}

@Composable
fun DayItinerary(
    items: List<ItineraryItem>
) {
    Column {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(items) { index, item ->
                ItineraryItem(
                    isFirst = index == 0,
                    item = item
                )
            }
        }
    }
}

@Composable
fun ItineraryItem(
    isFirst: Boolean,
    item: ItineraryItem
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(IntrinsicSize.Min)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!isFirst) {
                SubtleVerticalDivider(
                    modifier = Modifier.height(8.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }
            ElevatedIcon(
                iconUrl = item.iconUrl.orEmpty(),
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp),
            )
            SubtleVerticalDivider(
                modifier = Modifier.fillMaxHeight()
            )
        }

        Column(
            modifier =
                Modifier.padding(
                    start = 16.dp,
                    end = 0.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
        ) {
            Text(
                text = stringResource(R.string.title_visit_time, item.getVisitTime()),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.65f),
                maxLines = 1,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            if (item.photos.isNotEmpty()) {
                PlacePhotos(
                    imageUrls = item.photos,
                    maxWidthPx = 200,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            SubtleHorizontalDivider()
        }
    }
}

private fun ItineraryItem.getVisitTime(): String {
    val duration = LocalTime.MIN.plus(
        Duration.ofMinutes(visitTimeMin.toLong())
    )
    var displayDuration = ""
    if (duration.hour > 0) {
        displayDuration += "${duration.hour}h "
    }
    if (duration.minute > 0) {
        displayDuration += "${duration.minute}m"
    }
    return displayDuration
}

@Preview(showBackground = true)
@Composable
fun ItineraryItemPreview() {
    DestinatorTheme {
        ItineraryItem(
            isFirst = true,
            item = ItineraryItem(
                name = "Sample Destination",
                description = "This is a sample description for a destination.",
                iconUrl = "https://example.com/icon.png",
                latitude = 0.0,
                longitude = 0.0,
                date = "2023-04-01",
                metadataSourceId = "123",
                order = 1,
                tripDay = 1,
                visitTimeMin = 60
            )
        )
    }
}
