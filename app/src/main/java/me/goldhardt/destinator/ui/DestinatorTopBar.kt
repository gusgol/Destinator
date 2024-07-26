package me.goldhardt.destinator.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import me.goldhardt.destinator.R
import me.goldhardt.destinator.navigation.START_DESTINATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinatorTopBar(
    appState: DestinatorAppState,
) {
    CenterAlignedTopAppBar(
//        modifier = Modifier
//            .padding(top = Tokens.StatusBar.height),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        ),
        title = {
            appState.currentScreenTitle?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        },
        navigationIcon = {
            if (appState.currentDestination?.route != START_DESTINATION) {
                IconButton(
                    onClick = {
                        appState.navController.popBackStack()
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_navigate_back)
                    )
                }
            }
        },
    )
}