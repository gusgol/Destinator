package me.goldhardt.destinator.feature.trips.destinations.create

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import me.goldhardt.destinator.feature.trips.CREATE_TRIP_ROUTE
import me.goldhardt.destinator.feature.trips.CreateTripScreens
import me.goldhardt.destinator.feature.trips.R


@Composable
fun SelectDestination(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val parentEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(CREATE_TRIP_ROUTE)
    }
    val viewModel = hiltViewModel<CreateDestinationViewModel>(parentEntry)
    var text by rememberSaveable { mutableStateOf("") }
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Spacer(modifier = Modifier.height(120.dp))
        CreateTripTitle(resourceId = R.string.title_trip_destination)
        TextField(
            value = text,
            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
            onValueChange = { text = it },
            placeholder = {
                Text(
                    stringResource(R.string.hint_enter_destination),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = backgroundColor,
                focusedContainerColor = backgroundColor,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        NextStepButton {
            viewModel.city = text
            navController.navigate(CreateTripScreens.SELECT_DATES)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDates(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val parentEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(CREATE_TRIP_ROUTE)
    }
    val viewModel = hiltViewModel<CreateDestinationViewModel>(parentEntry)
    val state = rememberDateRangePickerState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        DateRangePicker(
            state = state,
            dateFormatter = DatePickerDefaults.dateFormatter(
                selectedDateSkeleton = "dd MMM",
            ),
            headline = {},
            title = {
                CreateTripTitle(resourceId = R.string.title_trip_destination)
            },
            showModeToggle = false,
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        )
        NextStepButton {
            viewModel.fromMs = state.selectedStartDateMillis ?: 0
            viewModel.toMs = state.selectedEndDateMillis ?: 0
            navController.navigate(CreateTripScreens.SELECT_TRIP_STYLE)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectTripStyle(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val parentEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(CREATE_TRIP_ROUTE)
    }
    val viewModel = hiltViewModel<CreateDestinationViewModel>(parentEntry)
    var selectedStyles by rememberSaveable { mutableStateOf(listOf<TripStyle>()) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(120.dp))
        CreateTripTitle(resourceId = R.string.title_select_trip_style)
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.padding(vertical = 32.dp)
        ) {
            TripStyle.entries.forEach {
                TripStyleChip(
                    tripStyle = it,
                    isSelected = selectedStyles.contains(it),
                    onSelectionChanged = { isSelected ->
                        selectedStyles = if (isSelected) {
                            selectedStyles + it
                        } else {
                            selectedStyles - it
                        }
                        Log.e("SelectedStyles", selectedStyles.toString())
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
        NextStepButton {
            viewModel.tripStyle = selectedStyles.map { it.name }
            viewModel.generate()
        }
    }
}

@Composable
fun CreateTripTitle(
    resourceId: Int
) {
    Text(
        text = stringResource(resourceId),
        style = MaterialTheme.typography.headlineSmall,
    )
}

@Composable
fun TripStyleChip(
    tripStyle: TripStyle,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onSelectionChanged: (Boolean) -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = { onSelectionChanged(!isSelected) },
        label = { Text(text = stringResource(id = tripStyle.displayName)) },
        modifier = modifier
    )
}

enum class TripStyle(val displayName: Int) {
    MustSee(R.string.trip_style_must_see),
    NatureAndOutdoors(R.string.trip_style_nature_and_outdoors),
    Shopping(R.string.trip_style_shopping),
    Nightlife(R.string.trip_style_nightlife),
    Sports(R.string.trip_style_sports),
    FamilyFriendly(R.string.trip_style_family_friendly),
    CulturalExperiences(R.string.trip_style_cultural_experiences),
    PhotographySpots(R.string.trip_style_photography_spots),
    RomanticGetaways(R.string.trip_style_romantic_getaways)
}

/**
 * TODO Extract to DS
 */
@Composable
private fun NextStepButton(
    onNextClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onNextClick,
        Modifier.padding(48.dp)
    ) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            stringResource(R.string.cd_select_destination)
        )
    }
}