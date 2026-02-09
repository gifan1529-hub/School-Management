package com.example.schoolmanagement.UI.Screen.Admin

import androidx.compose.animation.core.copy
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.forEach
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.navigation.NavController
import com.example.schoolmanagement.DI.ToastHelper
import com.example.schoolmanagement.Domain.Model.ChartData
import com.example.schoolmanagement.UI.Component.AdminMenuCard
import com.example.schoolmanagement.UI.Component.AttendanceTrendChart
import com.example.schoolmanagement.UI.Component.StatsBox
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.HomeAdminViewModel
import com.example.schoolmanagement.getTodayDate
import org.koin.compose.viewmodel.koinViewModel
import qrgenerator.qrkitpainter.rememberQrKitPainter

@Composable
fun HomeScreenAdmin (
    navController: NavController,
    viewModel: HomeAdminViewModel = koinViewModel()
) {
    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)

    val stats by viewModel.stats.collectAsState()
    val error by viewModel.error.collectAsState()
    val refresh by viewModel.isRefreshing.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val trendData by viewModel.trendData.collectAsState()
    val errorTrend by viewModel.errorTrend.collectAsState()

    val Poppins = getPoppinsFontFamily()
    var showQrDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.loadStats()
        viewModel.loadAttendanceTrend()
    }

    if (showQrDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showQrDialog = false },
            confirmButton = {
                TextButton(onClick = { showQrDialog = false }) {
                    Text(
                        "Tutup",
                        color = Color(0xFF0066FF)
                    )
                }
            },
            title = {
                Text(
                    "QR Absensi Murid",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.Gray,
                    fontFamily = Poppins
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Minta guru scan QR ini untuk absen", fontSize = 12.sp, color = Color.Gray)
                    Spacer(Modifier.height(16.dp))

                    val painter = rememberQrKitPainter(
                        data = "GURU-HADIR-2026"
                    )

                    Image(
                        painter = painter,
                        contentDescription = "QR Code",
                        modifier = Modifier.size(250.dp)
                    )
                }
            }
        )
    }

    PullToRefreshBox(
        isRefreshing = refresh,
        onRefresh = {viewModel.refreshData()},
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(lightGray)
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
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Panel Administrator",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Selamat Datang di EduTrack Admin",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                    IconButton(
                        onClick = {navController.navigate("activity")},
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)

                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {showQrDialog = true},
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "QR",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)

                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 20.dp).offset(y = (-20).dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Data Sekolah", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(getTodayDate(), color = primaryBlue, fontSize = 12.sp)
                        }

                        Spacer(Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            StatsBox(
                                "Siswa",
                                stats.total_siswa,
                                Icons.Default.Groups,
                                Modifier.weight(1f)
                            )
                            StatsBox(
                                "Guru",
                                stats.total_guru,
                                Icons.Default.Person,
                                Modifier.weight(1f)
                            )
                            StatsBox(
                                "Kelas",
                                stats.total_kelas,
                                Icons.Default.Class,
                                Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text("Manajemen Data", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdminMenuCard(
                        "Kelola Pengguna",
                        "Tambah/Edit Siswa & Guru",
                        Icons.Default.Add,
                        primaryBlue
                    ) {
                        navController.navigate("usermanage")
                    }
                    AdminMenuCard(
                        "Jadwal Pelajaran", "Atur jam mengajar guru", Icons.Default.DateRange,
                        Color(0xFF4CAF50)
                    ) {
                        // navController.navigate("manage_schedule")
                    }
                    AdminMenuCard(
                        "Laporan Absensi",
                        "Export data kehadiran bulanan",
                        Icons.Default.Description,
                        Color(0xFFFFA500)
                    ) {
                        navController.navigate("attendancereport")
                    }
                    AdminMenuCard(
                        "Broadcast Pengumuman", "Kirim alert ke semua user", Icons.Default.Campaign,
                        Color(0xFFF44336)
                    ) {
                        navController.navigate("formalert")
                    }
                }
                Spacer(Modifier.height(40.dp))
                Text(
                    "Statistik Kehadiran Bulanan",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp, bottom = 130.dp), // 16dp padding di sini
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    AttendanceTrendChart(data = trendData)
                }
            }
        }
    }
}