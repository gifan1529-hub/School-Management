package com.example.schoolmanagement.UI.Screen.Teacher

import androidx.compose.animation.core.copy
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.schoolmanagement.DI.ToastHelper
import com.example.schoolmanagement.Domain.Model.HomeworkData
import com.example.schoolmanagement.UI.Component.HomeworkTeacherItem
import com.example.schoolmanagement.UI.Component.TimePickerDialog
import com.example.schoolmanagement.ViewModel.HomeWorkViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun HomeWorkScreen (
    navController: NavHostController,
    viewModel: HomeWorkViewModel = koinViewModel()
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState(is24Hour = true)

    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)

    val homeworkList by viewModel.homeworkList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isAddSuccess by viewModel.isAddSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val errorLoad by viewModel.errorLoadHomeworks.collectAsState()
    val errorSubmit by viewModel.errorSubmitHomeworks.collectAsState()
    val errorDelete by viewModel.errorDeleteHomeworks.collectAsState()
    val errorAdd by viewModel.errorAddHomeworks.collectAsState()


    var taskTitle by remember { mutableStateOf("") }
    var taskDesc by remember { mutableStateOf("") }
    var taskSubject by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedClass by remember { mutableStateOf("Pilih Kelas") }
    val classOptions = listOf("12-IPA-1", "11-IPA-2", "10-IPA-3")
    var deadLine by remember { mutableStateOf("Pilih Tanggal") }

    if(showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        val date = Instant.fromEpochMilliseconds(selectedMillis)
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
                        deadLine = date
                        showDatePicker = false
                        showTimePicker = true // lanjut munculin jam setelah tanggal dipilih
                    }
                }) { Text("Lanjut Pilih Jam") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val hour = timePickerState.hour.toString().padStart(2, '0')
                    val min = timePickerState.minute.toString().padStart(2, '0')
                    // Gabungkan Tanggal yang tadi dengan Jam baru
                    deadLine = "$deadLine $hour:$min"
                    showTimePicker = false
                }) { Text("Selesai") }
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadHomeworks()
    }

    LaunchedEffect(errorMessage) {
        ToastHelper().Toast(errorMessage ?: "")
    }
    LaunchedEffect(isAddSuccess) {
        if (isAddSuccess) {
            taskTitle = ""
            taskDesc = ""
            taskSubject = ""
            viewModel.resetState()
        }
    }

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
                Text(
                    "Beri Tugas Murid",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Buat Tugas Baru", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = taskSubject,
                            onValueChange = { taskSubject = it },
                            label = { Text("Mata Pelajaran") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = taskTitle,
                            onValueChange = { taskTitle = it },
                            label = { Text("Deskripsi Tugas") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = selectedClass,
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text("Kelas") },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    trailingIcon = {
                                        IconButton(onClick = { expanded = !expanded }) {
                                            Icon(if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown, null)
                                        }
                                    }
                                )
                                Box(Modifier.matchParentSize().clickable { expanded = !expanded })
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    classOptions.forEach { cls ->
                                        DropdownMenuItem(text = { Text(cls) }, onClick = { selectedClass = cls; expanded = false })
                                    }
                                }
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = deadLine,
                                    readOnly = true,
                                    enabled = false,
                                    onValueChange = { deadLine = it },
                                    label = { Text("Deadline (Tgl)") },
                                    modifier = Modifier
                                        .clickable { showDatePicker = true }
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        disabledTextColor = Color.Black,
                                        disabledBorderColor = Color.Gray,
                                        disabledLabelColor = primaryBlue
                                    )
                                )
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clickable { showDatePicker = true }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.addHomework(
                                    subject = taskSubject,
                                    targetClass = selectedClass,
                                    title = taskTitle,
                                    description = "Silakan kerjakan tugas ini",
                                    deadline = deadLine
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                        ) {
                            Text("Bagikan Tugas")
                        }
                    }
                }
            }

            item {
                Text("Tugas Terkirim", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            items(homeworkList) { hw ->
                HomeworkTeacherItem(
                    HomeworkData(hw.title, hw.`class`, "Terkirim", hw.deadline),
                    primaryBlue,
                    onClick = { navController.navigate("detailtugasguru/${hw.id}") },
                    onDelete = { viewModel.deletePR(hw.id) }
                )
            }
        }
    }
}
