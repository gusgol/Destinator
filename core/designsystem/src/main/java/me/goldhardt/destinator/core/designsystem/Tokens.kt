package me.goldhardt.destinator.core.designsystem

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

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

    object Gradient {
        val colors: List<Color>
            @Composable get() = listOf(
                // TODO decide what to do with these...
//                MaterialTheme.colorScheme.tertiary,
//                MaterialTheme.colorScheme.primary,
//                MaterialTheme.colorScheme.secondary,
                Color("#43C0D5".toColorInt()),
                Color("#478AE2".toColorInt())
            )
    }
}

fun Modifier.paddingTopBarAndStatusBar() =
    this.padding(top = Tokens.StatusBar.height + Tokens.TopBar.height)