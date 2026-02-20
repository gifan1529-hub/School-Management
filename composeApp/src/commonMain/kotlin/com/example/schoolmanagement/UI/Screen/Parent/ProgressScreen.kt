package com.example.schoolmanagement.UI.Screen.Parent

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.schoolmanagement.UI.Component.ProgressStatCard
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProgressScreen (
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val poppins = getPoppinsFontFamily()
    val primaryBlue = Color(0xFF0066FF)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .verticalScroll(rememberScrollState())
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
                            text = "Performance",
                            fontFamily = poppins,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-20).dp)
            ){
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    ProgressStatCard(
                        title = "Average Score",
                        value = "85%",
                        icon = Icons.Default.Adjust,
                        iconColor = primaryBlue,
                        modifier = Modifier.weight(1f),
                        font = poppins
                    )

                    ProgressStatCard(
                        title = "Violation",
                        value = "3",
                        icon = Icons.Default.Gavel,
                        iconColor = Color(0xFFE74C3C) ,
                        modifier = Modifier.weight(1f),
                        font = poppins
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    ProgressStatCard(
                        title = "Improvement",
                        value = "+12%",
                        icon = Icons.Default.TrendingUp,
                        iconColor = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f),
                        font = poppins
                    )
                    ProgressStatCard(
                        title = "Total Tests",
                        value = "24",
                        icon = Icons.Default.AutoStories,
                        iconColor = Color(0xFF9C27B0),
                        modifier = Modifier.weight(1f),
                        font = poppins
                    )
                }
                Spacer(Modifier.height(50.dp))
            }
        }
    }
}