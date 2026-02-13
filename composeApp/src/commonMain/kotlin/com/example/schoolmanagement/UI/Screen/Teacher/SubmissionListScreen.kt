package com.example.schoolmanagement.UI.Screen.Teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.schoolmanagement.DI.ToastHelper
import com.example.schoolmanagement.DI.rememberLauncher
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.HomeWorkViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmissionListScreen (
    navController: NavController,
    homeworkId: Int,
    viewModel: HomeWorkViewModel = koinViewModel()
) {
    val poppins  = getPoppinsFontFamily()

    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)
    val launcher = rememberLauncher()

    var showGradeDialog by remember { mutableStateOf(false) }
    var selectedSubmissionId by remember { mutableStateOf(0) }
    var gradeInput by remember { mutableStateOf("") }

    val detail by viewModel.selectedHomeworkDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (showGradeDialog){
        AlertDialog(
            onDismissRequest = { showGradeDialog = false },
            title = { Text("Beri Nilai Murid", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Masukkan nilai antara 0 - 100",fontFamily = poppins, fontSize = 12.sp, color = Color.Gray)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = gradeInput,
                        onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 3) gradeInput = it },
                        label = { Text("Nilai",fontFamily = poppins,) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val grade = gradeInput.toIntOrNull()
                        if (grade != null && grade in 0..100) {
                            viewModel.giveGrade(selectedSubmissionId!!, grade, homeworkId)
                            showGradeDialog = false
                        } else {
                            ToastHelper().Toast("Nilai harus antara 0 - 100")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0066FF))
                ) { Text("Simpan", fontFamily = poppins,) }
            },
            dismissButton = {
                TextButton(onClick = { showGradeDialog = false }) { Text("Batal",fontFamily = poppins,) }
            }
        )
    }

    LaunchedEffect(homeworkId) {
        viewModel.loadHomeworkDetail(homeworkId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Pengumpulan",fontFamily = poppins, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = primaryBlue)
            } else if (detail == null) {
                Text("Gagal memuat data",fontFamily = poppins, modifier = Modifier.align(Alignment.Center))
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = detail?.subject ?: "",fontFamily = poppins, color = primaryBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = detail?.title ?: "",fontFamily = poppins, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(text = "Target Kelas: ${detail?.`class`}",fontFamily = poppins, fontSize = 13.sp, color = Color.Gray)
                        }
                    }

                    Text(
                        text = "Murid yang sudah mengumpulkan (${detail?.submissions?.size ?: 0})",
                        fontFamily = poppins,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val submissions = detail?.submissions ?: emptyList()

                        if (submissions.isEmpty()) {
                            item {
                                Box(Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                                    Text("Belum ada pengumpulan dari murid.", color = Color.Gray, fontFamily = poppins,)
                                }
                            }
                        }

                        items(submissions) { sub ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(40.dp).background(primaryBlue.copy(0.1f),
                                            RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Description, null, tint = primaryBlue)
                                    }

                                    Spacer(Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = sub.student?.name ?: "Unknown",
                                            fontFamily = poppins,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            text = if (sub.grade != null) "Nilai: ${sub.grade}" else "Belum Dinilai",
                                            fontFamily = poppins,
                                            color = if (sub.grade != null) Color(0xFF4CAF50) else Color.Red,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(text = "Dikirim: ${sub.created_at.take(16)}", fontFamily = poppins, fontSize = 12.sp, color = Color.Gray)
                                    }

                                    IconButton(onClick = {
                                        selectedSubmissionId = sub.id
                                        gradeInput = sub.grade?.toString() ?: ""
                                        showGradeDialog = true
                                    }) {
                                        Icon(Icons.Default.Star, "Beri Nilai", tint = Color(0xFFFFA500))
                                    }

                                    IconButton(onClick = {
                                        val baseUrl = "https://jaquelyn-unrefreshed-miss.ngrok-free.dev/storage/"
                                        val fileUrl = baseUrl + sub.file

                                        launcher.openUrl(fileUrl)
                                    }) {
                                        Icon(Icons.Default.OpenInNew, "Lihat", tint = primaryBlue)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}