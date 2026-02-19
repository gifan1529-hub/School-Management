package com.example.schoolmanagement

import androidx.compose.ui.autofill.ContentDataType
import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant

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

fun isLate():Boolean {
    val currentMoment = Clock.System.now()
    val now = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    return now.hour in 8..10
}

fun getAttendanceStatus(): String {
    val currentMoment = Clock.System.now()
    val now = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    return when {
        now.hour < 8 -> "Present"       // Sebelum jam 8
        now.hour in 8..10 -> "Late"     // Jam 8 sampai 10:59
        else -> "Absent"                // Jam 11 ke atas
    }
}

fun getTodayDateS(): String {
    val currentMoment = Clock.System.now()
    return currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
        .date.toString()
}

fun formatToWib(isoString: String): String {
    return try {
        val cleanIso = isoString.split(".").first().removeSuffix("Z") + "Z"
        val instant = Instant.parse(cleanIso)

        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')

        return "$hour:$minute"
    } catch (e: Exception) {
        isoString.split("T").lastOrNull()?.take(5) ?: ""
    }
}