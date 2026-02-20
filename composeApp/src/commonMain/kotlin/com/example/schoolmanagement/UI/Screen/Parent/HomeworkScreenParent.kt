package com.example.schoolmanagement.UI.Screen.Parent

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.schoolmanagement.UI.Component.HomeworkCard
import com.example.schoolmanagement.UI.Component.SummaryItem
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.collections.filter

data class HomeworkItem(
    val subject: String,
    val title: String,
    val dueDate: String,
    val status: String
)

@Composable
fun HomeworkScreenParent(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val poppins = getPoppinsFontFamily()
    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)

    var selectedFilter by remember { mutableStateOf("All") }

    val parentData by viewModel.parentDashboardData.collectAsState()

    val homeworkList = listOf(
        HomeworkItem("Mathematics", "Chapter 5 - Algebra Problems", "Oct 15, 2025", "Active"),
        HomeworkItem("Science", "Project on Solar System", "Oct 12, 2025", "Active"),
        HomeworkItem("English", "Essay - My Favorite Book", "Oct 11, 2025", "Completed"),
        HomeworkItem("Indonesia", "Project on Solar System", "Oct 13, 2025", "Expired")
    )

    val filteredList = when (selectedFilter) {
        "Active" -> homeworkList.filter { it.status == "Active" }
        "Completed" -> homeworkList.filter { it.status == "Completed" }
        "Expired" -> homeworkList.filter { it.status == "Expired" }
        else -> homeworkList
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
//                .verticalScroll(rememberScrollState())
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
                            text = "Homework",
                            fontFamily = poppins,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-20).dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SummaryItem("3", "Active", Color(0xFF0066FF), poppins)
                    Box(Modifier.height(40.dp).width(1.dp).background(Color.LightGray.copy(0.3f)))
                    SummaryItem("2", "Completed", Color(0xFF4CAF50), poppins)
                    Box(Modifier.height(40.dp).width(1.dp).background(Color.LightGray.copy(0.3f)))
                    SummaryItem("1", "Expired", Color(0xFFFF0000), poppins)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(Color.White, RoundedCornerShape(50.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("All", "Active", "Completed", "Expired").forEach { filter ->
                    val isSelected = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(if (isSelected) primaryBlue else Color.Transparent)
                            .clickable { selectedFilter = filter },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filter,
                            fontFamily = poppins,
                            color = if (isSelected) Color.White else Color.Gray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 10.sp
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 100.dp, top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredList) { item ->
                    HomeworkCard(item, poppins, primaryBlue)
                }
            }
        }
    }
}