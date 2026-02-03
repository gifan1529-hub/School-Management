package com.example.schoolmanagement.UI.Screen.Teacher

import androidx.compose.animation.core.copy
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import com.example.schoolmanagement.Domain.Model.HomeworkData
import com.example.schoolmanagement.UI.Component.HomeworkTeacherItem

@Composable
fun HomeWorkScreen (
    navController: NavHostController,
) {
    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)

    var taskTitle by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedClass by remember { mutableStateOf("Pilih Kelas") }
    val classOptions = listOf("12-IPA-1", "12-IPA-2", "12-IPS-1", "12-IPS-2")
    var deadLine by remember { mutableStateOf("Besok, 23:59") }

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
                .padding(top = 40.dp, bottom = 24.dp, start = 16.dp, end = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
                Text(
                    "Beri Tugas Murid",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Buat Tugas Baru", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = taskTitle,
                            onValueChange = { taskTitle = it },
                            label = { Text("Judul Tugas (Contoh: Latihan Bab 3)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = selectedClass,
                                    onValueChange = { },
                                    readOnly = true, // Supaya tidak bisa diketik manual
                                    label = { Text("Pilih Kelas") },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    trailingIcon = {
                                        IconButton(onClick = { expanded = !expanded }) {
                                            Icon(
                                                imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                )

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .background(Color.White)
                                        .fillMaxWidth(0.4f)
                                ) {
                                    classOptions.forEach { selectionOption ->
                                        DropdownMenuItem(
                                            text = { Text(selectionOption) },
                                            onClick = {
                                                selectedClass = selectionOption
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clickable { expanded = !expanded }
                                )
                            }
                            OutlinedTextField(
                                value = deadLine,
                                onValueChange = { deadLine = it },
                                label = { Text("Deadline") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { /* Logic Simpan Tugas pake UC */ },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Bagikan Tugas")
                        }
                    }
                }
            }

            item {
                Text(
                    "Tugas Terkirim",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }

            // Dummy Data (Nantinya ambil dari API Laravel)
            val homeworkList = listOf(
                HomeworkData("Tugas Matematika", "12-IPA-1", "Terkirim", "12 Feb"),
                HomeworkData("Laporan Biologi", "12-IPA-2", "Draft", "15 Feb")
            )

            items(homeworkList) { hw ->
                HomeworkTeacherItem(hw, primaryBlue)
            }
        }
    }
}
