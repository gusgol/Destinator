package me.goldhardt.destinator.data.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

const val DATE_MANIPULATION_FORMAT = "dd/MM/yyyy"
const val DATE_MANIPULATION_TZ = "UTC"

fun Long.toUTC(): String {
    val formatter = SimpleDateFormat(DATE_MANIPULATION_FORMAT, Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone(DATE_MANIPULATION_TZ)
    return formatter.format(Date(this))
}