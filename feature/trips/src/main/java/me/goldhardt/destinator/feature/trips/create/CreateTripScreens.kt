package me.goldhardt.destinator.feature.trips.create

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun CreateTripScreen() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Create!!",
            style = MaterialTheme.typography.labelMedium
        )
    }
}