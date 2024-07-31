package me.goldhardt.destinator.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import me.goldhardt.destinator.core.common.LocalMenuItemState
import me.goldhardt.destinator.core.common.MenuItemsState
import me.goldhardt.destinator.navigation.DestinatorNavHost

@Composable
fun DestinatorApp() {
    val navController = rememberNavController()
    val appState = rememberDestinatorAppState(navController)

    val menuItemsState = MenuItemsState()

    CompositionLocalProvider(
        LocalMenuItemState provides menuItemsState
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            DestinatorNavHost(
                navController = navController,
                modifier = Modifier
                    .padding(padding)
            )
            DestinatorTopBar(appState)
        }
    }
}
