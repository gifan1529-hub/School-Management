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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.example.schoolmanagement.DI.readFileBytes
import com.example.schoolmanagement.UI.Component.CustomToast
import com.example.schoolmanagement.UI.Component.InfoCard
import com.example.schoolmanagement.UI.Component.ToastType
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.HomeWorkViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailHomeWork (
    navController: NavController,
    viewModel: HomeWorkViewModel = koinViewModel(),
    homeworkId: Int
) {
    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)

    val poppins = getPoppinsFontFamily()

    var showFilePicker by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val homeworkList by viewModel.homeworkList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSubmit by viewModel.isSubmitSuccess.collectAsState()

    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }

    val detail = remember(homeworkList, homeworkId) {
        homeworkList.find { it.id == homeworkId }
    }

    FilePicker(show = showFilePicker, fileExtensions = listOf("pdf", "jpg", "png", "doc", "docx")) { platformFile ->
        showFilePicker = false
        platformFile?.let { file ->
            scope.launch {
                val bytes = file.readFileBytes()
                if (bytes != null) {
                    val fileName = file.path.split("/").last().split("\\").last()
                    viewModel.submitHomework(homeworkId, bytes, fileName)
                } else {
                    // Tampilkan pesan error jika file gagal dibaca
                }
            }
        }
    }

    LaunchedEffect(homeworkList) {
        if (homeworkList.isEmpty()) {
            viewModel.loadHomeworks()
        }
    }

    LaunchedEffect(isSubmit){
        if (isSubmit) {
            toastMessage = "Berhasil submit tugas"
            toastType = ToastType.SUCCESS
            showToast = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Tugas", fontWeight = FontWeight.SemiBold, fontFamily = poppins,) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                onClick = {
                    navController.navigate("discussion/$homeworkId")
                },
                containerColor = primaryBlue,
                contentColor = Color.White,
                shape = androidx.compose.foundation.shape.CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "Diskusi",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightGray)
        ) {
            if (isLoading && detail == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (detail == null) {
                Text(
                    "Tugas tidak ditemukan (ID: $homeworkId)",
                    modifier = Modifier.align(Alignment.Center),
                    fontFamily = poppins,
                    color = Color.Gray
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoCard(
                        icon = Icons.Default.Title,
                        title = "Mata Pelajaran: ${detail.subject}",
                        content = {
                            Text(detail.title, fontWeight = FontWeight.SemiBold, fontFamily = poppins, fontSize = 20.sp)
                        }
                    )

                    InfoCard(
                        icon = Icons.Default.CalendarToday,
                        title = "Informasi Tugas",
                        content = {
                            DetailRow(label = "Batas Waktu", value = detail.deadline)
                            DetailRow(label = "Untuk Kelas", value = detail.`class`)
                            DetailRow(
                                label = "Diberikan oleh",
                                value = detail.teacher?.name ?: "-"
                            )
                        }
                    )

                    InfoCard(
                        icon = Icons.Default.Description,
                        title = "Deskripsi & Instruksi",
                        content = {
                            Text(
                                detail.description,
                                fontFamily = poppins,
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                color = Color.DarkGray
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    when (detail.status) {
                        "Done" -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Tugas Sudah Dikumpulkan ✅", color = Color(0xFF4CAF50), fontWeight = FontWeight.SemiBold, fontFamily = poppins,)
                                if (detail.your_grade != null) {
                                    Spacer(Modifier.height(8.dp))
                                    Text("Nilai Kamu:", fontSize = 12.sp, fontFamily = poppins, color = Color.Gray)
                                    Text("${detail.your_grade ?: "-"}",fontFamily = poppins, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = primaryBlue)
                                } else {
                                    Text("Menunggu Penilaian Guru",fontFamily = poppins, fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                        }
                        "Expired" -> {
                            Surface(
                                color = Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Batas waktu pengumpulan telah berakhir",
                                    color = Color.Red,
                                    fontFamily = poppins,
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        else -> {
                            Button(
                                onClick = { showFilePicker = true },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                                enabled = !isLoading
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                                } else {
                                    Text("Pilih File & Kumpulkan", fontFamily = poppins, fontWeight = FontWeight.Bold)
                                }
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
private fun DetailRow(label: String, value: String) {
    val poppins = getPoppinsFontFamily()
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontFamily = poppins,)
        Text(value, fontWeight = FontWeight.Normal,fontFamily = poppins, color = Color.Black)
    }
}