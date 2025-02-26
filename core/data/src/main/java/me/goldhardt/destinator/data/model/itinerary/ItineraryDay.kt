package me.goldhardt.destinator.data.model.itinerary

import me.goldhardt.destinator.core.database.model.ItineraryDayWithItems

class ItineraryDay(
    val id: Long,
    val date: String,
    val day: Int,
    var items: List<ItineraryItem>,
)

fun ItineraryDayWithItems.toItineraryDay(): ItineraryDay =
    ItineraryDay(
        id = itineraryDay.id,
        date = itineraryDay.date,
        day = itineraryDay.day,
        items = itineraryItems.map {
            it.toItineraryItem()
        }.sortedBy { it.order }
    )