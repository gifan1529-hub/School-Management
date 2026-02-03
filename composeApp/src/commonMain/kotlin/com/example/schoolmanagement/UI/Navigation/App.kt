package com.example.schoolmanagement.UI.Navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.schoolmanagement.UI.Screen.FormIzin
import com.example.schoolmanagement.UI.Component.NavButton
import com.example.schoolmanagement.UI.Screen.ScannerScreen
import com.example.schoolmanagement.UI.Screen.SignIn
import com.example.schoolmanagement.UI.Screen.Student.AlertScreen
import com.example.schoolmanagement.UI.Screen.Student.HistoryAbsenScreen
import com.example.schoolmanagement.UI.Screen.Student.HomeScreen
import com.example.schoolmanagement.UI.Screen.Student.IzinScreen
import com.example.schoolmanagement.UI.Screen.Student.JadwalScreen
import com.example.schoolmanagement.UI.Screen.Student.NilaiScreen
import com.example.schoolmanagement.UI.Screen.Student.ProfileScreen
import com.example.schoolmanagement.UI.Screen.Student.TugasScreen
import com.example.schoolmanagement.UI.Screen.Teacher.HomeScreenGuru
import com.example.schoolmanagement.UI.Screen.Teacher.HomeWorkScreen
import com.example.schoolmanagement.UI.Screen.Teacher.IzinScreenGuru
import com.example.schoolmanagement.UI.Screen.Teacher.JadwalMengajarScreen
import com.example.schoolmanagement.UI.Screen.Teacher.MarkAttendanceScreen
import com.example.schoolmanagement.ViewModel.AuthViewModel
import com.example.schoolmanagement.ViewModel.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = koinViewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = "signin",
            modifier = modifier.fillMaxSize()
        ){
            composable("signin") {
                LaunchedEffect(Unit) {
                    if (authViewModel.isLoggedIn()) {
                        navController.navigate("home") {
                            popUpTo("signin") { inclusive = true }
                        }
                    }
                }
                SignIn(navController)
            }
            composable("home") {
                MainPagerScreen(navController)
            }
            composable("profile") {
                ProfileScreen(navController)
            }
            composable("scanner") {
                ScannerScreen(navController)
            }
            composable("alert") {
                AlertScreen(navController)
            }
            composable("jadwal"){
                JadwalScreen(navController)
            }
            composable ("tugas"){
                TugasScreen(navController)
            }
            composable("izin") {
                IzinScreen(navController)
            }
            composable("formizin"){
                FormIzin(navController)
            }
            composable("absenHistory"){
                HistoryAbsenScreen(navController)
            }
            composable("nilai"){
                NilaiScreen(navController)
            }
            composable("siswahadir"){
                MarkAttendanceScreen(navController)
            }
            composable ("izinguru"){
                IzinScreenGuru(navController)
            }
            composable ("jadwalngajar"){
                JadwalMengajarScreen(navController)
            }
            composable("homework") {
                HomeWorkScreen(navController)
            }
        }
    }
}

@Composable
fun MainPagerScreen (
    navController: NavHostController,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 3 })
    val scope = rememberCoroutineScope()
    val userRole by homeViewModel.userRole.collectAsState()

    Scaffold (
        bottomBar = {
            NavButton(
                selectedIndex = pagerState.currentPage,
                onTabSelected = { index ->
                    scope.launch { pagerState.animateScrollToPage(index) }
                }
            )
        }
    ) { _ ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = true
        ) { page ->
            when (page) {
                1 -> when (userRole) {
                    "student" -> HomeScreen(navController)
                    "teacher" -> HomeScreenGuru(navController)
                    "admin" -> HomeScreen(navController)
                }
                0 -> AlertScreen(navController)
                2 -> ProfileScreen(navController)
            }
        }
    }
}