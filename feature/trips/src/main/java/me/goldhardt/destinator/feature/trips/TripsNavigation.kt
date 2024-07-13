package me.goldhardt.destinator.feature.trips

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
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
        startDestination = CreateTripScreens.SELECT_DESTINATION
    ) {
        composable(route = CreateTripScreens.SELECT_DESTINATION) {
            SelectDestination {
                navController.navigate(CreateTripScreens.SELECT_DATES)
            }
        }
        composable(route = CreateTripScreens.SELECT_DATES) {
            SelectDates {
                navController.navigate(CreateTripScreens.SELECT_TRIP_STYLE)
            }
        }
        composable(route = CreateTripScreens.SELECT_TRIP_STYLE) {
            SelectTripStyle()
        }
    }
}