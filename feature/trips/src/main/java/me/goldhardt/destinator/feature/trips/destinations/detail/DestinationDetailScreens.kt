package me.goldhardt.destinator.feature.trips.destinations.detail

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import me.goldhardt.destinator.core.common.LocalMenuItemState
import me.goldhardt.destinator.core.common.MenuItem
import me.goldhardt.destinator.core.common.MenuItemsState
import me.goldhardt.destinator.core.designsystem.Tokens
import me.goldhardt.destinator.core.designsystem.components.ElevatedIcon
import me.goldhardt.destinator.core.designsystem.components.ErrorScreen
import me.goldhardt.destinator.core.designsystem.components.LoadingScreen
import me.goldhardt.destinator.core.designsystem.components.PlacePhotos
import me.goldhardt.destinator.core.designsystem.components.SubtleHorizontalDivider
import me.goldhardt.destinator.core.designsystem.components.SubtleVerticalDivider
import me.goldhardt.destinator.core.designsystem.theme.DestinatorTheme
import me.goldhardt.destinator.data.extensions.formatDate
import me.goldhardt.destinator.data.model.destination.Destination
import me.goldhardt.destinator.data.model.itinerary.ItineraryDay
import me.goldhardt.destinator.data.model.itinerary.ItineraryItem
import me.goldhardt.destinator.data.model.places.InterestPlace
import me.goldhardt.destinator.data.model.places.PlaceType
import me.goldhardt.destinator.feature.trips.DESTINATION_DETAIL_ROUTE
import me.goldhardt.destinator.feature.trips.R
import java.time.Duration
import java.time.LocalTime

@Composable
fun DestinationDetail(
    destinationDetailViewModel: DestinationDetailViewModel = hiltViewModel(),
    onEditClick: (Destination) -> Unit,
    onAddClick: (Destination, PlaceType) -> Unit,
) {
    val uiState by destinationDetailViewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState) {
        DestinationDetailUiState.Failed -> {
            ErrorScreen(errorMessage = R.string.error_failed_loading_destination)
        }

        DestinationDetailUiState.Loading -> {
            LoadingScreen(message = R.string.title_loading)
        }

        is DestinationDetailUiState.Success -> {
            DestinationDetail(
                uiState = state,
                onEditClick = onEditClick,
                onAddClick = onAddClick,
            )
        }
    }
}

@Composable
fun DestinationDetail(
    uiState: DestinationDetailUiState.Success,
    onEditClick: (Destination) -> Unit,
    onAddClick: (Destination, PlaceType) -> Unit,
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var tabType by rememberSaveable { mutableIntStateOf(DestinationTab.ITINERARY) }

    /**
     * If selected tab is not an itinerary day (Dining, Shopping), list should be empty
     */
    val selectedItems = uiState.destination.itineraryDays.getOrNull(selectedTab)?.items.orEmpty()
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    var isFullscreen by rememberSaveable { mutableStateOf(false) }
    val mapSize: Float by animateFloatAsState(if (isFullscreen) 1f else 2.4f, label = "mapSize")
    val menu = LocalMenuItemState.current

    ConfigureMenuItems(menu, isFullscreen) {
        isFullscreen = it
    }

    val mapModifier = if (isPortrait) {
        Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.div(mapSize).dp)
    } else {
        Modifier
            .fillMaxHeight()
            .width(LocalConfiguration.current.screenWidthDp.div(mapSize).dp)
    }

    val mapItems = when (tabType) {
        DestinationTab.ITINERARY -> selectedItems.map { MapPlace.from(it) }

        DestinationTab.DINING -> uiState.destination.interestPlaces
            .filter { it.type == PlaceType.Dining }
            .map { MapPlace.from(it) }

        DestinationTab.SHOPPING -> uiState.destination.interestPlaces
            .filter { it.type == PlaceType.Shop }
            .map { MapPlace.from(it) }

        else -> {
            emptyList()
        }
    }

    DetailLayout(isPortrait) {
        DestinationMap(
            LatLng(uiState.destination.latitude, uiState.destination.longitude),
            mapItems,
            modifier = mapModifier
        )
        Column {
            if (!isPortrait) {
                Spacer(modifier = Modifier.height(Tokens.TopBar.height))
            }
            ItineraryTabs(
                uiState = uiState,
                selectedTab = selectedTab,
                onTabSelected = { index, type ->
                    selectedTab = index
                    tabType = type.type
                },
                onEditClick = onEditClick
            )
            when (tabType) {
                DestinationTab.ITINERARY -> DayItinerary(
                    items = selectedItems
                )

                DestinationTab.DINING -> InterestPlacesContent(
                    places = uiState.destination.interestPlaces.filter {
                        it.type == PlaceType.Dining
                    },
                    onAddClick = {
                        onAddClick(uiState.destination, PlaceType.Dining)
                    }
                )

                DestinationTab.SHOPPING -> InterestPlacesContent(
                    places = uiState.destination.interestPlaces.filter {
                        it.type == PlaceType.Shop
                    },
                    onAddClick = {
                        onAddClick(uiState.destination, PlaceType.Shop)
                    }
                )
            }
        }
    }
}

@Composable
fun InterestPlacesContent(
    places: List<InterestPlace>,
    onAddClick: () -> Unit = {}
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (places.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    stringResource(R.string.title_you_haven_t_added_any_places_yet),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn {
                items(places) { place ->
                    Row(
                        modifier = Modifier.clickable {
                            place.mapProviderUri?.let {
                                openGoogleMaps(context, it)
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            ElevatedIcon(
                                iconUrl = place.iconUrl.orEmpty(),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(20.dp),
                            )
                        }
                        val description = if (place.description.isNotBlank()) {
                            "${place.description}\n${place.address}"
                        } else {
                            place.address
                        }
                        ListItem(
                            title = place.name,
                            subtitle = description,
                            photosUrls = emptyList(), //TODO Right now the query does not know how to retrieve the photo for the InterestPlace (instead of Place, since both can have the same ids).
                        )
                    }
                }
            }
        }
        FilledTonalButton(
            onClick = {
                onAddClick()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                stringResource(R.string.action_add_place),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

private val EXIT_FULLSCREEN_ICON
    get() = me.goldhardt.destinator.core.designsystem.R.drawable.ic_fullscreen_exit

private val ENTER_FULLSCREEN_ICON
    get() = me.goldhardt.destinator.core.designsystem.R.drawable.ic_fullscreen

@Composable
private fun ConfigureMenuItems(
    menu: MenuItemsState,
    isFullscreen: Boolean,
    setFullscreen: (Boolean) -> Unit,
) {
    LaunchedEffect(isFullscreen) {
        val menuItems = mutableListOf(
            if (isFullscreen) {
                MenuItem("exit_fs", R.string.action_exit_fullscreen, EXIT_FULLSCREEN_ICON) {
                    setFullscreen(false)
                }
            } else {
                MenuItem("enter_fs", R.string.action_enter_fullscreen, ENTER_FULLSCREEN_ICON) {
                    setFullscreen(true)
                }
            }
        )
        menu.setMenuItems(DESTINATION_DETAIL_ROUTE, menuItems)
    }
}

@Composable
fun DetailLayout(
    isPortrait: Boolean = true,
    content: @Composable () -> Unit
) {
    if (isPortrait) {
        Column {
            content()
        }
    } else {
        Row {
            content()
        }
    }
}


internal sealed interface DestinationTab {
    companion object {
        const val ITINERARY = 0
        const val DINING = 1
        const val SHOPPING = 2
    }

    val content: @Composable () -> Unit
    val type: Int

    data class Itinerary(
        val itineraryDay: ItineraryDay
    ) : DestinationTab {
        override val content: @Composable () -> Unit = {
            ItineraryDayTab(itineraryDay)
        }
        override val type: Int = ITINERARY
    }

    data object Dining : DestinationTab {
        override val content: @Composable () -> Unit = {
            DiningTab()
        }
        override val type: Int = DINING
    }

    data object Shopping : DestinationTab {
        override val content: @Composable () -> Unit = {
            ShopTab()
        }
        override val type: Int = SHOPPING
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ItineraryTabs(
    uiState: DestinationDetailUiState.Success,
    selectedTab: Int,
    onTabSelected: (Int, DestinationTab) -> Unit,
    onEditClick: (Destination) -> Unit
) {
    SecondaryScrollableTabRow(
        selectedTabIndex = selectedTab,
        divider = {
        },
        indicator = {
        }
    ) {
        val tabs = uiState.destination.itineraryDays.map { DestinationTab.Itinerary(it) } + listOf(
            DestinationTab.Dining,
            DestinationTab.Shopping
        )
        tabs.forEachIndexed { index, tab ->
            val isSelected = selectedTab == index
            val tabBackgroundColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                if (tab.type == DestinationTab.ITINERARY) {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                } else {
                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                }
            }
            Tab(
                selected = isSelected,
                onClick = { onTabSelected(index, tab) },
                selectedContentColor = MaterialTheme.colorScheme.surface,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .background(color = tabBackgroundColor, shape = RoundedCornerShape(4.dp))
                    .width(120.dp)
                    .height(80.dp),
                text = {
                    tab.content()
                }
            )
        }
        Tab(
            selected = false,
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .background(
                    color = MaterialTheme.colorScheme.background,
                )
                .width(80.dp)
                .height(80.dp),
            onClick = {
                onEditClick(uiState.destination)
            },
            text = {
                EditTab()
            }
        )
    }
}

@Composable
private fun ItineraryTab(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            text = title,
            maxLines = 1,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = subtitle,
            maxLines = 1,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun ItineraryDayTab(
    itineraryDay: ItineraryDay
) {
    ItineraryTab(
        stringResource(R.string.title_trip_day, itineraryDay.day),
        formatDate(itineraryDay.date)
    )
}

@Composable
private fun EditTab() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = stringResource(R.string.cd_edit_destination_and_itinerary),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = stringResource(R.string.action_edit),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun DiningTab() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_dining),
            contentDescription = stringResource(R.string.title_dining),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = stringResource(R.string.title_dining),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun ShopTab() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = stringResource(R.string.title_shopping),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = stringResource(R.string.title_shopping),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private data class MapPlace(
    val latitude: Double,
    val longitude: Double,
    val name: String,
) {
    companion object {
        fun from(interestPlace: InterestPlace): MapPlace {
            return MapPlace(
                latitude = interestPlace.latitude,
                longitude = interestPlace.longitude,
                name = interestPlace.name
            )
        }

        fun from(itineraryItem: ItineraryItem): MapPlace {
            return MapPlace(
                latitude = itineraryItem.latitude,
                longitude = itineraryItem.longitude,
                name = itineraryItem.name
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun DestinationMap(
    destinationCoordinates: LatLng,
    items: List<MapPlace>,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(destinationCoordinates, 15f)
    }

    val bounds = LatLngBounds.builder().apply {
        if (items.isEmpty()) {
            include(destinationCoordinates)
        } else {
            items.forEach { item -> include(LatLng(item.latitude, item.longitude)) }
    }
    }.build()

    LaunchedEffect(bounds) {
        val cameraUpdate = if (items.isEmpty()) {
            CameraUpdateFactory.newLatLngZoom(bounds.center, 13f)
        } else {
            CameraUpdateFactory.newLatLngBounds(bounds, 200)
        }
        cameraPositionState.animate(cameraUpdate, 1_000)
    }

    var isMyLocationEnabled by rememberSaveable { mutableStateOf(false) }

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (locationPermissions.allPermissionsGranted) {
        isMyLocationEnabled = true
    } else {
        LaunchedEffect(Unit) {
            locationPermissions.launchMultiplePermissionRequest()
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = isMyLocationEnabled),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = false,
        )
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
internal fun ItineraryItem(
    isFirst: Boolean,
    item: ItineraryItem
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(IntrinsicSize.Min)
            .clickable(onClick = {
                item.mapProviderUri?.let {
                    openGoogleMaps(context, it)
                }
            })
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
        ListItem(
            title = item.name,
            subtitle = item.description,
            label = stringResource(R.string.title_visit_time, item.getVisitTime()),
            photosUrls = item.photos,
        )
    }
}

@Composable
internal fun ListItem(
    title: String,
    subtitle: String,
    label: String? = null,
    displayDivider: Boolean = true,
    photosUrls: List<String>,
) {
    Column(
        modifier =
        Modifier.padding(
            start = 16.dp,
            end = 0.dp,
            top = 8.dp,
            bottom = 8.dp
        )
    ) {
        label?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.65f),
                maxLines = 1,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        if (photosUrls.isNotEmpty()) {
            PlacePhotos(
                photosReferences = photosUrls,
                maxWidthPx = 200,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (displayDivider) {
            Spacer(modifier = Modifier.height(16.dp))
            SubtleHorizontalDivider()
        }
    }
}

/**
 * Get the visit time in a human-readable format.
 *
 * TODO move this to a different file
 */
fun ItineraryItem.getVisitTime(): String {
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

private fun openGoogleMaps(context: Context, placeId: String) {
    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(placeId))
    context.startActivity(mapIntent)
}

@Preview(showBackground = true)
@Composable
fun ItineraryItemPreview() {
    DestinatorTheme {
        ItineraryItem(
            isFirst = true,
            item = ItineraryItem(
                id = 0,
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
    }
}

@Preview(showBackground = true)
@Composable
fun DiningContentPreview() {
    DestinatorTheme {
        InterestPlacesContent(
            places = listOf(
                InterestPlace(
                    id = 0,
                    destinationId = 0,
                    name = "Sample Place",
                    description = "This is a sample description for a place.",
                    longitude = 0.0,
                    latitude = 0.0,
                    address = "123 Sample St, Sample City",
                    type = PlaceType.Dining,
                    iconUrl = "https://example.com/icon.png",
                    metadataSourceId = "123"
                ),
                InterestPlace(
                    id = 1,
                    destinationId = 0,
                    name = "Another Place",
                    description = "This is another sample description for a place.",
                    longitude = 0.0,
                    latitude = 0.0,
                    address = "456 Another St, Another City",
                    type = PlaceType.Dining,
                    iconUrl = "https://example.com/icon.png",
                    metadataSourceId = "456"
                )
            )
        )
    }
}
