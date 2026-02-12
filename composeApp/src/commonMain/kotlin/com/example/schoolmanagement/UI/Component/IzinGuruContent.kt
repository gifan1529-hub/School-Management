package com.example.schoolmanagement.UI.Component

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.PermitViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun IzinGuruContent (
    navController: NavHostController,
    viewModel: PermitViewModel = koinViewModel()
) {
    val poppins = getPoppinsFontFamily()

    val guruPermits by viewModel.guruPermitHistory .collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPermitHistory()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading && guruPermits.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (guruPermits.isEmpty()) {
            Text(
                "Belum ada permohonan izin dari guru",
                fontFamily = poppins,
                modifier = Modifier.align(Alignment.Center),
                color = Color.Gray
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        "Permohonan Izin Guru",
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                items(guruPermits) { izin ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {

                                Text(
                                    text = izin.user?.name ?: "Guru",
                                    fontFamily = poppins,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "${izin.type}: ${izin.reason}",
                                    fontFamily = poppins,
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Periode: ${izin.start_date} s/d ${izin.end_date}",
                                    fontSize = 11.sp,
                                    fontFamily = poppins,
                                    color = Color.LightGray
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Surface(
                                    color = when (izin.status) {
                                        "approved" -> Color(0xFFE8F5E9)
                                        "rejected" -> Color(0xFFFFEBEE)
                                        else -> Color(0xFFFFF3E0)
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = izin.status.uppercase(),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontSize = 10.sp,
                                        fontFamily = poppins,
                                        fontWeight = FontWeight.SemiBold,
                                        color = when (izin.status) {
                                            "approved" -> Color(0xFF4CAF50)
                                            "rejected" -> Color.Red
                                            else -> Color(0xFFFFA500)
                                        }
                                    )
                                }

                                if (izin.status == "pending") {
                                    Text(
                                        "Detail",
                                        fontFamily = poppins,
                                        color = Color(0xFF0066FF),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .clickable {
                                                println("DEBUG IZIN: Detail Izin Clicked ${izin.id}")
                                                navController.navigate("detailIzin/${izin.id}")
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}