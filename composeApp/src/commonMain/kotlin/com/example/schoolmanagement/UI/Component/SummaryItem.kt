package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SummaryItem(count: String, label: String, color: Color, font: androidx.compose.ui.text.font.FontFamily) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = color, fontFamily = font)
        Text(label, fontSize = 14.sp, color = Color.Gray, fontFamily = font)
    }
}