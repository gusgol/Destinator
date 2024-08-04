@file:OptIn(ExperimentalFoundationApi::class)

package me.goldhardt.destinator.feature.trips.destinations.list

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.goldhardt.destinator.core.designsystem.Tokens
import me.goldhardt.destinator.core.designsystem.components.PlacePhoto
import me.goldhardt.destinator.core.designsystem.components.StaticMapImage
import me.goldhardt.destinator.core.designsystem.components.SubtleHorizontalDivider
import me.goldhardt.destinator.data.extensions.formatDates
import me.goldhardt.destinator.data.model.destination.Destination
import me.goldhardt.destinator.data.model.destination.DestinationStatus
import me.goldhardt.destinator.feature.trips.R
import kotlin.math.absoluteValue

@Composable
fun DestinationsRoute(
    destinationsViewModel: DestinationsViewModel = hiltViewModel(),
    onDestinationClick: (Destination) -> Unit,
    onCreateTripClick: () -> Unit,
) {
    val uiState by destinationsViewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(modifier = Modifier.weight(1f)) {
            DestinationsList(uiState, onDestinationClick)
        }
        ExtendedFloatingActionButton(
            text = {
                Text(
                    text = stringResource(R.string.action_create_itinerary),
                    style = MaterialTheme.typography.titleMedium,
                )
            },
            onClick = onCreateTripClick,
            icon = {
                Icon(
                    painter = painterResource(me.goldhardt.destinator.core.designsystem.R.drawable.ic_travel),
                    contentDescription = stringResource(R.string.action_create_itinerary)
                )
            },
            contentColor = MaterialTheme.colorScheme.surface,
            containerColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        )
    }
}

@Composable
fun DestinationsList(
    uiState: DestinationsUiState,
    onClick: (Destination) -> Unit
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
            EmptyDestinations()
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
    onClick: (Destination) -> Unit
) {
    val isPortrait = LocalConfiguration.current.orientation ==
            Configuration.ORIENTATION_PORTRAIT
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = {
        destinations.size
    })
    var selectedTab by rememberSaveable { mutableStateOf(destinations.first().status) }

    Column {
        Spacer(modifier = Modifier.height(Tokens.TopBar.height))
        DestinationsStatusTabs(
            destinations = destinations,
            selectedTab = selectedTab,
            onTabSelected = { status ->
                selectedTab = status
                val firstItem = destinations.indexOf(
                    destinations.first { it.status == selectedTab }
                )
                coroutineScope.launch {
                    if (isPortrait) {
                        pagerState.animateScrollToPage(firstItem)
                    }
                }
            }
        )
        if (isPortrait) {
            Spacer(modifier = Modifier.weight(1f))
            DestinationsPager(
                pagerState = pagerState,
                destinations = destinations,
                selectTab = {
                    selectedTab = it
                },
                onClick = onClick
            )
            Spacer(modifier = Modifier.weight(0.8f))
        } else {
            DestinationsRow(
                destinations = destinations.filter {
                    it.status == selectedTab
                },
                onClick = onClick
            )
        }
    }
}

@Composable
private fun DestinationsPager(
    pagerState: PagerState,
    destinations: List<Destination>,
    selectTab: (DestinationStatus) -> Unit,
    onClick: (Destination) -> Unit
) {
    HorizontalPager(
        state = pagerState,
        pageSpacing = 16.dp,
        contentPadding = PaddingValues(horizontal = 32.dp),
    ) { page ->
        val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
            .absoluteValue

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                destinations.getOrNull(page)?.let { destination ->
                    selectTab(destination.status)
                }
            }
        }

        DestinationListItem(
            destination = destinations[page],
            pageOffset = pageOffset,
            onClick = onClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationsStatusTabs(
    destinations: List<Destination>,
    selectedTab: DestinationStatus,
    onTabSelected: (DestinationStatus) -> Unit
) {
    SecondaryScrollableTabRow(
        selectedTabIndex = selectedTab.ordinal,
        divider = {
            SubtleHorizontalDivider()
        },
        indicator = {
        },
    ) {
        val availableStatuses = destinations.map { it.status }.distinct()
        DestinationStatus.entries.forEachIndexed { index, value ->
            val isSelected = value == selectedTab
            val fontSize by animateFloatAsState(targetValue = if (isSelected) 24f else 16f,
                label = "tabText"
            )
            Tab(
                selected = isSelected,
                onClick = {
                    onTabSelected(value)
                },
                enabled = value in availableStatuses,
                selectedContentColor = MaterialTheme.colorScheme.outline.copy(0.05f),
                text = {
                    Column(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
                    ) {
                        Text(
                            text = stringResource(value.displayName),
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            fontSize = fontSize.sp,
                            style = if (isSelected) {
                                MaterialTheme.typography.titleMedium.copy(
                                    brush = Brush.linearGradient(
                                        colors = Tokens.Gradient.colors
                                    )
                                )
                            } else {
                                if (value in availableStatuses) {
                                    MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                } else {
                                    MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                    )
                                }
                            }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun DestinationsRow(
    destinations: List<Destination>,
    onClick: (Destination) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(destinations) { destination ->
            DestinationRowItem(
                destination = destination,
                onClick = onClick
            )
        }
    }
}

@Composable
fun DestinationRowItem(
    destination: Destination,
    onClick: (Destination) -> Unit
) {
    val completedColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .width(400.dp),
        onClick = {
            onClick(destination)
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceBright),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PlacePhoto(
                imageUrl = destination.thumbnail,
                maxWidthPx = 600,
                modifier = if (destination.status == DestinationStatus.COMPLETED) {
                    Modifier.drawWithContent {
                        drawContent()
                        drawRect(color = completedColor)
                    }
                } else {
                    Modifier
                }
                    .fillMaxHeight()
                    .weight(1f)
            )
            DestinationDetail(
                destination = destination,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(2f),
            )
        }
    }
}

@Composable
fun DestinationListItem(
    destination: Destination,
    pageOffset: Float,
    onClick: (Destination) -> Unit
) {
    val completedColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleY = lerp(
                    start = 0.8f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
            },
        onClick = { onClick(destination) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
    ) {
        Column {
            PlacePhoto(
                imageUrl = destination.thumbnail,
                maxWidthPx = 600,
                modifier = if (destination.status == DestinationStatus.COMPLETED) {
                    Modifier.drawWithContent {
                        drawContent()
                        drawRect(color = completedColor)
                    }
                } else {
                    Modifier
                }
                    .fillMaxWidth()
                    .height(400.dp)
                    .graphicsLayer {
                        scaleX = lerp(
                            start = 1f,
                            stop = 1.2f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                        scaleY = lerp(
                            start = 1f,
                            stop = 1.2f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceBright),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DestinationDetail(
                    destination = destination,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1.2f)
                )
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp, bottom = 16.dp, start = 8.dp)
                        .height(100.dp)
                        .weight(1f)
                ) {
                    StaticMapImage(
                        latitude = destination.latitude,
                        longitude = destination.longitude,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .fillMaxSize(),
                        contentDescription = stringResource(
                            R.string.cd_map_of,
                            destination.city,
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun DestinationDetail(
    destination: Destination,
    modifier: Modifier
) {
    Column(modifier) {
        Text(
            text = "${destination.city}, ${destination.country}",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
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
}

@Composable
fun EmptyDestinations() {
    val heading = buildAnnotatedString {
        append(stringResource(R.string.title_empty_heading_1))
        append(" ")
        withStyle(
            style = SpanStyle(brush = Brush.linearGradient(
                colors = Tokens.Gradient.colors
            ))
        ) {
            append(stringResource(R.string.title_empty_heading_2))
        }
        append("!")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(32.dp)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_globe_plane),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Text(
            text = heading,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.title_empty_subheading),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
        )
    }
}