package me.goldhardt.destinator.feature.trips.destinations.edit

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.goldhardt.destinator.core.designsystem.components.ErrorScreen
import me.goldhardt.destinator.core.designsystem.components.LoadingScreen
import me.goldhardt.destinator.core.designsystem.theme.DestinatorTheme
import me.goldhardt.destinator.data.extensions.formatDate
import me.goldhardt.destinator.data.model.itinerary.ItineraryDay
import me.goldhardt.destinator.data.model.itinerary.ItineraryItem
import me.goldhardt.destinator.feature.trips.R
import me.goldhardt.destinator.feature.trips.destinations.detail.getVisitTime

@Composable
fun EditDestinationScreens(
    viewModel: EditDestinationViewModel = hiltViewModel(),
    onAddPlaceClick: (ItineraryDay) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState) {
        is EditDestinationUiState.Loading -> {
            LoadingScreen(R.string.title_loading)
        }

        is EditDestinationUiState.Success -> {
            EditDestinationScreen(
                state = state,
                onMoveUpClick = viewModel::moveItineraryUp,
                onMoveDownClick = viewModel::moveItineraryDown,
                onAddPlaceClick = onAddPlaceClick
            )
        }

        is EditDestinationUiState.Failed -> {
            ErrorScreen(R.string.error_generic)
        }
    }
}

@Composable
internal fun EditDestinationScreen(
    state: EditDestinationUiState.Success,
    onMoveUpClick: (ItineraryDay, ItineraryItem) -> Unit,
    onMoveDownClick: (ItineraryDay, ItineraryItem) -> Unit,
    onAddPlaceClick: (ItineraryDay) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(WindowInsets.statusBars.only(WindowInsetsSides.Top).asPaddingValues())
            .background(color = MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.5f))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            state.itineraryDays.forEach { day ->
                item {
                    EditDestinationsHeader(day)
                }
                items(day.items) { item ->
                    ItineraryItem(
                        item = item,
                        onMoveUpClick = {
                            onMoveUpClick(day, item)
                        },
                        onMoveDownClick = {
                            onMoveDownClick(day, item)
                        }
                    )
                }
                item {
                    AddPlaceButton {
                        onAddPlaceClick(day)
                    }
                }
            }
        }
    }
}

@Composable
internal fun AddPlaceButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(start = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Text(
            text = stringResource(R.string.action_add_place),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
internal fun EditDestinationsHeader(itineraryDay: ItineraryDay) {
    val color = MaterialTheme.colorScheme.onBackground
    val circleSize = 12.dp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .size(32.dp)
        ) {
            Canvas(modifier = Modifier.size(circleSize)) {
                drawCircle(
                    color = color,
                    radius = size.minDimension / 2
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Day ${itineraryDay.day}",
                style = MaterialTheme.typography.titleMedium,
                color = color,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = formatDate(itineraryDay.date),
                style = MaterialTheme.typography.bodyMedium,
                color = color,
            )
        }
    }
}


@Composable
internal fun ItineraryItem(
    item: ItineraryItem,
    onMoveUpClick: () -> Unit,
    onMoveDownClick: () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .clickable(onClick = {
                item.mapProviderUri?.let {
                    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                    context.startActivity(mapIntent)
                }
            })
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(32.dp)
        ) {
            VerticalDivider(
                thickness = 2.dp,
                modifier = Modifier.fillMaxHeight(),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .padding(end = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.title_visit_time, item.getVisitTime()),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.65f),
                        maxLines = 1,
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
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Icon(
                        Icons.Rounded.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.cd_move_up),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = false, radius = 24.dp),
                                onClick = { onMoveUpClick() }
                            ),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Icon(
                        Icons.Rounded.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.cd_move_down),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = false, radius = 24.dp),
                                onClick = { onMoveDownClick() }
                            ),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditDestinationScreenPreview() {
    DestinatorTheme {
        EditDestinationScreen(
            onMoveDownClick = { _, _ -> },
            onMoveUpClick = { _, _ -> },
            onAddPlaceClick = {},
            state = EditDestinationUiState.Success(
                itineraryDays = listOf(
                    ItineraryDay(
                        id = 0,
                        day = 0,
                        date = "2023-04-02",
                        items = listOf(
                            ItineraryItem(
                                id = 0,
                                name = "Sample Destination",
                                description = "This is a sample description for a destination.",
                                iconUrl = "https://example.com/icon.png",
                                latitude = 0.0,
                                longitude = 0.0,
                                metadataSourceId = "123",
                                order = 0,
                                visitTimeMin = 60
                            ),
                            ItineraryItem(
                                id = 1,
                                name = "Sample Destination",
                                description = "This is a sample description for a destination.",
                                iconUrl = "https://example.com/icon.png",
                                latitude = 0.0,
                                longitude = 0.0,
                                metadataSourceId = "123",
                                order = 1,
                                visitTimeMin = 60
                            )
                        )
                    ),
                    ItineraryDay(
                        id = 1,
                        day = 1,
                        date = "2023-04-03",
                        items = listOf(
                            ItineraryItem(
                                id = 0,
                                name = "Sample Destination",
                                description = "This is a sample description for a destination.",
                                iconUrl = "https://example.com/icon.png",
                                latitude = 0.0,
                                longitude = 0.0,
                                metadataSourceId = "123",
                                order = 0,
                                visitTimeMin = 60
                            ),
                            ItineraryItem(
                                id = 1,
                                name = "Sample Destination",
                                description = "This is a sample description for a destination.",
                                iconUrl = "https://example.com/icon.png",
                                latitude = 0.0,
                                longitude = 0.0,
                                metadataSourceId = "123",
                                order = 1,
                                visitTimeMin = 60
                            )
                        )
                    )
                )
            )
        )
    }
}
