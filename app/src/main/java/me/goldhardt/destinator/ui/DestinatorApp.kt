package me.goldhardt.destinator.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.goldhardt.destinator.R
import me.goldhardt.destinator.navigation.DestinatorNavHost
import me.goldhardt.destinator.navigation.START_DESTINATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinatorApp() {
    val navController = rememberNavController()

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Destinator".uppercase(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge,
                    )
                },
                navigationIcon = {
                    if (currentBackStackEntry.value?.destination?.route != START_DESTINATION) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.cd_navigate_back)
                            )
                        }
                    }
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
