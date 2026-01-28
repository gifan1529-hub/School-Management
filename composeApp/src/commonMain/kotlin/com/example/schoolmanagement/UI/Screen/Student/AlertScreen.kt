package com.example.schoolmanagement.UI.Screen.Student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.schoolmanagement.UI.Component.AlertCard
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock

data class AlertData(
    val title: String,
    val message: String,
    val time: String,
    val type: String
)
@Composable
fun AlertScreen(
    navController: NavController
) {
    val currentMoment = Clock.System.now()
    val now = currentMoment.toLocalDateTime(currentSystemDefault())
    val isMorningTime = now.hour in 6..16

    // cuman dummy aja. nni kalo udah ada api nnya bakal ngambil dari api
    val alertsFromGuru = listOf(
        AlertData("Info Rapat", "Kumpul di aula jam 8", "07:00", "info"),
        AlertData("Tugas Baru", "Cek menu tugas untuk Matematika", "Kemarin", "warning"),
        AlertData("Libur Dadakan", "Besok sekolah libur karena renovasi", "2 jam lalu", "danger")
    )

    val primaryBlue = Color(0xFF0066FF)

    Box (
        modifier = Modifier.fillMaxSize()
    ){
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
                    Text(
                        text = "Alerts",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-20).dp), // ngebuat card agak naik nimpa header
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    contentPadding = PaddingValues(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (isMorningTime) {
                        item {
                            AlertCard(
                                title = "Peringatan Absensi",
                                message = "Kamu belum melakukan absensi pagi ini. Segera scan QR di kelas!",
                                time = "07:15",
                                type = "danger",
                                onClick = {
                                }
                            )
                        }
                    }

                    items(alertsFromGuru) { alert ->
                        AlertCard(
                            title = alert.title,
                            message = alert.message,
                            time = alert.time,
                            type = alert.type,
                            onClick = { }
                        )
                    }
                }
            }
        }
    }
}