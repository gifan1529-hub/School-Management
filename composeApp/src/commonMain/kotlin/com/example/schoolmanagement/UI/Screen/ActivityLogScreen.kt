package com.example.schoolmanagement.UI.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.schoolmanagement.Domain.Model.GradeData
import com.example.schoolmanagement.UI.Component.LogItem
import com.example.schoolmanagement.UI.Component.NilaiItem
import com.example.schoolmanagement.ViewModel.ActivityLogViewModel
import org.koin.compose.viewmodel.koinViewModel

// dummy
data class ActivityLog(
    val id: Int,
    val action: String,
    val module: String,
    val description: String,
    val time: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun ActivityLogScreen (
    navController: NavController,
    viewModel: ActivityLogViewModel = koinViewModel()
) {
    val log by viewModel.logs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)

    // dummy
    val logs = listOf(
        ActivityLog(1, "Create", "Homework", "Guru Budi membuat tugas baru: Matematika Logaritma", "10:30", Icons.Default.Add, Color(0xFF4CAF50)),
        ActivityLog(2, "Absen", "Attendance", "Anda melakukan absensi kehadiran (Hadir)", "07:15", Icons.Default.CheckCircle, primaryBlue),
        ActivityLog(3, "Grade", "Submission", "Guru memberikan nilai 90 pada tugas Bahasa Inggris", "Kemarin", Icons.Default.Star, Color(0xFFFFA500)),
        ActivityLog(4, "Update", "Profile", "Anda memperbarui foto profil", "2 hari lalu", Icons.Default.Edit, Color.Gray),
        ActivityLog(5, "Delete", "Announcement", "Admin menghapus pengumuman lama", "3 hari lalu", Icons.Default.Delete, Color.Red)
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = primaryBlue,
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 40.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Text(
                            text = "Activity Logs",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-20).dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isLoading && log.isEmpty()) {
                    item {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = primaryBlue
                        )
                    }
                }

                if (log.isEmpty() && !isLoading) {
                    item {
                        Text(
                            "Belum ada aktivitas terbaru",
                            modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }

                items(log) { item ->
                    LogItem(item)
                }
            }
        }
    }
}