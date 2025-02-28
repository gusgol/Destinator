package me.goldhardt.destinator.data.repository

import me.goldhardt.destinator.data.model.itinerary.ItineraryItem

interface ItinerariesRepository {

    /**
     * Creates a new itinerary day and returns its ID.
     * @param destinationId The Id of the destination.
     * @param itineraryDayId Which itinerary day it should be added to.
     * @param itinerary The itinerary item to be added.
     * @return The ID of the newly created itinerary day.
     */
    suspend fun insertItinerary(
        destinationId: Long,
        itineraryDayId: Long,
        itinerary: ItineraryItem
    )

    suspend fun updateItineraryItemOrder(
        itineraryDayId: Long,
        itineraryItemId: Long,
        offset: Int
    )
}