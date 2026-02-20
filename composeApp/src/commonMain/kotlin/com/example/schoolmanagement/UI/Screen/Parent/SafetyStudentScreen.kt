package com.example.schoolmanagement.UI.Screen.Parent

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.schoolmanagement.DI.MapView
import com.example.schoolmanagement.UI.Component.ZoneItem
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.example.schoolmanagement.UI.Component.ActivityRow

// dummy
data class ActivityItem(
    val title: String,
    val time: String,
    val icon: ImageVector,
    val color: Color
)
@Composable
fun SafetyStudentScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val poppins = getPoppinsFontFamily()
    val primaryBlue = Color(0xFF0066FF)

    // dummy
    val activityLog = listOf(
        ActivityItem("Emma left school", "02:45 PM", Icons.Default.School, Color(0xFFF4511E)),
        ActivityItem("Emma entered school", "08:15 AM", Icons.Default.School, Color(0xFF43A047)),
        ActivityItem("Emma left home", "07:50 AM", Icons.Default.Home, Color(0xFF1E88E5))
    )

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
                        Column {
                            Text(
                                text = "Student Location Tracking",
                                fontFamily = poppins,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Real-time Safety Monitor",
                                fontFamily = poppins,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-20).dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(modifier = Modifier.size(60.dp), shape = CircleShape, color = Color(0xFFE91E63)) {
                        Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.padding(12.dp))
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Emma Wilson", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Grade 7B", color = Color.Gray, fontSize = 14.sp)
                    }
                    Surface(color = Color(0xFF00C853), shape = RoundedCornerShape(50.dp)) {
                        Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.School, null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("At School", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            MapView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(24.dp)),
                latitude = -6.200000, // Nanti ganti pake data Firebase
                longitude = 106.816666,
                title = "Lokasi Emma Wilson"
            )
            Spacer(Modifier.height(16.dp))

            Surface(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ZoneItem(primaryBlue, "Home Zone (100m)", poppins)
                    ZoneItem(Color(0xFF00C853), "School Zone (300m)", poppins)
                }
            }

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ){
                Text(
                    text = "Today's Activity",
                    fontFamily = poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF2D3142)
                )

                Spacer(Modifier.height(16.dp))

                activityLog.forEach { item ->
                    ActivityRow(item, poppins)
                    Spacer(Modifier.height(12.dp))
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}