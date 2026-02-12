package com.example.schoolmanagement.UI.Screen.Admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagement.UI.Component.IzinGuruContent
import com.example.schoolmanagement.UI.Component.IzinItem
import com.example.schoolmanagement.UI.Component.IzinMuridContent
import com.example.schoolmanagement.UI.Component.IzinMuridContentAdmin
import com.example.schoolmanagement.UI.Component.IzinPribadiContent
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.PermitViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun IzinScreenAdmin(
    navController: NavHostController,
) {
    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Izin Guru", "Izin Murid")

    val poppins = getPoppinsFontFamily()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = primaryBlue,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(top = 40.dp, bottom = 24.dp, start = 16.dp, end = 24.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text(
                        text = "Manajemen Izin",
                        fontFamily = poppins,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .background(
                                    if (isSelected) Color.White else Color.Transparent,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedTab = index },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontFamily = poppins,
                                color = if (isSelected) primaryBlue else Color.White,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (selectedTab == 0) {
                // ngeliat izin guru
                IzinGuruContent(navController)
            } else {
                // untuk ngeliat izin murid
                IzinMuridContentAdmin(navController)
            }
        }
    }
}