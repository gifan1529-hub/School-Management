package com.example.schoolmanagement.UI.Component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun NavButton (
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val poppins = getPoppinsFontFamily()
    val primaryBlue = Color(0xFF0066FF)
    val accentBlue = Color(0xFF4791FF)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 60.dp, start = 40.dp, end = 40.dp)
            .height(100.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp),
            shape = RoundedCornerShape(30.dp),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 20.dp,
            tonalElevation = 5.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    selected = selectedIndex == 0,
                    iconRes = Icons.Default.Notifications,
                    label = "Alerts",
                    onClick = { onTabSelected(0) }
                )

                Spacer(modifier = Modifier.width(60.dp))

                BottomNavItem(
                    selected = selectedIndex == 2,
                    iconRes = Icons.Default.Person,
                    label = "Profile",
                    onClick = { onTabSelected(2) }
                )
            }
        }

        val homeElevation by animateDpAsState(if (selectedIndex == 1) 15.dp else 0.dp)

        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-5).dp)
                .size(68.dp),
            shape = CircleShape,
            color = Color.Transparent,
            shadowElevation = homeElevation
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = if (selectedIndex == 1)
                                listOf(primaryBlue, accentBlue)
                            else
                                listOf(Color.Gray.copy(0.6f), Color.Gray)
                        )
                    )
                    .clickable (
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ){
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
    val poppins = getPoppinsFontFamily()
    val animatedColor by animateColorAsState(
        targetValue = if (selected) Color(0xFF0066FF) else Color.Gray.copy(alpha = 0.5f),
        animationSpec = tween(durationMillis = 300)
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = iconRes,
            contentDescription = label,
            tint = animatedColor,
            modifier = Modifier.size(26.dp)
        )
        if (selected) {
            Text(
                text = label,
                color = animatedColor,
                fontSize = 10.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
