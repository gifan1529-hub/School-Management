package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily

@Composable
fun TugasItem (
    subject: String,
    title: String,
    deadline: String,
    status: String,
    grade: Int? = null,
    onClick: () -> Unit
) {
    val poppins = getPoppinsFontFamily()
    val primaryBlue = Color(0xFF0066FF)
    val (statusColor, statusBg, statusLabel) = when (status) {
        "Done" -> Triple(
            Color(0xFF4CAF50),
            Color(0xFFE8F5E9),
            "Selesai"
        )
        "Expired" -> Triple(
            Color(0xFFF44336),
            Color(0xFFFFEBEE),
            "Terlewat"
        )
        else -> Triple( // Active
            primaryBlue,
            Color(0xFFE3F2FD),
            "Aktif"
        )
    }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = subject, fontSize = 12.sp, fontFamily = poppins, color = primaryBlue, fontWeight = FontWeight.Bold)
            Text(text = title, fontSize = 15.sp, fontFamily = poppins, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.Gray
                )
                Text(text = " Deadline: $deadline", fontFamily = poppins, fontSize = 12.sp, color = Color.Gray)
            }
        }

        Surface(
            color = statusBg,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = statusLabel,
                color = statusColor,
                fontSize = 11.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

    }
}