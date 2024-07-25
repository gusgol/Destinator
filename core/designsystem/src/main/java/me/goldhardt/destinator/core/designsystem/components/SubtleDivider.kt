package me.goldhardt.destinator.core.designsystem.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SubtleHorizontalDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
        modifier = modifier
    )
}

@Composable
fun SubtleVerticalDivider(
    modifier: Modifier = Modifier
) {
    VerticalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
        modifier = modifier
    )
}