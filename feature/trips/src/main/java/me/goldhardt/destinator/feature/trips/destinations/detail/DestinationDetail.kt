package me.goldhardt.destinator.feature.trips.destinations.detail

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
            indicator = { tabPositions ->
//                Box(
//                    Modifier
//                        .tabIndicatorOffset(tabPositions[selectedTab])
//                        .padding(5.dp)
//                        .fillMaxSize()
//                        .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface), RoundedCornerShape(5.dp))
//                )
            }
        ) {
            uiState.calendar.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                )
            }
        }
        Text(
            text = "Text tab ${selectedTab + 1} selected",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}
