package me.goldhardt.destinator.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

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
        @Composable get() = currentBackStackEntry?.arguments?.getString(ARG_TITLE)

    /**
     * The current back stack entry.
     */
    private val currentBackStackEntry
        @Composable get() = navController.currentBackStackEntryAsState().value
}