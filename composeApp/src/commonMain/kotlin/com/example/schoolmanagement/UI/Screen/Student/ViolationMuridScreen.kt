package com.example.schoolmanagement.UI.Screen.Student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagement.Domain.Model.GradeData
import com.example.schoolmanagement.UI.Component.NilaiItem
import com.example.schoolmanagement.UI.Component.ViolationCardMurid
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.ViolationViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ViolationMuridScreen(
    navController: NavHostController,
    viewModel: ViolationViewModel = koinViewModel(),
) {
    val poppins = getPoppinsFontFamily()
    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)

    val violations by viewModel.violations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val totalPoints = remember(violations) {
        violations.sumOf { it.points }
    }

    LaunchedEffect(Unit) {
        viewModel.loadViolations()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = primaryBlue,
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 40.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Text(
                            text = "Violation",
                            fontFamily = poppins,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "$totalPoints",
                                color = Color.White,
                                fontFamily = poppins,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                "/ 100",
                                color = Color.White.copy(alpha = 0.7f),
                                fontFamily = poppins,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 10.dp, start = 4.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                "Total Poin Pelanggaran",
                                fontFamily = poppins,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }
                        Text(
                            text = when {
                                totalPoints == 0 -> "Murid Teladan ✨"
                                totalPoints < 50 -> "Peringatan Ringan ⚠️"
                                totalPoints < 100 -> "Peringatan Keras ❗"
                                else -> "Sanksi Berat ❌"
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = Color.White,
                            fontFamily = poppins,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            if (isLoading) {
                Box {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = primaryBlue
                    )
                }
            } else if (violations.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SentimentVerySatisfied,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color(0xFF4CAF50)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Mantap! Kamu tidak punya pelanggaran.",
                            fontFamily = poppins,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .offset(y = (-20).dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(violations) { item ->
                        ViolationCardMurid(
                            item = item,
                            fontFamily = poppins,
                            onClick = { navController.navigate("detailviolation/${item.id}") }
                        )
                    }
                }
            }
        }
    }
}