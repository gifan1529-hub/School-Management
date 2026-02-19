package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ChildStatItem(value: String, label: String, color: Color, font: FontFamily) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = color,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            fontFamily = font
        )
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp,
            fontFamily = font
        )
    }
}