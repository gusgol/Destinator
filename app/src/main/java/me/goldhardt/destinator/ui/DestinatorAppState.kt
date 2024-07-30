package me.goldhardt.destinator.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import me.goldhardt.destinator.R
import me.goldhardt.destinator.navigation.START_DESTINATION

@Composable
fun rememberDestinatorAppState(
    navController: NavHostController,
): DestinatorAppState {
    return remember(
        navController
    ) {
        DestinatorAppState(navController)
    }
}

class DestinatorAppState(
    val navController: NavHostController,
) {
    companion object {
        const val ARG_TITLE = "title"
        val displayTopBarTitleRoutes = listOf(
            START_DESTINATION
        )
    }

    /**
     * The current destination in the navigation graph.
     */
    val currentDestination: NavDestination?
        @Composable get() = currentBackStackEntry?.destination

    /**
     * The title of the current screen or null if navigation destination does not contain title.
     */
    val currentScreenTitle: String?
        @Composable get() = if (currentDestination?.route in displayTopBarTitleRoutes) {
            stringResource(id = R.string.app_name)
        } else {
            currentBackStackEntry?.arguments?.getString(ARG_TITLE)
        }

    /**
     * The current back stack entry.
     */
    private val currentBackStackEntry
        @Composable get() = navController.currentBackStackEntryAsState().value
}