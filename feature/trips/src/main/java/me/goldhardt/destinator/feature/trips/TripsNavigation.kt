package me.goldhardt.destinator.feature.trips

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import me.goldhardt.destinator.feature.trips.CreateTripScreens.GENERATING_ITINERARY
import me.goldhardt.destinator.feature.trips.CreateTripScreens.SELECT_DATES
import me.goldhardt.destinator.feature.trips.CreateTripScreens.SELECT_DESTINATION
import me.goldhardt.destinator.feature.trips.CreateTripScreens.SELECT_TRIP_STYLE
import me.goldhardt.destinator.feature.trips.destinations.create.GeneratingItinerary
import me.goldhardt.destinator.feature.trips.destinations.create.SelectDestination
import me.goldhardt.destinator.feature.trips.destinations.create.SelectDates
import me.goldhardt.destinator.feature.trips.destinations.create.SelectTripStyle
import me.goldhardt.destinator.feature.trips.destinations.detail.DestinationDetail
import me.goldhardt.destinator.feature.trips.destinations.list.DestinationsRoute

const val DESTINATIONS_ROUTE = "trips"
const val CREATE_DESTINATION = "$DESTINATIONS_ROUTE/create"
const val DESTINATION_ID = "destinationId"
const val DESTINATION_DETAIL = "$DESTINATIONS_ROUTE/detail"

/**
 * Nested navigation for create trip
 */
object CreateTripScreens {
    const val SELECT_DESTINATION = "$CREATE_DESTINATION/select_destination"
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
            onDestinationClick = { destinationId ->
                navController.navigateToDestinationDetail(destinationId)
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
        route = "$DESTINATION_DETAIL/{$DESTINATION_ID}",
        arguments = listOf(
            navArgument(DESTINATION_ID) { type = NavType.LongType }
        )
    ) {
        DestinationDetail()
    }
}

fun NavController.navigateToDestinationDetail(destinationId: Long) {
    navigate("$DESTINATION_DETAIL/$destinationId") {
        popUpTo(graph.findStartDestination().id)
    }
}