package com.example.schoolmanagement.UI.Screen.Parent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagement.UI.Component.AbsenceCard
import com.example.schoolmanagement.UI.Component.CalendarGrid
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.AttendanceParentViewModel
import com.example.schoolmanagement.ViewModel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceParentScreen(
    navController: NavHostController,
    viewModel: AttendanceParentViewModel = koinViewModel()
) {
    val poppins = getPoppinsFontFamily()
    val primaryBlue = Color(0xFF0066FF)

    var currentMonth by remember {
        mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    }

    val attendanceData by viewModel.state.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val progress = attendanceData?.summary?.percentage?.replace("%", "")?.toFloatOrNull()?.div(100f) ?: 0f

    LaunchedEffect(currentMonth) {
        viewModel.loadAttendance(month = currentMonth.month, year =  currentMonth.year)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .verticalScroll(rememberScrollState())
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
                Column {
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
                            text = "Attendance",
                            fontFamily = poppins,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).offset(y = (-20).dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    if (isLoading && attendanceData == null) {
                        Box(
                            Modifier.fillMaxWidth().height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = primaryBlue)
                        }
                    } else {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        "This Month",
                                        fontSize = 12.sp,
                                        color = Gray,
                                        fontFamily = poppins
                                    )
                                    Text(
                                        attendanceData?.summary?.percentage ?: "0%",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Black,
                                        color = primaryBlue,
                                        fontFamily = poppins
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "${attendanceData?.summary?.present_count ?: 0} / ${attendanceData?.summary?.total_school_days ?: 0} days",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = poppins
                                    )
                                    Text(
                                        "Missed ${attendanceData?.summary?.missed_count ?: 0} days",
                                        fontSize = 12.sp,
                                        color = Red,
                                        fontFamily = poppins
                                    )
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                                color = primaryBlue,
                                trackColor = LightGray.copy(0.3f)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        currentMonth = currentMonth.minus(1, DateTimeUnit.MONTH)
                    }) {
                        Icon(Icons.Default.ChevronLeft, null)
                    }
                    Text(
                        "${currentMonth.month.name} ${currentMonth.year}",
                        fontFamily = poppins, fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = {
                        currentMonth = currentMonth.plus(1, DateTimeUnit.MONTH)
                    }) {
                        Icon(Icons.Default.ChevronRight, null)
                    }
                }

            Box(modifier = Modifier.padding(horizontal = 4.dp).offset(y = (-10).dp)) {
                CalendarGrid(
                    date = currentMonth,
                    font = poppins,
                    primaryColor = primaryBlue,
                    attendanceData = attendanceData?.calendar ?: emptyList()
                )
            }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Recent Absences",
                        fontFamily = poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF2D3142)
                    )

                    Spacer(Modifier.height(16.dp))

                    if (attendanceData?.recent_absences?.isEmpty() == true) {
                        Text("Tidak ada riwayat absen bulan ini", color = Color.Gray, fontSize = 12.sp)
                    } else {
                        attendanceData?.recent_absences?.forEach { absence ->
                            AbsenceCard(
                                date = absence.date_formatted,
                                desc = absence.note,
                                font = poppins
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            Spacer(Modifier.height(50.dp))
        }
    }
}