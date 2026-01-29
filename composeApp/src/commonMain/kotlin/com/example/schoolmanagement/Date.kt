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

fun getTodayTime(): String {
    val currentMoment = Clock.System.now()
    val dateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    // nambahin angka 0 jika jam atau menit di bawah 10
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')

    return "$hour:$minute"
}

fun getTodaDayName(): String {
    val currentMoment = Clock.System.now()
    val dateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    return when (dateTime.dayOfWeek.name) {
        "MONDAY" -> "Senin"
        "TUESDAY" -> "Selasa"
        "WEDNESDAY" -> "Rabu"
        "THURSDAY" -> "Kamis"
        "FRIDAY" -> "Jumat"
        "SATURDAY" -> "Sabtu"
        "SUNDAY" -> "Minggu"
        else -> ""
    }
}