package me.goldhardt.destinator.data.model.destination

import me.goldhardt.destinator.core.database.model.DestinationWithItinerary
import me.goldhardt.destinator.data.model.itinerary.ItineraryItem
import me.goldhardt.destinator.data.model.itinerary.toItineraryItem

data class Destination(
    val city: String,
    val country: String,
    val from: Long,
    val to: Long,
    val longitude: Double,
    val latitude: Double,
    val thumbnail: String,
    val itinerary: List<ItineraryItem>
)

fun DestinationWithItinerary.toDestination(): Destination =
    Destination(
        city = destination.city,
        country = destination.country,
        from = destination.from,
        to = destination.to,
        longitude = destination.longitude,
        latitude = destination.latitude,
        thumbnail = destination.thumbnail,
        itinerary = itinerary.map { it.toItineraryItem() }
    )