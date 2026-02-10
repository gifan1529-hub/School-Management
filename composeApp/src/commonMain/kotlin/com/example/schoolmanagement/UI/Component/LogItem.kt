package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.schoolmanagement.Domain.Model.LogData
import com.example.schoolmanagement.UI.Screen.ActivityLog
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily

@Composable
fun LogItem(log: LogData) {
    val poppins = getPoppinsFontFamily()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(getLogColor(log.action), CircleShape)
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(Color.LightGray.copy(alpha = 0.5f))
            )
        }

        Card(
            modifier = Modifier
                .padding(bottom = 16.dp, end = 8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = getLogColor(log.action).copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = getLogIcon(log.action),
                            contentDescription = null,
                            tint = getLogColor(log.action),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = log.module,
                            fontSize = 12.sp,
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold,
                            color = getLogColor(log.action)
                        )
                        Text(
                            text = log.created_at,
                            fontSize = 10.sp,
                            fontFamily = poppins,
                            color = Color.Gray
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = log.details,
                        fontSize = 14.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 18.sp
                    )
                    log.user?.let {
                        Text(
                            text = "Oleh: ${it.name} (${it.role})",
                            fontSize = 11.sp,
                            fontFamily = poppins,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getLogIcon(action: String) = when (action.uppercase()) {
    "CREATE", "STORE" -> Icons.Default.AddCircle
    "UPDATE", "PATCH" -> Icons.Default.Edit
    "DELETE" -> Icons.Default.Delete
    "LOGIN" -> Icons.Default.History
    else -> Icons.Default.Info
}

@Composable
fun getLogColor(action: String) = when (action.uppercase()) {
    "CREATE", "STORE" -> Color(0xFF4CAF50) // Hijau
    "UPDATE", "PATCH" -> Color(0xFFFFA500) // Oranye
    "DELETE" -> Color(0xFFF44336) // Merah
    else -> Color(0xFF0066FF) // Biru
}
