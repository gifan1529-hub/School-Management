package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun IzinPribadiContent(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Riwayat Izin Anda", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 14.sp)
        }
        // Dummy Data Izin Guru
        items(listOf("Seminar Pendidikan", "Keperluan Keluarga")) { izin ->
            IzinItem(
                title = izin,
                date = "30 Jan 2026",
                status = if (izin.contains("Seminar")) "Disetujui" else "Pending"
            )
        }
    }
}