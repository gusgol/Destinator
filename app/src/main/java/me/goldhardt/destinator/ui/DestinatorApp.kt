package me.goldhardt.destinator.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import me.goldhardt.destinator.navigation.DestinatorNavHost

@Composable
fun DestinatorApp() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            DestinatorTopBar(navController = navController)
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        DestinatorNavHost(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}
