package com.example.schoolmanagement.UI.Screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.navigation.NavController
import com.example.schoolmanagement.UI.Component.TimePickerDialog
import com.example.schoolmanagement.UI.Component.TipeIzinChip
import com.example.schoolmanagement.ViewModel.PermitViewModel
import com.example.schoolmanagement.getTodayDate
import com.example.schoolmanagement.getTodayDateS
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Instant
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormIzin (
    navController: NavController,
    viewModel: PermitViewModel = koinViewModel ()
) {
    val tanggal = getTodayDate()
    val tanggals = getTodayDateS()

    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F5F5)

    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    val tipeIzin by viewModel.tipeIzin.collectAsState()
    val tanggalMulai by viewModel.tanggalMulai.collectAsState()
    val tanggalSelesai by viewModel.tanggalSelesai.collectAsState()
    val jamMulai by viewModel.jamMulai.collectAsState()
    val jamSelesai by viewModel.jamSelesai.collectAsState()
    val alasan by viewModel.alasan.collectAsState("sakit")

    var showTimePickerMulai by remember { mutableStateOf(false) }
    var showTimePickerSelesai by remember { mutableStateOf(false) }
    var showDatePickerMulai by remember { mutableStateOf(false) }
    var showDatePickerSelesai by remember { mutableStateOf(false) }

    val timePickerStateMulai = rememberTimePickerState(initialHour = 8, initialMinute = 0, is24Hour = true)
    val timePickerStateSelesai = rememberTimePickerState(initialHour = 10, initialMinute = 0, is24Hour = true)
    val datePickerStateMulai = rememberDatePickerState()
    val datePickerStateSelesai = rememberDatePickerState()

    if (showDatePickerMulai) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerMulai = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerStateMulai.selectedDateMillis
                    if (selectedMillis != null) {
                        val date = Instant.fromEpochMilliseconds(selectedMillis)
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
                        viewModel.onStartDateChange(date)
                    }
                    showDatePickerMulai = false
                }) { Text("Pilih") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerMulai = false }) { Text("Batal") }
            }
        ) {
            DatePicker(state = datePickerStateMulai)
        }
    }

    if (showDatePickerSelesai) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerSelesai = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerStateSelesai.selectedDateMillis
                    if (selectedMillis != null) {
                        val date = Instant.fromEpochMilliseconds(selectedMillis)
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
                        viewModel.onEndDateChange(date)
                    }
                    showDatePickerSelesai = false
                }) { Text("Pilih") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerSelesai = false }) { Text("Batal") }
            }
        ) {
            DatePicker(state = datePickerStateSelesai)
        }
    }

    if (showTimePickerMulai) {
        TimePickerDialog(
            onDismissRequest = { showTimePickerMulai = false },
            confirmButton = {
                TextButton(onClick = {
                    val time = "${timePickerStateMulai.hour.toString().padStart(2, '0')}:${timePickerStateMulai.minute.toString().padStart(2, '0')}"
                    viewModel.onTimeInChange(time)
                    showTimePickerMulai = false
                }) { Text("Pilih") }
            }
        ) {
            TimePicker(state = timePickerStateMulai)
        }
    }

    if (showTimePickerSelesai) {
        TimePickerDialog(
            onDismissRequest = { showTimePickerSelesai = false },
            confirmButton = {
                TextButton(onClick = {
                    val time = "${timePickerStateSelesai.hour.toString().padStart(2, '0')}:${timePickerStateSelesai.minute.toString().padStart(2, '0')}"
                    viewModel.onTimeOutChange(time)
                    showTimePickerSelesai = false
                }) { Text("Pilih") }
            }
        ) {
            TimePicker(state = timePickerStateSelesai)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = primaryBlue,
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 40.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "Form Permohonan Izin",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-30).dp)
                    .fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Pilih Alasan", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        TipeIzinChip("Sakit", tipeIzin == "Sakit") { viewModel.onTipeChange("Sakit") }
                        TipeIzinChip("Izin", tipeIzin == "Izin") { viewModel.onTipeChange("Izin") }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Waktu Izin", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = jamMulai,
                                onValueChange = { },
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Mulai") },
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = Color.Black,
                                    disabledBorderColor = Color.Gray,
                                    disabledPlaceholderColor = Color.Gray
                                )
                            )
                            Box(modifier = Modifier.matchParentSize().clickable { showTimePickerMulai = true })
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = jamSelesai,
                                onValueChange = { },
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Selesai") },
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = Color.Black,
                                    disabledBorderColor = Color.Gray,
                                    disabledPlaceholderColor = Color.Gray
                                )
                            )
                            Box(modifier = Modifier.matchParentSize().clickable { showTimePickerSelesai = true })
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Tanggal Izin", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = tanggalMulai,
                                onValueChange = { },
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Pilih Tanggal") },
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = Color.Black,
                                    disabledBorderColor = Color.Gray,
                                    disabledPlaceholderColor = Color.Gray
                                )
                            )
                            Box(
                                modifier = Modifier.matchParentSize()
                                    .clickable { showDatePickerMulai = true })
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = tanggalSelesai,
                                onValueChange = { },
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Pilih Tanggal") },
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = Color.Black,
                                    disabledBorderColor = Color.Gray,
                                    disabledPlaceholderColor = Color.Gray
                                )
                            )
                            Box(
                                modifier = Modifier.matchParentSize()
                                    .clickable { showDatePickerSelesai = true })
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Detail Alasan", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    OutlinedTextField(
                        value = alasan,
                        onValueChange = { viewModel.onReasonChange(it) },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        placeholder = { Text("Contoh: izin ga masuk ") },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                        Text(" Tanggal: $tanggal", fontSize = 14.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {
                            viewModel.submitPermit()
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text("Kirim Pengajuan", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}