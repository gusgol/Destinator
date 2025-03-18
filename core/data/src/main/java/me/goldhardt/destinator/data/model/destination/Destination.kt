package me.goldhardt.destinator.data.model.destination

import me.goldhardt.destinator.core.database.model.DestinationWithItineraryDays
import me.goldhardt.destinator.data.R
import me.goldhardt.destinator.data.model.itinerary.ItineraryDay
import me.goldhardt.destinator.data.model.itinerary.toItineraryDay
import me.goldhardt.destinator.data.model.places.InterestPlace
import me.goldhardt.destinator.data.model.places.toInterestPlace
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
    val itineraryDays: List<ItineraryDay>,
    val itineraryCount: Int,
    val status: DestinationStatus,
    val interestPlaces: List<InterestPlace>,
)

enum class DestinationStatus(val displayName: Int) {
    CURRENT(R.string.destination_status_current),
    UPCOMING(R.string.destination_status_upcoming),
    COMPLETED(R.string.destination_status_completed)
}

fun DestinationWithItineraryDays.toDestination(): Destination {
    val itineraryDays = itineraryDays.map { it.toItineraryDay() }
    val itineraryItemsCount = itineraryDays.sumOf { it.items.size }

    return Destination(
        id = destination.id,
        city = destination.city,
        country = destination.country,
        from = destination.from,
        to = destination.to,
        longitude = destination.longitude,
        latitude = destination.latitude,
        thumbnail = destination.thumbnail,
        itineraryDays = itineraryDays,
        itineraryCount = itineraryItemsCount,
        status = determineDestinationStatus(destination.from, destination.to),
        interestPlaces = interestPlaces.map { it.toInterestPlace() }
    )
}

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