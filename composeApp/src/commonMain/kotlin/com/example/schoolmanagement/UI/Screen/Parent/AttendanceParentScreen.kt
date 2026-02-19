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
import com.example.schoolmanagement.ViewModel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceParentScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val poppins = getPoppinsFontFamily()
    val primaryBlue = Color(0xFF0066FF)

    var currentMonth by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date) }

    val attendanceHistory = emptyList<String>()

    // dummy
    val dummyAbsences = listOf(
        "Wednesday, Oct 9" to "Full day absent",
        "Friday, Oct 4" to "Full day absent",
        "Monday, Oct 7" to "Full day absent"
    )

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
                                    "83%",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    color = primaryBlue,
                                    fontFamily = poppins
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "15 / 18 days",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = poppins
                                )
                                Text(
                                    "Missed 3 days",
                                    fontSize = 12.sp,
                                    color = Red,
                                    fontFamily = poppins
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        LinearProgressIndicator(
                            progress = { 0.83f },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                            color = primaryBlue,
                            trackColor = LightGray.copy(0.3f)
                        )
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
                CalendarGrid(currentMonth, poppins, primaryBlue)

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

                    dummyAbsences.forEach { (date, desc) ->
                        AbsenceCard(date, desc, poppins)
                        Spacer(Modifier.height(12.dp))
                    }
                }
            Spacer(Modifier.height(50.dp))
        }
    }
}