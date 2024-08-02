package me.goldhardt.destinator.feature.trips.destinations.create

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import me.goldhardt.destinator.core.designsystem.paddingTopBarAndStatusBar
import me.goldhardt.destinator.core.designsystem.theme.DestinatorTheme
import me.goldhardt.destinator.feature.trips.CREATE_DESTINATION
import me.goldhardt.destinator.feature.trips.CreateTripScreens
import me.goldhardt.destinator.feature.trips.R

@Composable
fun ValidateDestination(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry,
) {
    val parentEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(CREATE_DESTINATION)
    }
    val viewModel = hiltViewModel<CreateDestinationViewModel>(parentEntry)
    val validationState by viewModel.destinationValidationState.collectAsStateWithLifecycle()

    val navigateToSelectDates = {
        navController.navigate(CreateTripScreens.SELECT_DATES) {
            popUpTo(CreateTripScreens.SELECT_DESTINATION) {
                inclusive = false
            }
        }
    }

    when (val state = validationState) {
        is DestinationValidationState.Loading -> Validating()
        is DestinationValidationState.Success -> navigateToSelectDates()
        is DestinationValidationState.SuccessMultiple -> SuccessMultipleScreen(state.options) {
            viewModel.setCity(it)
            navigateToSelectDates()
        }
        is DestinationValidationState.Invalid -> Invalid(errorMessage = state.errorMessage) {
            navController.popBackStack()
        }
    }
}

@Composable
private fun Validating() {
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
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Searching...".uppercase(),
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
}

@Composable
private fun SuccessMultipleScreen(
    options: List<String>,
    onOptionClicked: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .paddingTopBarAndStatusBar(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.title_multiple_destinations_found),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.title_select_one_destination),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        items(options) { option ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable(onClick = { onOptionClicked(option) }),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceBright)
                        .padding(24.dp),
                ) {
                    Text(
                        text = option,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSuccessMultipleScreen() {
    DestinatorTheme {
        val sampleOptions = listOf(
            "New York, NY, USA",
            "New York, PA, USA",
            "New York, OH, USA",
            "New York, NJ, USA",
            "New York, CT, USA",
        )
        SuccessMultipleScreen(options = sampleOptions, onOptionClicked = {})
    }
}

@Composable
private fun Invalid(
    errorMessage: Int,
    onActionClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(errorMessage),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onActionClicked) {
            Text(
                text = stringResource(R.string.action_try_again),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}