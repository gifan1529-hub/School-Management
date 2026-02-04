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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Title
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.schoolmanagement.UI.Component.InfoCard
import com.example.schoolmanagement.ViewModel.HomeWorkViewModel
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

    val homeworkList by viewModel.homeworkList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val detail = remember(homeworkList, homeworkId) {
        homeworkList.find { it.id == homeworkId }
    }

    LaunchedEffect(homeworkList) {
        if (homeworkList.isEmpty()) {
            viewModel.loadHomeworks()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Tugas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
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
                            Text(detail.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                color = Color.DarkGray
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* upload jawaban */ },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                    ) {
                        Text("Kumpulkan Jawaban", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Normal, color = Color.Black)
    }
}