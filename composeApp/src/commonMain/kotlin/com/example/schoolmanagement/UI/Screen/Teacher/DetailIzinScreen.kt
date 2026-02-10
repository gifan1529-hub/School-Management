package com.example.schoolmanagement.UI.Screen.Teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.navigation.NavHostController
import com.example.schoolmanagement.UI.Component.InfoCard
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.PermitViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailIzinScreen (
    navController: NavHostController,
    permitId: Int,
    viewModel: PermitViewModel = koinViewModel()
) {
    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)

    val poppins = getPoppinsFontFamily()

    val permitHistory by viewModel.muridPermitHistory.collectAsState()
    val detail = permitHistory.find { it.id == permitId }

    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    LaunchedEffect(Unit) {
        if (permitHistory.isEmpty()) {
            viewModel.loadPermitHistory()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Perizinan", fontWeight = FontWeight.SemiBold, fontFamily = poppins,) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(lightGray)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = primaryBlue
                )
            } else if (detail == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Data tidak ditemukan", fontFamily = poppins,)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(lightGray)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 120.dp, start = 20.dp, end = 20.dp)
            ) {
                InfoCard(
                    title = "Informasi Murid",
                    icon = Icons.Default.Person,
                    content = {
                        Text(detail.user?.name ?: "Siswa",fontFamily = poppins, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Text("Kelas: ${detail.user?.`class` ?: "-"}",fontFamily = poppins, color = Color.Gray)
                    }
                )

                Spacer(Modifier.height(16.dp))

                InfoCard(
                    title = "Detail Izin",
                    icon = Icons.Default.CalendarToday,
                    content = {
                        DetailRow("Tipe", detail.type)
                        DetailRow("Status", detail.status)
                        DetailRow("Jam Mulai", detail.timeIn)
                        DetailRow("Jam Selesai", detail.timeOut)
                        DetailRow("Tanggal Mulai", detail.start_date)
                        DetailRow("Tanggal Selesai", detail.end_date)
                    }
                )

                Spacer(Modifier.height(16.dp))

                InfoCard(
                    title = "Alasan",
                    icon = Icons.Default.Schedule,
                    content = {
                        Text(detail.reason ?: "",fontFamily = poppins, lineHeight = 20.sp)
                    }
                )

                Spacer(Modifier.height(32.dp))

                // Tombol Aksi (Hanya muncul jika status masih PENDING)
                if (detail.status == "pending") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.updatePermitStatus(detail.id, "rejected")
                                navController.popBackStack()
                            },
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            Text("Tolak",fontFamily = poppins, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = {
                                viewModel.updatePermitStatus(detail.id, "approved")
                                navController.popBackStack()
                            },
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            Text("Setujui",fontFamily = poppins, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    val poppins = getPoppinsFontFamily()
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label,fontFamily = poppins, color = Color.Gray)
        Text(value,fontFamily = poppins, fontWeight = FontWeight.Normal)
    }
}