package me.goldhardt.destinator.feature.trips.destinations.create

import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import me.goldhardt.destinator.core.designsystem.Tokens
import me.goldhardt.destinator.core.designsystem.components.NextStepButton
import me.goldhardt.destinator.core.designsystem.paddingTopBarAndStatusBar
import me.goldhardt.destinator.feature.trips.CREATE_DESTINATION
import me.goldhardt.destinator.feature.trips.CreateTripScreens
import me.goldhardt.destinator.feature.trips.R
import me.goldhardt.destinator.feature.trips.navigateToDestinationDetail


@Composable
fun SelectDestination(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val parentEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(CREATE_DESTINATION)
    }
    val viewModel = hiltViewModel<CreateDestinationViewModel>(parentEntry)

    var selectedCity by rememberSaveable { mutableStateOf("") }
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paddingTopBarAndStatusBar()
            .imePadding()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            CreateTripTitle(resourceId = R.string.title_trip_destination)
            TextField(
                value = selectedCity,
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                onValueChange = { selectedCity = it },
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
        }
        NextStepButton(
            enabled = selectedCity.isNotBlank(),
            modifier = Modifier.padding(36.dp).align(Alignment.BottomCenter)
        ) {
            viewModel.setCity(selectedCity)
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
        navController.getBackStackEntry(CREATE_DESTINATION)
    }
    val viewModel = hiltViewModel<CreateDestinationViewModel>(parentEntry)

    val state = rememberDateRangePickerState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .paddingTopBarAndStatusBar()
    ) {
        DateRangePicker(
            state = state,
            dateFormatter = DatePickerDefaults.dateFormatter(
                selectedDateSkeleton = "dd MMM",
            ),
            headline = {},
            title = {
                CreateTripTitle(resourceId = R.string.title_trip_dates)
            },
            showModeToggle = false,
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        )
        NextStepButton(
            enabled = state.selectedStartDateMillis != null && state.selectedEndDateMillis != null,
            modifier = Modifier.padding(36.dp)
        ) {
            viewModel.setDates(
                state.selectedStartDateMillis ?: 0,
                state.selectedEndDateMillis ?: 0
            )
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
        navController.getBackStackEntry(CREATE_DESTINATION)
    }
    val viewModel = hiltViewModel<CreateDestinationViewModel>(parentEntry)

    var selectedStyles by rememberSaveable { mutableStateOf(listOf<TripStyle>()) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .paddingTopBarAndStatusBar()
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
        Spacer(modifier = Modifier.weight(1f))
        NextStepButton(
            enabled = selectedStyles.isNotEmpty(),
            modifier = Modifier
                .padding(36.dp)
        ) {
            viewModel.setTripStyle(
                selectedStyles.map { it.name }
            )
            navController.navigate(CreateTripScreens.GENERATING_ITINERARY)
        }
    }
}

enum class PlaneAnimationState {
    Start, Finish
}

@Composable
fun GeneratingItinerary(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry
) {
    val parentEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(CREATE_DESTINATION)
    }
    val viewModel = hiltViewModel<CreateDestinationViewModel>(parentEntry)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is CreateDestinationUiState.Creating -> {
            viewModel.generate()
        }
        is CreateDestinationUiState.Failed -> {
            GenerateItineraryFailed(viewModel)
        }
        is CreateDestinationUiState.Generated -> {
            navController.navigateToDestinationDetail(state.destinationId , state.city )
        }
        is CreateDestinationUiState.Processing -> {
            ProcessingItineraryRequest()
        }
    }
}

@Composable
private fun GenerateItineraryFailed(viewModel: CreateDestinationViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paddingTopBarAndStatusBar(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.error_failed_generate_itinerary),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { viewModel.generate() }
        ) {
            Text(text = stringResource(R.string.action_try_again))
        }
    }
}

@Composable
fun ProcessingItineraryRequest() {
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp
    val width = configuration.screenWidthDp.dp

    var animationState by remember { mutableStateOf(PlaneAnimationState.Start) }

    // Plane 1
    val plane1: Dp by animateDpAsState(
        if (animationState == PlaneAnimationState.Start) {
            64.dp
        } else {
            -height - Tokens.TopBar.height - Tokens.StatusBar.height
        },
        keyframes {
            durationMillis = 10_000
        }, label = "plane1"
    )

    // Plane 2
    val plane2: Dp by animateDpAsState(
        if (animationState == PlaneAnimationState.Start) 0.dp else (-width - 64.dp),
        keyframes {
            durationMillis = 15_000
        }, label = "plane2"
    )

    // Plane 3
    val plane3: Dp by animateDpAsState(
        if (animationState == PlaneAnimationState.Start) 0.dp else (width + 64.dp),
        keyframes {
            durationMillis = 8_000
        }, label = "plane3"
    )

    // Text
    val infiniteTransition = rememberInfiniteTransition(label = "text")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    )  {
        Image(
            painter = painterResource(me.goldhardt.destinator.core.designsystem.R.drawable.ic_plane),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 48.dp)
                .size(32.dp)
                .absoluteOffset(y = plane1)
        )
        Image(
            painter = painterResource(me.goldhardt.destinator.core.designsystem.R.drawable.ic_plane),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(vertical = 48.dp)
                .size(32.dp)
                .absoluteOffset(x = plane2)
                .graphicsLayer(rotationZ = -90f)

        )
        Image(
            painter = painterResource(me.goldhardt.destinator.core.designsystem.R.drawable.ic_plane),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(vertical = 64.dp)
                .size(32.dp)
                .absoluteOffset(x = plane3)
                .graphicsLayer(rotationZ = 90f)

        )
        Text(
            text = stringResource(R.string.title_generating_itinerary).uppercase(),
            maxLines = 3,
            style = MaterialTheme.typography.titleLarge.copy(textMotion = TextMotion.Animated),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin.Center
                }
                .align(Alignment.Center)
        )
    }

    LaunchedEffect("airplanes") {
        animationState = PlaneAnimationState.Finish
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