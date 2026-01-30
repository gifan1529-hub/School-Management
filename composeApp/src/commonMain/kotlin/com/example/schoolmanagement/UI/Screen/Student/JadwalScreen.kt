package com.example.schoolmanagement.UI.Screen.Student

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.schoolmanagement.ViewModel.ProfileViewModel
import com.example.schoolmanagement.ViewModel.ScheduleViewModel
import com.example.schoolmanagement.getTodaDayName
import com.example.schoolmanagement.getTodayDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun JadwalScreen (
    navController: NavController,
    viewModel: ScheduleViewModel = koinViewModel()
) {
    val userClass by viewModel.userClass.collectAsState()
    val schedules by viewModel.schedules.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val primaryBlue = Color(0xFF0066FF)

    val todayName = getTodaDayName()
    val todaySchedules = schedules.filter { it.day == todayName }

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
                        text = "Jadwal",
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "$todayName, ${getTodayDate()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = primaryBlue
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = userClass,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = primaryBlue
                            )
                        }
                    } else if (todaySchedules.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tidak ada jadwal untuk hari $todayName", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(todaySchedules) { item ->
                                ScheduleItem(
                                    time = "${item.time_in.take(5)} - ${item.time_out.take(5)}",
                                    subject = item.subject,
                                    teacher = item.teacher?.name ?: "gada"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

            @Composable
            fun ScheduleItem(time: String, subject: String, teacher: String) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.width(80.dp)) {
                        Text(time.split("-")[0], fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(time.split("-")[1], color = Color.Gray, fontSize = 12.sp)
                    }

                    Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color.LightGray))

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(text = subject, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.Person, null, Modifier.size(12.dp), Color.Gray)
                            Text(text = " $teacher", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
