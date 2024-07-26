package me.goldhardt.destinator.core.designsystem

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Suppress("KDocUnresolvedReference")
object Tokens {
    object TopBar {
        /**
         * @see [TopAppBarSmallTokens.ContainerHeight]
         */
        val height = 64.dp
    }
    object StatusBar {
        val height = 24.dp
    }
}

fun Modifier.paddingTopBarAndStatusBar() =
    this.padding(top = Tokens.StatusBar.height + Tokens.TopBar.height)