package me.goldhardt.destinator.core.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import me.goldhardt.destinator.core.designsystem.BuildConfig

@Composable
fun StaticMapImage(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    zoom: Int = 10,
    width: Int = 400,
    height: Int = 400,
) {
    val url =
        "https://maps.googleapis.com/maps/api/staticmap?" +
                "center=$latitude,$longitude&zoom=$zoom&size=${width}x$height" +
                "&key=${BuildConfig.PLACES_API_KEY}"
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        placeholder = ColorPainter(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        modifier = modifier
    )
}