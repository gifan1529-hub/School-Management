package com.example.schoolmanagement.UI.Screen.Teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.navigation.NavHostController
import com.example.schoolmanagement.UI.Component.ViolationCard
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.ViolationViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPelanggaranMurid(
    navController: NavHostController,
    studentName: String,
    studentId: Int,
    viewModel: ViolationViewModel = koinViewModel()
) {
    val poppins = getPoppinsFontFamily()
    val primaryBlue = Color(0xFF0066FF)
    val allViolations by viewModel.violations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val studentViolations = remember(allViolations) {
        allViolations.filter { it.student_id == studentId }
    }

    LaunchedEffect(Unit){
        viewModel.loadViolations()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(studentName, fontFamily = poppins, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("ID: $studentId", fontFamily = poppins, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryBlue, titleContentColor = Color.White)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("formpelanggaran/$studentId/$studentName") },
                containerColor = Color.Red,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Input Pelanggaran", fontFamily = poppins, fontWeight = FontWeight.Medium)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFFF5F7FA))) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = primaryBlue,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(40.dp)
                    )
                }
            } else if (studentViolations.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SentimentVerySatisfied,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Murid ini tidak melanggar apapun",
                        fontFamily = poppins,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            "Riwayat Pelanggaran",
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    items(allViolations) { item ->
                        ViolationCard(item, poppins)
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}