package me.goldhardt.destinator.feature.trips.destinations.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
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
import me.goldhardt.destinator.data.model.itinerary.ItineraryItem
import me.goldhardt.destinator.feature.trips.R

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
    var selectedItems = uiState.destination.itinerary.filter {
        it.tripDay == uiState.calendar[selectedTab]
    }
    Column {
        DestinationMap(
            LatLng(uiState.destination.latitude, uiState.destination.longitude),
            selectedItems,
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.div(3).dp)
        )
        SecondaryScrollableTabRow(
            selectedTabIndex = selectedTab,
            divider = {
            },
//            indicator = { tabPositions ->
//                Box(
//                    Modifier
//                        .tabIndicatorOffset(tabPositions[selectedTab])
//                        .padding(5.dp)
//                        .fillMaxSize()
//                        .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface), RoundedCornerShape(5.dp))
//                )
//            }
        ) {
            uiState.calendar.forEachIndexed { index, tripDay ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = stringResource(R.string.title_trip_day, tripDay),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
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
        Text(
            text = items.map { it.date }.distinct().joinToString(),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(items) { item ->
                ItineraryItem(item = item)
            }
        }
    }
}

@Composable
fun ItineraryItem(
    item: ItineraryItem
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = item.name,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = item.description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}
