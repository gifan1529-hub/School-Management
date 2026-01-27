package com.example.schoolmanagement

import androidx.compose.ui.autofill.ContentDataType
import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun getTodayDate(): String {
    // ngambil waktu sekrang
    val currentMoment = Clock.System.now()
    val date = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    // list nama bulan singkat
    val monthNames = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    val month = monthNames[date.monthNumber - 1]
    val day = date.dayOfMonth

    return "$month $day"
}