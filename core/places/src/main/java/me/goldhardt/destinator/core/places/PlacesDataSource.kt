package me.goldhardt.destinator.core.places

interface PlacesDataSource {
    /**
     * Get a place from a query and close from the coordinates provided.
     * @param query The query to search for.
     * @param latitude Latitude location bias.
     * @param longitude Latitude location bias.
     */
    suspend fun getPlace(
        query: String,
        latitude: Double,
        longitude: Double,
    ): PlaceMetadata?
}