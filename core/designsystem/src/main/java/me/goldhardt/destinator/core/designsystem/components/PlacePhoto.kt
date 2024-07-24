package me.goldhardt.destinator.core.designsystem.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PlacePhotos(
    imageUrls: List<String>,
    modifier: Modifier = Modifier,
    maxWidthPx: Int? = 400,
    maxHeightPx: Int? = null,
) {
    Box(
        modifier = modifier.horizontalScroll(rememberScrollState()),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            imageUrls.forEach {
                PlacePhoto(
                    imageUrl = it,
                    maxWidthPx = maxWidthPx,
                    maxHeightPx = maxHeightPx
                )
            }
        }
    }
}

@Composable
fun PlacePhoto(
    imageUrl: String,
    maxWidthPx: Int? = 400,
    maxHeightPx: Int? = null
) {
    var transformedUrl = "$imageUrl?"
    if (maxWidthPx != null) {
        transformedUrl += "&maxWidthPx=$maxWidthPx"
    }
    if (maxHeightPx != null) {
        transformedUrl += "&maxHeightPx=$maxHeightPx"
    }
    transformedUrl += "&key=AIzaSyDeOWPFuQ6yLrofwvtyzybeRGVmuBNyfTs"
    AsyncImage(
        model = transformedUrl,
        contentDescription = null, // TODO fix accessibility
        contentScale = ContentScale.Crop,
        placeholder = ColorPainter(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}