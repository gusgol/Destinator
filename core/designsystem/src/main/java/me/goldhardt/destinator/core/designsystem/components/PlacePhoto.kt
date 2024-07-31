package me.goldhardt.destinator.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import me.goldhardt.destinator.core.designsystem.BuildConfig
import me.goldhardt.destinator.core.designsystem.R

@Composable
fun PlacePhotos(
    imageUrls: List<String>,
    modifier: Modifier = Modifier,
    maxWidthPx: Int? = 400,
    maxHeightPx: Int? = null,
    photosSize: Dp = 120.dp
) {
    var fullScreenPhoto by rememberSaveable { mutableStateOf("") }
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
                    maxHeightPx = maxHeightPx,
                    contentDescription = null, // TODO fix accessibility
                    modifier = Modifier
                        .size(photosSize)
                        .clip(RoundedCornerShape(8.dp)),
                    onPhotoClick = { fullScreenPhoto = it }
                )
            }
        }
    }

    val dismiss = {
        fullScreenPhoto = ""
    }

    if (fullScreenPhoto.isNotBlank()) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = dismiss
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
            ) {
                CircularProgressIndicator()
                PlacePhoto(
                    imageUrl = fullScreenPhoto,
                    maxWidthPx = 1024,
                    contentScale = ContentScale.Fit,
                    placeholder = ColorPainter(Color.Transparent),
                    modifier = Modifier.fillMaxSize(),
                )
                IconButton(
                    onClick = dismiss,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.action_close_photo)
                    )
                }
            }
        }
    }
}

@Composable
fun PlacePhoto(
    imageUrl: String,
    modifier: Modifier = Modifier,
    maxWidthPx: Int? = 400,
    maxHeightPx: Int? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter? = ColorPainter(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
    onPhotoClick: ((String) -> Unit)? = null,
) {
    var transformedUrl = "$imageUrl?"
    if (maxWidthPx != null) {
        transformedUrl += "&maxWidthPx=$maxWidthPx"
    }
    if (maxHeightPx != null) {
        transformedUrl += "&maxHeight`Px=$maxHeightPx"
    }
    transformedUrl += "&key=${BuildConfig.PLACES_API_KEY}"
    val clickableModifier = if (onPhotoClick != null) {
        modifier.clickable { onPhotoClick(imageUrl) }
    } else {
        modifier
    }
    AsyncImage(
        model = transformedUrl,
        contentDescription = contentDescription,
        contentScale = contentScale,
        placeholder = placeholder,
        modifier = clickableModifier
    )
}