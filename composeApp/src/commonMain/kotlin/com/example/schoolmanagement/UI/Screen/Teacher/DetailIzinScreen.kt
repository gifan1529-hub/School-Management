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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.schoolmanagement.UI.Component.CustomToast
import com.example.schoolmanagement.UI.Component.InfoCard
import com.example.schoolmanagement.UI.Component.ToastType
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

    var isImageVisible by remember { mutableStateOf(false)}

    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }

    val poppins = getPoppinsFontFamily()

    val guruHistory by viewModel.guruPermitHistory.collectAsState()
    val permitHistory by viewModel.muridPermitHistory.collectAsState()

    // buat nyari id nya ada di mana aja
    val detail = remember(permitHistory, guruHistory) {
        permitHistory.find { it.id == permitId }
            ?: guruHistory.find { it.id == permitId }
    }

    LaunchedEffect(detail) {
        println("DEBUG FOTO: URL-nya adalah -> ${detail?.image}")
    }

    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val isUpdateSuccess by viewModel.isUpdateSuccess.collectAsState()

    LaunchedEffect(Unit) {
        if (permitHistory.isEmpty()) {
            viewModel.loadPermitHistory()
        }
    }

    LaunchedEffect(isUpdateSuccess) {
        if (isUpdateSuccess) {
            navController.previousBackStackEntry?.savedStateHandle?.set("toast_message", "Berhasil Update Permohonan Izin")
            navController.previousBackStackEntry?.savedStateHandle?.set("toast_type", ToastType.SUCCESS)
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    "Detail Perizinan",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppins
                ) },
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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(lightGray)
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = primaryBlue
                )
            } else if (detail == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Data tidak ditemukan", fontFamily = poppins)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(lightGray)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
            ) {
                InfoCard(
                    title = "Informasi Murid",
                    icon = Icons.Default.Person,
                    content = {
                        Text(
                            detail.user?.name ?: "Siswa",
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                        Text(
                            "Kelas: ${detail.user?.`class` ?: "-"}",
                            fontFamily = poppins,
                            color = Color.Gray
                        )
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
                        Text(detail.reason ?: "", fontFamily = poppins, lineHeight = 20.sp)
                    }
                )

                Spacer(Modifier.height(16.dp))

                if (!detail.image.isNullOrBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Bukti Lampiran",
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        TextButton(onClick = { isImageVisible = !isImageVisible }) {
                            Text(
                                if (isImageVisible) "Sembunyikan" else "Lihat Foto",
                                color = primaryBlue,
                                fontFamily = poppins,
                                fontSize = 12.sp
                            )
                        }
                    }
                    if (isImageVisible) {
                        AsyncImage(
                            model = detail.image,
                            contentDescription = "Bukti Izin",
                            onError = { println("DEBUG FOTO: Gagal load karena ${it.result.throwable}") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Tidak ada lampiran foto", color = Color.Gray, fontFamily = poppins, fontSize = 12.sp)
                    }
                }

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
                            },
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            Text("Setujui",fontFamily = poppins, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                }
            }
            CustomToast(
                message = toastMessage,
                type = toastType,
                isVisible = showToast,
                onDismiss = { showToast = false }
            )
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