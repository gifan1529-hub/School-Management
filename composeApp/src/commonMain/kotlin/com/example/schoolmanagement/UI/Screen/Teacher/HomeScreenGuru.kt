package com.example.schoolmanagement.UI.Screen.Teacher

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagement.UI.Screen.Student.AttendanceBox
import com.example.schoolmanagement.UI.Screen.Student.MenuCard
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.ActivityLogViewModel
import com.example.schoolmanagement.ViewModel.HomeTeacherViewModel
import com.example.schoolmanagement.ViewModel.HomeViewModel
import com.example.schoolmanagement.ViewModel.SignIn
import com.example.schoolmanagement.getAttendanceStatus
import com.example.schoolmanagement.getTodayDate
import com.example.schoolmanagement.isLate
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import qrgenerator.qrkitpainter.rememberQrKitPainter

@Composable
fun HomeScreenGuru (
    navController: NavHostController,
    viewModel: HomeTeacherViewModel = koinViewModel (),
    fcm: SignIn = koinViewModel(),
    logViewModel: ActivityLogViewModel = koinViewModel()
) {
    val poppins = getPoppinsFontFamily()

    val unreadCount by logViewModel.unreadCount.collectAsState()

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isAlreadyAbsen by viewModel.isAlreadyAbsen.collectAsState()
    val isLoadingAbsen by viewModel.isLoadingAbsen.collectAsState()

    val userName by viewModel.userName.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val userKelas by viewModel.userClass.collectAsState()

    val scope = rememberCoroutineScope()

    val countHadir by viewModel.countHadir.collectAsState()
    val countTelat by viewModel.countTelat.collectAsState()
    val countAbsen by viewModel.countAbsen.collectAsState()
    val todayStatus by viewModel.todayStatus.collectAsState()

    var showQrDialog by remember { mutableStateOf(false) }

    val telat = isLate()

    val total = (countHadir.toInt() + countTelat.toInt() + countAbsen.toInt())
    val progressValue = if (total > 0) countHadir.toFloat() / total.toFloat() else 0f

    val Poppins = getPoppinsFontFamily()

    val primaryBlue = Color(0xFF0066FF)
    val lightBlue = Color(0xFFE3F2FD)
    val lightGreen = Color(0xFFE8F5E9)
    val lightRed = Color(0xFFFFEBEE)
    val lightYellow = Color(0xFFFFF3E0)

    if (showQrDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showQrDialog = false },
            confirmButton = {
                TextButton(onClick = { showQrDialog = false }) {
                    Text(
                        "Tutup",
                        fontFamily = poppins,
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
                    Text("Minta murid scan QR ini untuk absen",fontFamily = poppins, fontSize = 12.sp, color = Color.Gray)
                    Spacer(Modifier.height(16.dp))

                      val painter = rememberQrKitPainter(
                        data = "HADIR-QR-2026"
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

    LaunchedEffect(Unit) {
        logViewModel.loadUnreadCount()
        scope.launch {
            try {
                println("DEBUG FCM: Mencoba ambil token")
                val helper = com.example.schoolmanagement.DI.FcmHelper()
                val token = helper.getFcmToken()
                if (token != null) {
                    println("DEBUG FCM: Token dapet! -> $token")
                    fcm.sendFcmToken(token)
                } else {
                    println("DEBUG FCM: Token NULL! Cek google-services.json")
                }
            } catch (e: Exception) {
                println("DEBUG FCM CRASH: ${e.message}")
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {viewModel.refreshData()},
        modifier = Modifier
            .fillMaxSize()
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
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Halo, $userName!",
                            color = Color.White,
                            fontFamily = poppins,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Wali kelas : ${userKelas} ",
                            color = Color.White.copy(alpha = 0.8f),
                            fontFamily = poppins,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Role : $userRole",
                            color = Color.White.copy(alpha = 0.8f),
                            fontFamily = poppins,
                            fontSize = 14.sp
                        )
                    }
                    Box {
                        IconButton(
                            onClick = {
                                logViewModel.markAsRead()
                                navController.navigate("activity")
                            },
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
                        if (unreadCount > 0) {
                            Surface(
                                color = Color.Red,
                                shape = CircleShape,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(22.dp)
                                    .offset(x = 2.dp, y = (-2).dp),
                                border = BorderStroke(
                                    1.5.dp,
                                    primaryBlue
                                )
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = if (unreadCount > 9) "9+" else unreadCount.toString(),
                                        color = Color.White,
                                        fontFamily = poppins,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box {
                        IconButton(
                            onClick = { showQrDialog = true },
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
            }

            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-20).dp), // ngebuat card agak naik nimpa header
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                onClick = { navController.navigate("absenHistory") }
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Ringkasan Kehadiran",
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold,
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
                                fontFamily = poppins,
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
                            value = countHadir,
                            color = Color(0xFF4CAF50),
                            bgColor = lightGreen,
                            modifier = Modifier.weight(1f)
                        )
                        AttendanceBox(
                            label = "Telat",
                            value = countTelat,
                            color = Color(0xFFFFA500),
                            bgColor = lightYellow,
                            modifier = Modifier.weight(1f)
                        )
                        AttendanceBox(
                            label = "Absen",
                            value = countAbsen,
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
                        Text("Persentase Kehadiran", fontSize = 14.sp, fontFamily = poppins, color = Color.Gray)

                        Surface(
                            color = when {
                                isAlreadyAbsen && todayStatus == "Absent" -> Color(0xFFFFEBEE)
                                isAlreadyAbsen -> {
                                    if (todayStatus == "Late") Color(0xFFFFF3E0) else Color(
                                        0xFFE8F5E9
                                    )
                                }

                                telat -> Color(0xFFFFF3E0)
                                else -> Color(0xFFFFEBEE)
                            },
                            border = BorderStroke(
                                1.dp,
                                when {
                                    isAlreadyAbsen && todayStatus == "Absent" -> Color.Red
                                    isAlreadyAbsen -> {
                                        if (todayStatus == "Late") Color(0xFFFFA500) else Color(
                                            0xFF4CAF50
                                        )
                                    }
                                    telat -> Color(0xFFFFA500)
                                    else -> Color.Red
                                }
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .padding(end = 5.dp),
                            onClick = {
                                if (!isAlreadyAbsen && todayStatus != "Absent" && !isLoadingAbsen) {
                                    navController.navigate("scanner")
                                }
                            }
                        ) {
                            Text(
                                fontSize = 12.sp,
                                fontFamily = poppins,
                                color = when {
                                    isAlreadyAbsen && todayStatus == "Absent" -> Color.Red
                                    isAlreadyAbsen -> {
                                        if (todayStatus == "Late") Color(0xFFFFA500) else Color.Red
                                    }
                                    telat -> Color(0xFFFFA500)
                                    else -> Color.Red
                                },
                                text = when {
                                    isAlreadyAbsen && todayStatus == "Absent" -> "Sudah Alpha"
                                    getAttendanceStatus() == "Absent" -> "Sudah Alpa"
                                    // kalo udah absen, bakal ngecek si user absenya talat atau ngga
                                    isAlreadyAbsen -> {
                                        if (todayStatus == "Late") "Telat Absen" else "Sudah Absen"
                                    }

                                    telat -> "Telat Absen"
                                    // kalo belom absen by default nya absen sekaragn
                                    else -> "Absen Sekarang"
                                },
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = progressValue,
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = primaryBlue,
                        trackColor = Color(0xFFE0E0E0),
                        strokeCap = StrokeCap.Round
                    )
                }
            }
            Text(
                "Quick Actions",
                fontFamily = poppins,
                modifier = Modifier.padding(horizontal = 24.dp),
                fontWeight = FontWeight.SemiBold,
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
                        "Mark Attendance",
                        Icons.Default.DateRange,
                        Modifier
                            .weight(1f),
                        onClick = { navController.navigate("siswahadir") }
                    )
                    MenuCard(
                        "Homework",
                        Icons.Default.List,
                        Modifier
                            .weight(1f),
                        onClick = { navController.navigate("homework") }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MenuCard(
                        "Jadwal Mengajar",
                        Icons.Default.Star,
                        Modifier.weight(1f),
                        onClick = {navController.navigate("jadwalngajar")  }
                    )
                    MenuCard(
                        "Izin",
                        Icons.Default.Email,
                        Modifier.weight(1f),
                        onClick = {navController.navigate("izinguru") }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MenuCard(
                        "Materi",
                        Icons.Default.AutoStories,
                        Modifier.weight(1f),
                        onClick = {navController.navigate("materigurus")  }
                    )
                    MenuCard(
                        "Pelanggaran",
                        Icons.Default.Gavel,
                        Modifier.weight(1f),
                        onClick = {navController.navigate("pelanggaran")  }
                    )
                }
            }

            Spacer(modifier = Modifier.height(170.dp))
        }
    }
}