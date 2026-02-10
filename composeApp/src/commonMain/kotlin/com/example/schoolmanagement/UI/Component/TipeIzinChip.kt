package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily

@Composable
fun TipeIzinChip(label: String, isSelected: Boolean, onSelect: () -> Unit) {
    val poppins = getPoppinsFontFamily()

    Surface(
        modifier = Modifier.clickable { onSelect() },
        color = if (isSelected) Color(0xFF0066FF).copy(alpha = 0.1f) else Color.Transparent,
        border = BorderStroke(1.dp, if (isSelected) Color(0xFF0066FF) else Color.LightGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            fontFamily = poppins,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            color = if (isSelected) Color(0xFF0066FF) else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}