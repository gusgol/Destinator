package me.goldhardt.destinator.core.designsystem.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NextStepButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    onNextClick: () -> Unit,
) {
    val containerColor = if (enabled) {
        FloatingActionButtonDefaults.containerColor
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    FloatingActionButton(
        onClick = {
            if (enabled) {
                onNextClick()
            }
        },
        containerColor = containerColor,
        modifier = modifier
            .size(64.dp),
    ) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = contentDescription
        )
    }
}