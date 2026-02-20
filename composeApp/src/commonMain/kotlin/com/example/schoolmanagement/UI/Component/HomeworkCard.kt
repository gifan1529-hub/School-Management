package com.example.schoolmanagement.UI.Component

import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import com.example.schoolmanagement.UI.Screen.Parent.HomeworkItem

@Composable
fun HomeworkCard(item: HomeworkItem, font: FontFamily, primaryBlue: Color) {
    val statusColor =
        if  (item.status == "Active") { Color(0xFF0066FF)
        } else if (item.status == "Completed") { Color(0xFF4CAF50)
        } else { Color(0xFFFF0000) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(45.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF5F7FA)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(item.subject.take(1), fontWeight = FontWeight.Bold, color = primaryBlue)
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column(Modifier.weight(1f)) {
                    Text(item.subject, fontFamily = font, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(item.title, fontFamily = font, fontSize = 13.sp, color = Color.Gray)
                }

                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        item.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = font
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Divider(thickness = 0.5.dp, color = Color.LightGray.copy(0.3f))
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Due: ${item.dueDate}", fontSize = 12.sp, color = Color.Gray, fontFamily = font)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector =
                            if (item.status == "Completed") { Icons.Default.CheckCircle
                            } else if (item.status == "Active") { Icons.Default.RadioButtonUnchecked
                            } else { Icons.Default.HighlightOff },
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = if (item.status == "Completed") { "Completed"
                        } else if (item.status == "Active") { "Active"
                        } else { "Expired" },
                        fontFamily = font,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }
        }
    }
}