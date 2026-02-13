package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schoolmanagement.Domain.Model.ViolationData

@Composable
fun ViolationCardMurid(
    item: ViolationData,
    fontFamily: FontFamily,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val (bgColor, contentColor) = when (item.category) {
        "Ringan" -> Color(0xFFFFF9C4) to Color(0xFFFBC02D) // Kuning
        "Sedang" -> Color(0xFFFFE0B2) to Color(0xFFF57C00) // Orange
        "Berat" -> Color(0xFFFFCDD2) to Color(0xFFD32F2F)  // Merah
        else -> Color.LightGray to Color.DarkGray
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = bgColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.AssignmentLate, null, tint = contentColor)
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.violation_name, fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(
                    "Oleh: ${item.teacher?.name ?: "Guru"}",
                    fontFamily = fontFamily,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    item.created_at.take(10),
                    fontFamily = fontFamily,
                    fontSize = 11.sp,
                    color = Color.LightGray
                )
            }

            Text(
                text = "+${item.points}",
                fontFamily = fontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = contentColor
            )
        }
    }
}