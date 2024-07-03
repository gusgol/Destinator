package me.goldhardt.destinator.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import me.goldhardt.destinator.core.designsystem.theme.DestinatorTheme
import me.goldhardt.destinator.navigation.DestinatorNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinatorApp() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Destinator") // TODO extract
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        DestinatorNavHost(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}
