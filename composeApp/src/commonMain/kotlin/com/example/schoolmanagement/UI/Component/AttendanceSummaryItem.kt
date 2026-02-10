package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily

@Composable
fun AttendanceSummaryItem (
    label: String,
    value: String,
    color: Color
) {
    val poppins = getPoppinsFontFamily()

    Column {
        Text(
            fontFamily = poppins,
            text = label,
            color = color.copy(alpha = 0.7f),
            fontSize = 12.sp
        )
        Text(
            text = value,
            fontFamily = poppins,
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}