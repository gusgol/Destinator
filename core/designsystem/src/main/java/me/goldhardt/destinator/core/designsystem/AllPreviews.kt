package me.goldhardt.destinator.core.designsystem

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Preview(
    name = "Small Screen", device = Devices.PIXEL,
    showSystemUi = true
)
@Preview(
    name = "Small Screen - Font Scale",
    device = Devices.PIXEL,
    showSystemUi = true
)
@Preview(
    name = "Medium Screen",
    device = Devices.PIXEL_4,
    showSystemUi = true
)
@Preview(
    name = "Large Screen", device =
    Devices.PIXEL_7_PRO,
    showSystemUi = true
)
@Preview(
    name = "Tablet", device =
    Devices.PIXEL_TABLET,
    showSystemUi = true
)
@PreviewLightDark
annotation class AllPreviews