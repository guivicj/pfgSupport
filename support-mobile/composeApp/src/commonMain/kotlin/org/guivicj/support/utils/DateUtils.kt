package org.guivicj.support.utils

import kotlinx.datetime.LocalDateTime

fun formatDateTime(dateTime: String): String {
    return try {
        val date = LocalDateTime.parse(dateTime)
        date.let {
            "${it.hour}:${it.minute} ${it.dayOfMonth}/${it.monthNumber}/${it.year}"
        }
    } catch (e: Exception) {
        "-"
    }
}