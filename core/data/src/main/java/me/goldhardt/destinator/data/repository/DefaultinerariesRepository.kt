package me.goldhardt.destinator.data.repository

import me.goldhardt.destinator.data.datasource.ItinerariesDataSource
import javax.inject.Inject

class DefaultItinerariesRepository @Inject constructor(
    private val itinerariesDataSource: ItinerariesDataSource
) : ItinerariesRepository {
    override suspend fun updateItineraryItemOrder(
        itineraryDayId: Long,
        itineraryItemId: Long,
        offset: Int
    ) {
        itinerariesDataSource.updateItineraryItemOrder(
            itineraryDayId,
            itineraryItemId,
            offset
        )
    }
}