package com.example.schoolmanagement.UI.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.AlertViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FormAlert (
    navController: NavController,
    viewModel: AlertViewModel = koinViewModel()
) {
    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)

    val poppins = getPoppinsFontFamily()

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("info") }
    var expanded by remember { mutableStateOf(false) }
    val typeOptions = listOf("info", "warning", "urgent")

    var selectedAudience by remember { mutableStateOf("all")  }
    var audienceExpanded by remember { mutableStateOf(false) }
    val audienceOptions = listOf("all", "teacher", "student")


//    LaunchedEffect(isSuccess) {
//        if (isSuccess) {
//            viewModel.resetSuccessState()
//            navController.popBackStack()
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(primaryBlue,
                    RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(top = 40.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "Buat Notifikasi Baru",
                    fontFamily = poppins,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Judul Notifikasi",fontFamily = poppins, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Contoh: Pengumuman Rapat") },
                shape = RoundedCornerShape(12.dp)
            )

            Text("Pesan Notifikasi",fontFamily = poppins, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = { Text("Tulis detail pengumuman di sini...") },
                shape = RoundedCornerShape(12.dp)
            )

            Text("Tipe Notifikasi",fontFamily = poppins, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Box {
                OutlinedTextField(
                    value = selectedType.replaceFirstChar { it.uppercase() },
                    onValueChange = { },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.clickable { expanded = !expanded }
                        )
                    }
                )
                Box(modifier = Modifier.matchParentSize().clickable { expanded = !expanded })

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.8f).background(Color.White)
                ) {
                    typeOptions.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.replaceFirstChar { it.uppercase() },fontFamily = poppins,) },
                            onClick = {
                                selectedType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            Text("Tujuan Notifikasi", fontFamily = poppins, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Box {
                OutlinedTextField(
                    value = if (selectedAudience == "all") "Semua (Murid & Guru)" else if (selectedAudience == "student") "Murid" else "Guru",
                    onValueChange = { },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        Icon(
                            imageVector = if (audienceExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.clickable { audienceExpanded = !audienceExpanded }
                        )
                    }
                )
                Box(modifier = Modifier.matchParentSize().clickable { audienceExpanded = !audienceExpanded })

                DropdownMenu(
                    expanded = audienceExpanded,
                    onDismissRequest = { audienceExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.8f).background(Color.White)
                ) {
                    audienceOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(if (option == "all") "Semua (Murid & Guru)" else if (option == "student") "Murid" else "Guru", fontFamily = poppins,) },
                            onClick = {
                                selectedAudience = option
                                audienceExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.createAnnouncement(title, message, selectedType, selectedAudience)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(Icons.Default.Notifications, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Kirim Notifikasi", fontFamily = poppins, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}