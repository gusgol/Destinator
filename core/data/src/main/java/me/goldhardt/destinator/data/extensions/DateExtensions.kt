package me.goldhardt.destinator.data.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Convert a Long to a ISO8601 date string
 * Example: YYYY-MM-DD
 */
fun Long.toISO8601(
    zoneOffset: ZoneOffset = ZoneOffset.UTC
): String {
    val instant = Instant.ofEpochMilli(this)
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE.withZone(zoneOffset)
    return formatter.format(instant)
}

/**
 * Formats two given ISO 8601 date strings into a human-readable string.
 *
 * This function takes two strings representing dates in ISO 8601 format (YYYY-MM-DD) and formats them
 * into a more human-readable form. The output format changes based on whether the dates are within the
 * same year or not.
 *
 * If the dates are within the same year, the formatted string will exclude the year from the start date,
 * resulting in a format like "21 Oct - 24 Oct 2024". If the dates span across different years, the formatted
 * string will include the years in both dates, resulting in a format like "29 Dec 2024 - 3 Jan 2025".
 *
 * @param startDateString The start date in ISO 8601 format (YYYY-MM-DD).
 * @param endDateString The end date in ISO 8601 format (YYYY-MM-DD).
 * @return A string representing the formatted date range.
 */
fun formatDates(startDateString: String, endDateString: String): String {
    val startDate = LocalDate.parse(startDateString)
    val endDate = LocalDate.parse(endDateString)

    val sameYear = startDate.year == endDate.year

    val startFormat = if (sameYear) DateTimeFormatter.ofPattern("d MMM") else DateTimeFormatter.ofPattern("d MMM yyyy")
    val endFormat = DateTimeFormatter.ofPattern("d MMM yyyy")

    val formattedStart = startDate.format(startFormat)
    val formattedEnd = endDate.format(endFormat)

    return "$formattedStart - $formattedEnd"
}

/**
 * Formats a given ISO 8601 date string into a human-readable string.
 * Example: 2024-10-21 -> 21 Oct
 */
fun formatDate(dateString: String): String {
    val date = LocalDate.parse(dateString)
    val formatter = DateTimeFormatter.ofPattern("d MMM")
    return date.format(formatter)
}