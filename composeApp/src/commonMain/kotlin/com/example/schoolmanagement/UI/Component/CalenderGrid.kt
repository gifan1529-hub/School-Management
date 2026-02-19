package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.HomeViewModel
import kotlinx.datetime.*
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CalendarGrid(date: LocalDate, font: FontFamily, primaryColor: Color) {
    val daysInMonth = when (date.month) {
        Month.FEBRUARY -> if ((date.year % 4 == 0 && date.year % 100 != 0) || (date.year % 400 == 0)) 29 else 28
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        else -> 31
    }
    val firstDayOfMonth = LocalDate(date.year, date.month, 1).dayOfWeek.isoDayNumber
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    val shift = if (firstDayOfMonth == 7) 0 else firstDayOfMonth
    val poppins = getPoppinsFontFamily()

    Column(modifier = Modifier.padding(16.dp).background(Color.White,
        RoundedCornerShape(16.dp)
    ).padding(16.dp)) {
        // Header Hari
        Row(Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                Text(day, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp, color = Color.Gray)
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(300.dp),
            userScrollEnabled = false
        ) {
            // Kolom Kosong di awal bulan
            items(shift) { Spacer(Modifier.size(40.dp)) }

            // Angka Tanggal
            items(daysInMonth) { i ->
                val day = i + 1
                val status = when {
                    day % 5 == 0 -> "Absent"
                    day % 2 == 0 -> "Present"
                    else -> "None"
                }

                Box(
                    modifier = Modifier.padding(4.dp).aspectRatio(1f).clip(RoundedCornerShape(8.dp))
                        .background(
                            when(status) {
                                "Present" -> Color(0xFF2ECC71)
                                "Absent" -> Color(0xFFE74C3C)
                                else -> Color.Transparent
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.toString(),
                        color = if (status == "None") Color.LightGray else Color.White,
                        fontFamily = font,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) { LegendItem(Color(0xFF2ECC71), "Present", poppins)
            Spacer(Modifier.width(16.dp))
            LegendItem(Color(0xFFE74C3C), "Absent", poppins)
        }
    }
}