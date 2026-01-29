package com.example.schoolmanagement.UI.Screen.Student

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagement.ViewModel.HomeViewModel
import com.example.schoolmanagement.getTodayDate
import com.example.schoolmanagement.isLate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val isAlreadyAbsen by viewModel.isAlreadyAbsen.collectAsState()
    val isLoadingAbsen by viewModel.isLoadingAbsen.collectAsState()

    val userName by viewModel.userName.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val userNis by viewModel.userNIS.collectAsState()
    val userKelas by viewModel.userClass.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()

    val telat = isLate()

    LaunchedEffect(userName) {
        println("DEBUG: Nama : $userName")
        println("DEBUG: absen : $isAlreadyAbsen")
    }

    val primaryBlue = Color(0xFF0066FF)
    val lightBlue = Color(0xFFE3F2FD)
    val lightGreen = Color(0xFFE8F5E9)
    val lightRed = Color(0xFFFFEBEE)

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
            Row {
                Column {
                    Text(
                        text = "Halo, $userName!",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Kelas ${userKelas} • NISN: ${userNis}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Role : $userRole",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .offset(y = (-20).dp), // ngebuat card agak naik nimpa header
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Ringkasan Kehadiran",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(end = 5.dp)
                    )
                    Surface(
                        color = primaryBlue,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = getTodayDate(),
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AttendanceBox(
                        label = "Hadir",
                        value = "20",
                        color = Color(0xFF4CAF50),
                        bgColor = lightGreen,
                        modifier = Modifier.weight(1f)
                    )
                    AttendanceBox(
                        label = "Absen",
                        value = "2",
                        color = Color(0xFFF44336),
                        bgColor = lightRed,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Persentase Kehadiran", fontSize = 14.sp, color = Color.Gray)

                    Surface(
                        color = when {
                            isAlreadyAbsen -> {
                                if (telat) Color(0xFFFFF3E0) else Color(0xFFE8F5E9)
                            }

                            else -> Color(0xFFFFEBEE)
                        },
                        border = BorderStroke(
                            1.dp,
                            when {
                                isAlreadyAbsen -> {
                                    if (telat) Color(0xFFFFA500) else Color(0xFF4CAF50)
                                }
                                else -> Color.Red
                            }
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(end = 5.dp),
                        onClick = {
                            if (!isAlreadyAbsen && !isLoadingAbsen) {
                                navController.navigate("scanner")
                            }
                        }
                    ) {
                        Text(
                            fontSize = 12.sp,
                            color = Color.Black,
                            text = when {
                                // kalo udah absen, bakal ngecek si user absenya talat atau ngga
                                isAlreadyAbsen -> {
                                    if (telat) "Telat Absen" else "Sudah Absen"
                                }
                                // kalo belom absen by default nya absen sekaragn
                                else -> "Absen Sekarang"
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = 0.9f,
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = primaryBlue,
                    trackColor = Color(0xFFE0E0E0),
                    strokeCap = StrokeCap.Round
                )
            }
        }

        Text(
            "Menu Cepat",
            modifier = Modifier.padding(horizontal = 24.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MenuCard(
                    "Jadwal",
                    Icons.Default.DateRange,
                    Modifier
                        .weight(1f),
                    onClick = { navController.navigate("jadwal") }
                )
                MenuCard(
                    "Tugas",
                    Icons.Default.List,
                    Modifier
                        .weight(1f),
                    onClick = { navController.navigate("tugas") }
                    )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MenuCard(
                    "Nilai",
                    Icons.Default.Star,
                    Modifier.weight(1f),
                    onClick = { }
                )
                MenuCard(
                    "Izin",
                    Icons.Default.Email,
                    Modifier.weight(1f),
                    onClick = {navController.navigate("izin") }
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun AttendanceBox(label: String, value: String, color: Color, bgColor: Color, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = bgColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 14.sp, color = color.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun MenuCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF0066FF),
                modifier = Modifier.size(28.dp).background(Color(0xFFE3F2FD),
                    RoundedCornerShape(8.dp)).padding(4.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
    }
}
