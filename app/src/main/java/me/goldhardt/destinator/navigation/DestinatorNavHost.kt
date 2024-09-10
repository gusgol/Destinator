package me.goldhardt.destinator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import me.goldhardt.destinator.feature.trips.DESTINATIONS_ROUTE
import me.goldhardt.destinator.feature.trips.tripsScreens

const val START_DESTINATION = DESTINATIONS_ROUTE

@Composable
fun DestinatorNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = START_DESTINATION,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        tripsScreens(navController)
    }
}