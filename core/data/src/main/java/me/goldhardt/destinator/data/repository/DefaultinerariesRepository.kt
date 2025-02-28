package me.goldhardt.destinator.data.repository

import me.goldhardt.destinator.data.datasource.ItinerariesDataSource
import me.goldhardt.destinator.data.model.itinerary.ItineraryItem
import javax.inject.Inject

class DefaultItinerariesRepository @Inject constructor(
    private val itinerariesDataSource: ItinerariesDataSource
) : ItinerariesRepository {

    override suspend fun insertItinerary(
        destinationId: Long,
        itineraryDayId: Long,
        itinerary: ItineraryItem
    ) {
        itinerariesDataSource.insertItinerary(
            destinationId,
            itineraryDayId,
            itinerary
        )
    }

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