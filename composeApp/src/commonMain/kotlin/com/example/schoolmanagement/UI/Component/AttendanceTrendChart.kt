package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.forEach
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schoolmanagement.Domain.Model.ChartData

@Composable
fun AttendanceTrendChart(
    data: List<ChartData>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF0066FF)
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text("Attendance Trend", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth().height(180.dp)) {
            // Y-AXIS (Angka di samping kiri)
            Column(
                modifier = Modifier.fillMaxHeight().padding(end = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("95", "90", "85", "80", "75").forEach { label ->
                    Text(text = label, fontSize = 10.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(12.dp)) // Offset untuk label bulan
            }

            Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                val width = size.width
                val height = size.height
                val spacing = width / (data.size - 1)

                // Anggap range attendance 70% - 100% (sesuai gambar)
                val minVal = 70f
                val maxVal = 100f
                val range = maxVal - minVal

                // --- DRAW GRID LINES (Garis Putus-putus Horizontal) ---
                val gridCount = 4
                for (i in 0..gridCount) {
                    val yGrid = height / gridCount * i
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        start = Offset(0f, yGrid),
                        end = Offset(width, yGrid),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                val points = data.mapIndexed { index, chartData ->
                    val x = index * spacing
                    val normalizedValue = (chartData.value * 100 - minVal) / range
                    val y = height - (normalizedValue * height)
                    Offset(x, y)
                }

                // Gambar Garis Penghubung (Smooth Path)
                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        lineTo(points[i].x, points[i].y)
                    }
                }
                drawPath(path, color = lineColor, style = Stroke(width = 3.dp.toPx()))

                // Gambar Titik (Dots)
                points.forEach { center ->
                    drawCircle(color = Color.White, radius = 6.dp.toPx(), center = center)
                    drawCircle(color = lineColor, radius = 4.dp.toPx(), center = center)
                }
            }
        }

            // Label Bulan (X-Axis)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                data.forEach {
                    Text(it.label, fontSize = 10.sp, color = Color.Gray)
                }
            }
    }
}