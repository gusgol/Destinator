package me.goldhardt.destinator.data.model.destination

import me.goldhardt.destinator.core.database.model.DestinationWithItinerary
import me.goldhardt.destinator.data.model.itinerary.ItineraryItem
import me.goldhardt.destinator.data.model.itinerary.toItineraryItem

data class Destination(
    val id: Long,
    val city: String,
    val country: String,
    val from: String,
    val to: String,
    val longitude: Double,
    val latitude: Double,
    val thumbnail: String,
    val itinerary: List<ItineraryItem>
)

fun DestinationWithItinerary.toDestination(): Destination =
    Destination(
        id = destination.id,
        city = destination.city,
        country = destination.country,
        from = destination.from,
        to = destination.to,
        longitude = destination.longitude,
        latitude = destination.latitude,
        thumbnail = destination.thumbnail,
        itinerary = itinerary.map { it.toItineraryItem() }
    )