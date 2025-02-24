package me.goldhardt.destinator.data.model.itinerary

import me.goldhardt.destinator.core.database.model.ItineraryDayWithItems

class ItineraryDay(
    val date: String,
    val day: Int,
    val items: List<ItineraryItem>,
)

fun ItineraryDayWithItems.toItineraryDay(): ItineraryDay =
    ItineraryDay(
        date = itineraryDay.date,
        day = itineraryDay.day,
        items = itineraryItems.map { it.toItineraryItem() }
    )