package me.goldhardt.destinator.feature.trips

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import me.goldhardt.destinator.feature.trips.CreateTripScreens.GENERATING_ITINERARY
import me.goldhardt.destinator.feature.trips.CreateTripScreens.SELECT_DATES
import me.goldhardt.destinator.feature.trips.CreateTripScreens.SELECT_DESTINATION
import me.goldhardt.destinator.feature.trips.CreateTripScreens.SELECT_TRIP_STYLE
import me.goldhardt.destinator.feature.trips.destinations.create.GeneratingItinerary
import me.goldhardt.destinator.feature.trips.destinations.create.SelectDestination
import me.goldhardt.destinator.feature.trips.destinations.create.SelectDates
import me.goldhardt.destinator.feature.trips.destinations.create.SelectTripStyle
import me.goldhardt.destinator.feature.trips.destinations.list.TripsListScreen

const val TRIPS_ROUTE = "trips"
const val CREATE_TRIP_ROUTE = "$TRIPS_ROUTE/create"

/**
 * Nested navigation for create trip
 */
object CreateTripScreens {
    const val SELECT_DESTINATION = "$CREATE_TRIP_ROUTE/select_destination"
    const val SELECT_DATES = "$CREATE_TRIP_ROUTE/select_dates"
    const val SELECT_TRIP_STYLE = "$CREATE_TRIP_ROUTE/select_trip_style"
    const val GENERATING_ITINERARY = "$CREATE_TRIP_ROUTE/generating_itinerary"
}

fun NavGraphBuilder.tripsScreens(
    navController: NavHostController
) {
    composable(
        route = TRIPS_ROUTE
    ) {
        TripsListScreen {
            navController.navigate(CREATE_TRIP_ROUTE)
        }
    }
    navigation(
        route = CREATE_TRIP_ROUTE,
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
            GeneratingItinerary()
        }
    }
}