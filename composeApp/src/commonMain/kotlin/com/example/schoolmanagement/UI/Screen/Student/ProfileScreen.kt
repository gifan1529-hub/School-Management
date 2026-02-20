package com.example.schoolmanagement.UI.Screen.Student

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.ProfileViewModel
import com.example.schoolmanagement.getTodayDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen (
    navController: NavHostController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val userPhone by viewModel.userPhone.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val userAddress by viewModel.userAddress.collectAsState()
    val logoutEvent by viewModel.logoutEvent.collectAsState()

    val poppins = getPoppinsFontFamily()

    val primaryBlue = Color(0xFF0066FF)
    val red = Color(0xFFFF0000)
    val lightBlue = Color(0xFFE3F2FD)
    val lightGreen = Color(0xFFE8F5E9)
    val lightRed = Color(0xFFFFEBEE)

    LaunchedEffect(logoutEvent) {
        if (logoutEvent) {
            navController.navigate("signin") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                        Text(
                            text = "Profil & Settings",
                            fontFamily = poppins,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
            }
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-20).dp), // ngebuat card agak naik nimpa header
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(primaryBlue.copy(alpha = 0.1f))
                                .border(2.dp, primaryBlue, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.size(45.dp),
                                tint = primaryBlue
                            )
                        }
                        Column (modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                "${userName}",
                                fontFamily = poppins,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                            )
                            Text(
                                "${userRole}",
                                fontFamily = poppins,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { navController.navigate("editprofile") },
                            modifier = Modifier
                                .background(Color(0xFF0066FF), CircleShape)
                                .size(30.dp)
                        ){
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = "Edit Profile",
                                modifier = Modifier.size(20.dp),
                                tint = Color.White,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Spacer(modifier = Modifier.height(24.dp))

                    ProfileInfoRow(
                        Icons.Default.Email,
                        "Email",
                        "${userEmail}",
                        Color.Blue
                    )
                    ProfileInfoRow(
                        Icons.Default.Call,
                        "Phone",
                        "$userPhone",
                        Color.Green
                    )
                    ProfileInfoRow(
                        Icons.Default.AccountCircle,
                        "Role", "${userRole}",
                        Color.Magenta
                    )
                    ProfileInfoRow(
                        Icons.Default.Home,
                        "Address", "${userAddress}",
                        Color.Blue
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                color = red,
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(50.dp),
                onClick = {
                    viewModel.logout()
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Logout",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = poppins,
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String, iconColor: Color) {
    val poppins = getPoppinsFontFamily()
    Row(
        modifier = Modifier.padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
        }
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(label, fontSize = 11.sp,fontFamily = poppins, color = Color.Gray)
            Text(value, fontSize = 14.sp,fontFamily = poppins, fontWeight = FontWeight.SemiBold)
        }
    }
}