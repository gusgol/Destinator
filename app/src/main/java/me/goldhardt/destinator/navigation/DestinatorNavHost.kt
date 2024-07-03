package me.goldhardt.destinator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController

@Composable
fun DestinatorNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "home", //TODO change this to the actual start destination
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

    }
}