package com.example.schoolmanagement.UI.Screen.Admin

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.schoolmanagement.UI.Component.FilterDropdown
import com.example.schoolmanagement.UI.Component.AttendanceCard
import com.example.schoolmanagement.UI.Component.AttendanceSummaryItem
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.AttendanceReportViewModel
import com.example.schoolmanagement.getTodayDate
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceReportScreen (
    navController: NavController,
    viewModel: AttendanceReportViewModel = koinViewModel()
) {
    val poppins = getPoppinsFontFamily()

    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)

    val reportData by viewModel.reportData.collectAsState()
    val summary by viewModel.summary.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) } // 0: Siswa, 1: Guru
    var selectedClass by remember { mutableStateOf("Semua Kelas") }
    var selectedStatus by remember { mutableStateOf("Semua Status") }

    var classExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    val classOptions = listOf("Semua Kelas", "10-IPA-3", "11-IPA-2", "12-IPA-1")
    val statusOptions = listOf("Semua Status", "Present", "Late", "Absent", "Not Present Yet")

    LaunchedEffect(selectedTabIndex, selectedClass, selectedStatus) {
        val role = if (selectedTabIndex == 0) "student" else "teacher"
        viewModel.loadReport(role, selectedClass, selectedStatus)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Laporan Kehadiran", fontWeight = FontWeight.SemiBold, fontFamily = poppins) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightGray)
        ) {
             TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = primaryBlue,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = primaryBlue
                    )
                }
            ) {
                Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) {
                    Text(
                        "Siswa",
                        fontFamily = poppins,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) {
                    Text(
                        "Guru",
                        fontFamily = poppins,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Filter Kelas (Hanya muncul jika Tab Siswa dipilih)
                        if (selectedTabIndex == 0) {
                            Box(modifier = Modifier.weight(1f)) {
                                FilterDropdown(
                                    label = selectedClass,
                                    expanded = classExpanded,
                                    onExpandChange = { classExpanded = it },
                                    options = classOptions,
                                    onSelect = { selectedClass = it }
                                )
                            }
                        }

                        // Filter Status
                        Box(modifier = Modifier.weight(1f)) {
                            FilterDropdown(
                                label = selectedStatus,
                                expanded = statusExpanded,
                                onExpandChange = { statusExpanded = it },
                                options = statusOptions,
                                onSelect = { selectedStatus = it }
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AttendanceSummaryItem("Total", summary.total.toString(), primaryBlue)
                        AttendanceSummaryItem("Hadir", summary.hadir.toString(), Color(0xFF4CAF50))
                        AttendanceSummaryItem("Alpa / Belom Absen", summary.alpa.toString(), Color.Red)
                    }
                }
            }

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = primaryBlue)
            }

            Text(
                text = if (selectedTabIndex == 0) "Daftar Absensi Siswa" else "Daftar Absensi Guru",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppins,
                fontSize = 16.sp
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (reportData.isEmpty() && !isLoading) {
                    item {
                        Box(
                            Modifier.fillMaxWidth().padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tidak ada data ditemukan", fontFamily = poppins, color = Color.Gray)
                        }
                    }
                }

                items(reportData) { item ->
                    AttendanceCard(
                        name = item.name,
                        primaryColor = when (item.status) {
                            "Present" -> Color(0xFF4CAF50)
                            "Late" -> Color(0xFFFFA500)
                            "Not Present Yet" -> Color.Red
                            else -> Color.Red
                        },
                        timeIn = item.time_in,
                        staus = item.status
                    )
                }
            }
        }
    }
}