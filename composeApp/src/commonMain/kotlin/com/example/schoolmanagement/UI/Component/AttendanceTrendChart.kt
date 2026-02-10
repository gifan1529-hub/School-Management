package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.forEach
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
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
import com.example.schoolmanagement.Domain.Model.TrendData
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.HomeAdminViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AttendanceTrendChart(
    data: List<TrendData>,
    modifier: Modifier = Modifier,
    viewMode: HomeAdminViewModel = koinViewModel(),
    lineColor: Color = Color(0xFF0066FF)
) {
    val poppins = getPoppinsFontFamily()

    val isLoading = viewMode.isLoading.value

    if (isLoading && data.isEmpty()) {
        Box(Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = lineColor)
        }
        return
    } else if (!isLoading && data.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
            Text("Belum ada data statistik", fontFamily = poppins, color = Color.Gray, fontSize = 12.sp)
        }
    } else {
    Column(modifier = modifier.padding(16.dp)) {
        Text("Attendance Trend", fontFamily = poppins, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth().height(180.dp)) {
            // Y-AXIS (Angka di samping kiri)
            Column(
                modifier = Modifier.fillMaxHeight().padding(end = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("20", "15", "10", "5", "0").forEach { label ->
                    Text(text = label, fontSize = 10.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(12.dp)) // Offset untuk label bulan
            }

            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {

                Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                    val width = size.width
                    val height = size.height
                    val spacing = width / (data.size - 1)

                    val minVal = 0.0f
                    val maxVal = 20.0f
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

                val spacingPercent = 2f / (data.size - 1)
                data.forEachIndexed { index, trend ->
                    val horizontalBias = -1f + (index * spacingPercent)
                    val percentageText = "${(trend.value * 100).toInt()}"

                    // Hitung Y agar teks melayang di atas titik
                    // Kita gunakan bias vertikal agar lebih stabil di berbagai layar
                    val verticalBias =
                        1f - (trend.value * 2f) - 0.15f // -0.15f untuk offset ke atas

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = BiasAlignment(horizontalBias, verticalBias)
                    ) {
                        Text(
                            text = percentageText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = lineColor
                        )
                    }
                }
            }
        }

        // Label Bulan (X-Axis)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEach {
                Text(it.label, fontFamily = poppins, fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
    }
}