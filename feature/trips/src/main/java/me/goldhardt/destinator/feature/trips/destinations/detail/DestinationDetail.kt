package me.goldhardt.destinator.feature.trips.destinations.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.goldhardt.destinator.data.extensions.toDayMonth
import me.goldhardt.destinator.data.model.itinerary.ItineraryItem

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
    Column {
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
            uiState.calendar.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                )
            }
        }
        DayItinerary(
            items = uiState.destination.itinerary.filter {
                it.date.toDayMonth() == uiState.calendar[selectedTab]
            }
        )
    }
}

@Composable
fun DayItinerary(
    items: List<ItineraryItem>
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items) { item ->
            ItineraryItem(item = item)
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
