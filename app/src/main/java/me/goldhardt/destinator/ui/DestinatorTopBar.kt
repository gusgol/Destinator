package me.goldhardt.destinator.ui

import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import me.goldhardt.destinator.R
import me.goldhardt.destinator.core.designsystem.Tokens
import me.goldhardt.destinator.navigation.START_DESTINATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinatorTopBar(
    navController: NavHostController,
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    CenterAlignedTopAppBar(
//        modifier = Modifier
//            .padding(top = Tokens.StatusBar.height),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            if (currentBackStackEntry.value?.destination?.route == START_DESTINATION) {
                Text(
                    text = "DESTINATOR".uppercase(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        },
        navigationIcon = {
            if (
                currentBackStackEntry.value?.destination?.route != START_DESTINATION) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
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