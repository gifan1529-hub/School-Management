package com.example.schoolmanagement.UI.Screen.Parent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.schoolmanagement.UI.Component.ParentMenuCard
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.ActivityLogViewModel
import com.example.schoolmanagement.ViewModel.HomeAdminViewModel
import com.example.schoolmanagement.ViewModel.HomeViewModel
import com.example.schoolmanagement.ViewModel.SignIn
import org.koin.compose.viewmodel.koinViewModel
import com.example.schoolmanagement.UI.Component.ChildStatItem

@Composable
fun HomeScreenParent(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
    fcm: SignIn = koinViewModel(),
    logViewModel: ActivityLogViewModel = koinViewModel()
) {
    val userName by viewModel.userName.collectAsState()
    val parentData by viewModel.parentDashboardData.collectAsState()
    val userDetails by viewModel.userDetails.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val userNis by viewModel.userNIS.collectAsState()
    val userKelas by viewModel.userClass.collectAsState()

    val Poppins = getPoppinsFontFamily()

    val primaryBlue = Color(0xFF0066FF)
    val lightBlue = Color(0xFFE3F2FD)
    val lightGreen = Color(0xFFE8F5E9)
    val lightRed = Color(0xFFFFEBEE)
    val lightYellow = Color(0xFFFFF3E0)

    val unreadCount by logViewModel.unreadCount.collectAsState()

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        val titleSize = if (screenWidth < 360.dp) 18.sp else 24.sp
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refreshData() },
            modifier = Modifier
                .fillMaxSize()
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Halo, $userName!",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                fontFamily = Poppins,
                                text = "Track your child's progress",
                                color = Color.White.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                        Box {
                            IconButton(
                                onClick = {
                                    logViewModel.markAsRead()
                                    navController.navigate("activity")
                                },
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = "History",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)

                                )
                            }
                            if (unreadCount > 0) {
                                Surface(
                                    color = Color.Red,
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(22.dp)
                                        .offset(x = 2.dp, y = (-2).dp),
                                    border = BorderStroke(
                                        1.5.dp,
                                        primaryBlue
                                    )
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = if (unreadCount > 9) "9+" else unreadCount.toString(),
                                            color = Color.White,
                                            fontFamily = Poppins,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .offset(y = (-20).dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(60.dp),
                                shape = CircleShape,
                                color = primaryBlue
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = (parentData?.child_name ?: "A").take(1).uppercase(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        fontFamily = Poppins
                                    )
                                }
                            }

                            Spacer(Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = parentData?.child_name ?: "Memuat data...",
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Poppins,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = "Kelas ${parentData?.`class` ?: "-"}",
                                    color = Color.Gray,
                                    fontFamily = Poppins,
                                    fontSize = 14.sp
                                )
                                Surface(
                                    color = Color(0xFFF5F7FA),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Text(
                                        text = "NISN: ${parentData?.nisn ?: "-"}",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontSize = 11.sp,
                                        fontFamily = Poppins,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }

                        Divider(
                            modifier = Modifier.padding(vertical = 20.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ChildStatItem(
                                "${parentData?.total_attendance ?: "-"}",
                                "Attendance",
                                primaryBlue,
                                Poppins)
                            VerticalDivider()
                            ChildStatItem(
                                "${parentData?.avg_marks ?: "-"}",
                                "Avg Marks",
                                color = Color(0xFF4791FF),
                                Poppins)
                            VerticalDivider()
                            ChildStatItem(
                                "${parentData?.total_violations ?: "-"}",
                                "Violation",
                                color = Color(0xFFFFA500),
                                Poppins)
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .offset(y = (-10).dp)
                ) {
                    Text(
                        text = "Quick Access",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF2D3142)
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        ParentMenuCard(
                            "Student Safety", Icons.Default.LocationOn, Color.Red, Modifier.weight(1f), Poppins) {
                            navController.navigate("tracking")
                        }
                        ParentMenuCard("Attendance", Icons.Default.DateRange, primaryBlue, Modifier.weight(1f), Poppins) {
                            navController.navigate("attendanceparent")
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        ParentMenuCard("Homework", Icons.Default.AutoStories, Color(0xFF9C27B0), Modifier.weight(1f), Poppins) {
                            navController.navigate("homeworkparent")
                        }
                        ParentMenuCard("Progress", Icons.Default.TrendingUp, Color(0xFF4CAF50), Modifier.weight(1f), Poppins) {
                            // Navigasi
                        }
                    }
                }

                Spacer(modifier = Modifier.height(130.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme {
        HomeScreenParent(navController = rememberNavController())
    }
}