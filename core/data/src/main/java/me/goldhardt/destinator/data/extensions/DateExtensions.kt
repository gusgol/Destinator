package me.goldhardt.destinator.data.extensions

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

const val DATE_MANIPULATION_FORMAT = "dd/MM/yyyy"
const val DATE_MANIPULATION_TZ = "UTC"

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