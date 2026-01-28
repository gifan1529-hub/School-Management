package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun NavButton (
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp, start = 40.dp, end = 40.dp)
            .height(90.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp),
            shape = RoundedCornerShape(25.dp),
            color = Color.White.copy(alpha = 0.85f),
            shadowElevation = 15.dp,
            border = BorderStroke(1.dp, Color(0xFF1E90FF).copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 35.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    selected = selectedIndex == 0,
                    iconRes = Icons.Default.Notifications,
                    label = "Alerts",
                    onClick = { onTabSelected(0) }
                )

                Spacer(modifier = Modifier.width(30.dp))

                BottomNavItem(
                    selected = selectedIndex == 1,
                    iconRes = Icons.Default.Home,
                    label = "",
                    onClick = { onTabSelected(1) }
                )

                Spacer(modifier = Modifier.width(30.dp))

                BottomNavItem(
                    selected = selectedIndex == 2,
                    iconRes = Icons.Default.Person,
                    label = "Profile",
                    onClick = { onTabSelected(2) }
                )
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(68.dp)
                .clip(CircleShape),
            shape = CircleShape,
            color = Color.Transparent,
            shadowElevation = 12.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF1E90FF), Color(0xFF0000FF))
                        )
                    )
                    .clickable {
                        println("Home Clicked")
                        onTabSelected (1)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    tint = Color.White,
                    contentDescription = "Home",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    iconRes : ImageVector,
    label: String,
    onClick: () -> Unit,
    selected: Boolean
) {
//    val Poppins = getPoppinsFontFamily()

    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = iconRes,
            contentDescription = label,
            tint = if (selected) Color(0xFF0066FF) else Color.Gray.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 8.sp,
//            fontFamily = Poppins
        )
    }
}
