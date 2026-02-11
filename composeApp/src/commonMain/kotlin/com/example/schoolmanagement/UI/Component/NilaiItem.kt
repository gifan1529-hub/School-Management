package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schoolmanagement.Domain.Model.GradeData
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily

@Composable
fun NilaiItem(data: GradeData, onCardClick: () -> Unit) {
    val statusColor = if (data.nilai >= 75) Color(0xFF4CAF50) else Color(0xFFFFA500)
    val poppins = getPoppinsFontFamily()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onCardClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(data.subject, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Status: ${data.status}", fontFamily = poppins, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = data.nilai.toString(),
                    fontSize = 20.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Grade: ${data.grade}",
                    fontSize = 12.sp,
                    fontFamily = poppins,
                    color = Color.Gray
                )
            }
        }
    }
}