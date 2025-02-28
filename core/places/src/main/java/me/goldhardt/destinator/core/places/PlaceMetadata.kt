package me.goldhardt.destinator.core.places

data class PlaceMetadata(
    val sourceId: String,
    val iconUrl: String?,
    val latitude: Double?,
    val longitude: Double?,
    val photosReferences: List<String>?,
    val displayName: String?,
    val description: String?,
    val address: String?,
)