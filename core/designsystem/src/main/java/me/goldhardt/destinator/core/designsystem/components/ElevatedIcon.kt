package me.goldhardt.destinator.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

/**
 * Icons that should not appear in the UI.
 * TODO ideally this has to be done at the data layer
 */
val disallowedIcons = listOf(
    "https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet.png"
)

@Composable
fun ElevatedIcon(
    iconUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    defaultIcon: ImageVector = Icons.Default.Place,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val url = iconUrl.takeUnless { it in disallowedIcons } ?: ""
    Card(
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .build(),
                modifier = modifier,
                contentDescription = null
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Error -> {
                        Icon(
                            defaultIcon,
                            contentDescription = contentDescription,
                            tint = tint
                        )
                    }

                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator()
                    }

                    else -> Image(
                        painter = painter,
                        contentDescription = contentDescription,
                        colorFilter = ColorFilter.tint(tint),
                    )
                }
            }
        }
    }
}