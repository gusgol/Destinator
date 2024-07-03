package me.goldhardt.destinator.feature.trips

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import me.goldhardt.destinator.feature.trips.create.CreateTripScreen
import me.goldhardt.destinator.feature.trips.list.TripsListScreen

const val TRIPS_ROUTE = "trips"
const val CREATE_TRIP_ROUTE = "$TRIPS_ROUTE/create"

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
    composable(
        route = CREATE_TRIP_ROUTE
    ) {
        CreateTripScreen()
    }
}