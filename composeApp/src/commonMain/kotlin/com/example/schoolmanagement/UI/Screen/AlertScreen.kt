package com.example.schoolmanagement.UI.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.schoolmanagement.Domain.Model.AlertData
import com.example.schoolmanagement.UI.Component.AlertCard
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.AlertViewModel
import com.example.schoolmanagement.ViewModel.HomeViewModel
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun AlertScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
    alertViewModel: AlertViewModel = koinViewModel()
) {
    val currentMoment = Clock.System.now()
    val now = currentMoment.toLocalDateTime(currentSystemDefault())
    val isMorningTime = now.hour in 6..16

    val poppins = getPoppinsFontFamily()

    val alerts by alertViewModel.alerts.collectAsState()
    val isLoading by alertViewModel.isLoading.collectAsState()

    val isAlreadyAbsen by viewModel.isAlreadyAbsen.collectAsState()
    val userRole by viewModel.userRole.collectAsState()

    // cuman dummy aja. ntni kalo udah ada api nnya bakal ngambil dari api
//    val alertsFromGuru = listOf(
//        AlertData("Info Rapat", "Kumpul di aula jam 8", "07:00", "info"),
//        AlertData("Tugas Baru", "Cek menu tugas untuk Matematika", "Kemarin", "warning"),
//        AlertData("Libur Dadakan", "Besok sekolah libur karena renovasi", "2 jam lalu", "urgent")
//    )
    LaunchedEffect(Unit) {
        alertViewModel.loadAnnouncements()
    }

    val primaryBlue = Color(0xFF0066FF)
    Scaffold(
        floatingActionButton = {
            //  tombol hanya muncul untuk guru dan atmin
            if (userRole.lowercase() == "teacher" || userRole.lowercase() == "admin") {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("formalert")
                    },
                    containerColor = primaryBlue,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(bottom = 60.dp, end = 20.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Alert")
                }
            }
        }
    ) { _ ->
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
                Text(
                    text = "Alerts",
                    color = Color.White,
                    fontFamily = poppins,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-20).dp)
                    .fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White), // 2. Pastikan warna putih (bukan transparent)
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                if (isLoading && alerts.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = primaryBlue)
                    }
                } else if (!isLoading && alerts.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Belum ada notifikasi",fontFamily = poppins, color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (isMorningTime && !isAlreadyAbsen && userRole.lowercase() == "student") {
                            item {
                                AlertCard(
                                    title = "Peringatan Absensi",
                                    message = "Kamu belum melakukan absensi pagi ini!",
                                    time = "Penting",
                                    type = "urgent",
                                    onClick = { navController.navigate("scanner") }
                                )
                            }
                        }

                        items(alerts) { alert ->
                            AlertCard(
                                title = alert.title,
                                message = alert.message,
                                time = alert.created_at.take(10),
                                type = alert.type,
                                onClick = { /* detail alert */ }
                            )
                        }

                        item { Spacer(Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}