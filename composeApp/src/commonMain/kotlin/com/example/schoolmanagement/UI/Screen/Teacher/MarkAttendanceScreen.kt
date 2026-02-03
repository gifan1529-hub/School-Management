package com.example.schoolmanagement.UI.Screen.Teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagement.UI.Component.AttendanceCard
import com.example.schoolmanagement.ViewModel.HomeViewModel
import com.example.schoolmanagement.ViewModel.MarkAttendanceViewModel
import com.example.schoolmanagement.getTodayDate
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant

@Composable
fun MarkAttendanceScreen (
    navController: NavHostController,
    viewModel: MarkAttendanceViewModel = koinViewModel()
) {
    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)

//    val selectedDate by viewModel.selectedDate.collectAsState()
//    var showDatePicker by remember { mutableStateOf(false) }
//    val datePickerState = rememberDatePickerState()

    val students by viewModel.studentList.collectAsState()
    val countHadir by viewModel.countHadir.collectAsState()
    val countAbsen by viewModel.countAbsen.collectAsState()

//    if (showDatePicker) {
//        DatePickerDialog(
//            onDismissRequest = { showDatePicker = false },
//            confirmButton = {
//                TextButton(onClick = {
//                    val selectedMillis = datePickerState.selectedDateMillis
//                    if (selectedMillis != null) {
//                        // Konversi millis ke String YYYY-MM-DD
//                        val date = Instant.fromEpochMilliseconds(selectedMillis)
//                            .toLocalDateTime(currentSystemDefault()).date.toString()
//                        viewModel.updateSelectedDate(date)
//                    }
//                    showDatePicker = false
//                }) { Text("Pilih") }
//            },
//            dismissButton = {
//                TextButton(onClick = { showDatePicker = false }) { Text("Batal") }
//            }
//        ) {
//            DatePicker(state = datePickerState)
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(primaryBlue,
                    RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(top = 40.dp, bottom = 24.dp, start = 16.dp, end = 24.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                    Text(
                        "Daftar Siswa Kelas",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AttendanceSummaryItem("Siswa Hadir", countHadir, Color.White)
                    AttendanceSummaryItem("Belum Absen / Absent", countAbsen, Color.White.copy(alpha = 0.7f))
                    AttendanceSummaryItem("Tanggal", getTodayDate(), Color.White)
                }
//                Surface(
//                    color = Color.White.copy(alpha = 0.2f),
//                    shape = RoundedCornerShape(12.dp),
//                    onClick = { showDatePicker = true }
//                ) {
//                    Row(
//                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = selectedDate,
//                            color = Color.White,
//                            fontSize = 12.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                        Spacer(Modifier.width(4.dp))
//                        Icon(
//                            imageVector = Icons.Default.DateRange,
//                            contentDescription = null,
//                            tint = Color.White,
//                            modifier = Modifier.size(14.dp)
//                        )
//                    }
//                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val hadirList = students.filter { it.status == "Present" || it.status == "Late" }

            if (hadirList.isNotEmpty()) {
                item {
                    Text(
                        "Sudah Hadir (${hadirList.size})",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                items(hadirList) { student ->
                    AttendanceCard(
                        name = student.name,
                        primaryColor = if (student.status == "Present") Color(0xFF4CAF50) else Color(0xFFFFA500),
                        timeIn = student.time_in,
                        staus = student.status
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }

            val absent = students.filter { it.status == "Absent" }

            if (absent.isNotEmpty()) {
                item {
                    Text(
                        "Absent (${absent.size})",
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                items(absent) { student ->
                    AttendanceCard(
                        name = student.name,
                        primaryColor = Color.Red,
                        timeIn = student.time_in,
                        staus = student.status
                    )
                }
            }

            val belummHadir = students.filter { it.status == "Not Present Yet" }

            if (belummHadir.isNotEmpty()) {
                item {
                    Text(
                        "Belum Hadir (${belummHadir.size})",
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                items(belummHadir) { student ->
                    AttendanceCard(
                        name = student.name,
                        primaryColor = Color.Red,
                        timeIn = student.time_in,
                        staus = student.status
                    )
                }
            }
        }
    }
}

@Composable
fun AttendanceSummaryItem(label: String, value: String, color: Color) {
    Column {
        Text(label, color = color.copy(alpha = 0.8f), fontSize = 12.sp)
        Text(value, color = color, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}