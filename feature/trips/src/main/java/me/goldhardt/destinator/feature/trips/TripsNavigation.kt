@file:Suppress("KDocUnresolvedReference")

package me.goldhardt.destinator.feature.trips

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import me.goldhardt.destinator.data.extensions.formatDate
import me.goldhardt.destinator.data.model.places.PlaceType
import me.goldhardt.destinator.feature.trips.CreateTripScreens.GENERATING_ITINERARY
import me.goldhardt.destinator.feature.trips.CreateTripScreens.SELECT_DATES
import me.goldhardt.destinator.feature.trips.CreateTripScreens.SELECT_DESTINATION
import me.goldhardt.destinator.feature.trips.CreateTripScreens.SELECT_TRIP_STYLE
import me.goldhardt.destinator.feature.trips.CreateTripScreens.VALIDATE_DESTINATION
import me.goldhardt.destinator.feature.trips.destinations.addplace.AddInterestPlace
import me.goldhardt.destinator.feature.trips.destinations.addplace.AddPlace
import me.goldhardt.destinator.feature.trips.destinations.create.GeneratingItinerary
import me.goldhardt.destinator.feature.trips.destinations.create.SelectDates
import me.goldhardt.destinator.feature.trips.destinations.create.SelectDestination
import me.goldhardt.destinator.feature.trips.destinations.create.SelectTripStyle
import me.goldhardt.destinator.feature.trips.destinations.create.ValidateDestination
import me.goldhardt.destinator.feature.trips.destinations.detail.DestinationDetail
import me.goldhardt.destinator.feature.trips.destinations.edit.EditDestinationScreens
import me.goldhardt.destinator.feature.trips.destinations.list.DestinationsRoute

/**
 * Used to display the destination title on the top bar title.
 * @see DestinatorAppState
 */
const val TITLE = "title"

/**
 * Used to display the type of the place to add.
 * @see AddPlace
 */
const val TYPE = "type"

const val DESTINATIONS_ROUTE = "trips"
const val CREATE_DESTINATION = "$DESTINATIONS_ROUTE/create"
const val DESTINATION_ID = "destinationId"
const val ITINERARY_DAY_ID = "itineraryDayId"
const val DESTINATION_DETAIL = "$DESTINATIONS_ROUTE/detail"
const val DESTINATION_DETAIL_ROUTE = "$DESTINATION_DETAIL/{$DESTINATION_ID}?$TITLE={$TITLE}"
const val DESTINATION_EDIT = "$DESTINATIONS_ROUTE/edit"
const val DESTINATION_EDIT_ROUTE = "$DESTINATION_EDIT/{$DESTINATION_ID}?$TITLE={$TITLE}"
const val DESTINATION_ADD_PLACE_ROUTE =
    "$DESTINATION_EDIT/{$DESTINATION_ID}/addPlace/{$ITINERARY_DAY_ID}?$TITLE={$TITLE}"
const val DESTINATION_ADD_INTEREST_PLACE_ROUTE =
    "$DESTINATION_EDIT/{$DESTINATION_ID}/addInterestPlace?$TYPE={$TYPE}&$TITLE={$TITLE}"

/**
 * Nested navigation for create trip
 */
object CreateTripScreens {
    const val SELECT_DESTINATION = "$CREATE_DESTINATION/select_destination"
    const val VALIDATE_DESTINATION = "$CREATE_DESTINATION/validate_destination"
    const val SELECT_DATES = "$CREATE_DESTINATION/select_dates"
    const val SELECT_TRIP_STYLE = "$CREATE_DESTINATION/select_trip_style"
    const val GENERATING_ITINERARY = "$CREATE_DESTINATION/generating_itinerary"
}

fun NavGraphBuilder.tripsScreens(
    navController: NavHostController
) {
    composable(
        route = DESTINATIONS_ROUTE
    ) {
        DestinationsRoute(
            onDestinationClick = { destination ->
                navController.navigateToDestinationDetail(destination.id, destination.city)
            },
            onCreateTripClick = {
                navController.navigate(CREATE_DESTINATION)
            }
        )
    }
    navigation(
        route = CREATE_DESTINATION,
        startDestination = SELECT_DESTINATION
    ) {
        composable(route = SELECT_DESTINATION) {
            SelectDestination(navController, it)
        }
        composable(route = VALIDATE_DESTINATION) {
            ValidateDestination(navController, it)
        }
        composable(route = SELECT_DATES) {
            SelectDates(navController, it)
        }
        composable(route = SELECT_TRIP_STYLE) {
            SelectTripStyle(navController, it)
        }
        composable(route = GENERATING_ITINERARY) {
            GeneratingItinerary(navController, it)
        }
    }
    composable(
        route = DESTINATION_DETAIL_ROUTE,
        arguments = listOf(
            navArgument(DESTINATION_ID) { type = NavType.LongType },
            navArgument(TITLE) {
                defaultValue = null
                nullable = true
                type = NavType.StringType
            }
        )
    ) {
        val context = LocalContext.current
        DestinationDetail(
            onEditClick = { destination ->
                navController.navigateToDestinationEdit(destination.id, destination.city)
            },
            onAddClick = { destination, type ->
                navController.navigateToAddInterestPlace(context, destination.id, type)
            },
        )
    }
    composable(
        route = DESTINATION_EDIT_ROUTE,
        arguments = listOf(
            navArgument(DESTINATION_ID) { type = NavType.LongType },
        )
    ) { backStackEntry ->
        backStackEntry.arguments?.getLong(DESTINATION_ID)?.let { destinationId ->
            val context = LocalContext.current
            EditDestinationScreens(
                onAddPlaceClick = { itineraryDay ->
                    navController.navigateToAddPlace(
                        destinationId = destinationId,
                        itineraryDayId = itineraryDay.id,
                        day = context.getString(
                            R.string.title_add_place_day,
                            itineraryDay.day.toString(),
                            formatDate(itineraryDay.date)
                        )
                    )
                }
            )
        }
    }
    composable(
        route = DESTINATION_ADD_PLACE_ROUTE,
        arguments = listOf(
            navArgument(DESTINATION_ID) { type = NavType.LongType },
            navArgument(ITINERARY_DAY_ID) { type = NavType.LongType },
            navArgument(TITLE) {
                defaultValue = null
                nullable = true
                type = NavType.StringType
            }
        )
    ) {
        AddPlace {
            navController.popBackStack()
        }
    }
    composable(
        route = DESTINATION_ADD_INTEREST_PLACE_ROUTE,
        arguments = listOf(
            navArgument(DESTINATION_ID) { type = NavType.LongType },
            navArgument(TITLE) {
                defaultValue = null
                nullable = true
                type = NavType.StringType
            }
        )
    ) {
        AddInterestPlace {
            navController.popBackStack()
        }
    }
}

/**
 * Navigate to the destination detail screen.
 * @param destinationId The id of the destination to show.
 * @param city Has no effect on navigation, it's only used to display the title on the Top Bar.
 */
fun NavController.navigateToDestinationDetail(
    destinationId: Long,
    city: String? = null
) {
    navigate("$DESTINATION_DETAIL/$destinationId?$TITLE=$city") {
        popUpTo(graph.findStartDestination().id)
    }
}

/**
 * Navigate to the destination edit screen.
 * @param destinationId The id of the destination to edit.
 */
fun NavController.navigateToDestinationEdit(
    destinationId: Long,
    city: String? = null
) {
    navigate("$DESTINATION_EDIT/$destinationId?$TITLE=$city")
}

/**
 * Navigate to the add place screen.
 * @param destinationId The id of the destination to edit.
 */
fun NavController.navigateToAddPlace(
    destinationId: Long,
    itineraryDayId: Long,
    day: String? = null
) {
    navigate("$DESTINATION_EDIT/$destinationId/addPlace/$itineraryDayId?$TITLE=$day")
}

/**
 * Navigate to the add interest place screen.
 * @param destinationId The id of the destination to edit.
 */
fun NavController.navigateToAddInterestPlace(
    context: Context,
    destinationId: Long,
    type: PlaceType
) {
    val title = when (type) {
        PlaceType.Dining -> R.string.title_dining
        PlaceType.Shop -> R.string.title_shopping
    }
    navigate(
        "$DESTINATION_EDIT/$destinationId/addInterestPlace?$TYPE=${type.name}&$TITLE=${
            context.getString(
                title
            )
        }"
    )
}