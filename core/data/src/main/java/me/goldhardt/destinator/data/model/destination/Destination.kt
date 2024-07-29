package me.goldhardt.destinator.data.model.destination

import me.goldhardt.destinator.core.database.model.DestinationWithItinerary
import me.goldhardt.destinator.data.R
import me.goldhardt.destinator.data.model.itinerary.ItineraryItem
import me.goldhardt.destinator.data.model.itinerary.toItineraryItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Destination(
    val id: Long,
    val city: String,
    val country: String,
    val from: String,
    val to: String,
    val longitude: Double,
    val latitude: Double,
    val thumbnail: String,
    val itinerary: List<ItineraryItem>,
    val status: DestinationStatus
)

enum class DestinationStatus(val displayName: Int) {
    CURRENT(R.string.destination_status_current),
    UPCOMING(R.string.destination_status_upcoming),
    COMPLETED(R.string.destination_status_completed)
}

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
        itinerary = itinerary.map { it.toItineraryItem() },
        status = determineDestinationStatus(destination.from, destination.to)
    )

private fun determineDestinationStatus(from: String, to: String): DestinationStatus {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    val fromDate = LocalDate.parse(from, formatter)
    val toDate = LocalDate.parse(to, formatter)
    val today = LocalDate.now()

    return when {
        today.isBefore(fromDate) -> DestinationStatus.UPCOMING
        today.isAfter(toDate) -> DestinationStatus.COMPLETED
        else -> DestinationStatus.CURRENT
    }
}