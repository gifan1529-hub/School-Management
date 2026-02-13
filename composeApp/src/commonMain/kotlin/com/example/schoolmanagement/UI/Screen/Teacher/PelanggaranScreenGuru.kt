package com.example.schoolmanagement.UI.Screen.Teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.ViolationViewModel
import org.koin.compose.viewmodel.koinViewModel

data class DummyStudent(val id: Int, val name: String, val nisn: String)
val dummyStudents = listOf(
    DummyStudent(1, "Asep Surasep", "123123121"),
    DummyStudent(2, "Budi Martabak", "123123122"),
    DummyStudent(3, "Cici Paricici", "123123123"),
    DummyStudent(4, "Dedi Corbuzier", "123123124")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PelanggaranScreenGuru(
    navController: NavHostController,
    viewModel: ViolationViewModel = koinViewModel()
) {
    val poppins = getPoppinsFontFamily()
    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)
    val students by viewModel.studentList.collectAsState()

    LaunchedEffect(Unit){
        viewModel.loadStudents()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Poin Pelanggaran", fontFamily = poppins, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F7FA))
        ) {
            item {
                Text(
                    "Pilih Murid Untuk Kelola Point",
                    fontFamily = poppins,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            items(students) { student ->
                Card (
                    modifier = Modifier.fillMaxWidth().clickable {
                        navController.navigate("listpelanggaran/${student.id}/${student.name}")
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ){
                    Row (
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Surface (
                            modifier = Modifier.size(45.dp),
                            shape = CircleShape,
                            color = primaryBlue.copy(alpha = 0.1f)
                        ){
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, null, tint = primaryBlue)
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(student.name, fontFamily = poppins, fontWeight = FontWeight.Bold)
                            Text("NISN: ${student.nisn}", fontSize = 12.sp, color = Color.Gray)
                        }
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
                    }

                }

            }
        }
    }
}