package me.goldhardt.destinator.data.repository

interface ItinerariesRepository {
    suspend fun updateItineraryItemOrder(
        itineraryDayId: Long,
        itineraryItemId: Long,
        offset: Int
    )
}