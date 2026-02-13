package com.example.schoolmanagement.UI.Navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.schoolmanagement.UI.Component.CustomToast
import com.example.schoolmanagement.UI.Screen.FormIzin
import com.example.schoolmanagement.UI.Component.NavButton
import com.example.schoolmanagement.UI.Component.ToastType
import com.example.schoolmanagement.UI.Screen.ActivityLogScreen
import com.example.schoolmanagement.UI.Screen.Admin.AttendanceReportScreen
import com.example.schoolmanagement.UI.Screen.Admin.HomeScreenAdmin
import com.example.schoolmanagement.UI.Screen.Admin.IzinScreenAdmin
import com.example.schoolmanagement.UI.Screen.Admin.JadwalPelajaranGuru
import com.example.schoolmanagement.UI.Screen.Admin.KelolaUserScreen
import com.example.schoolmanagement.UI.Screen.ScannerScreen
import com.example.schoolmanagement.UI.Screen.SignIn
import com.example.schoolmanagement.UI.Screen.AlertScreen
import com.example.schoolmanagement.UI.Screen.EditProfileScreen
import com.example.schoolmanagement.UI.Screen.FormAlert
import com.example.schoolmanagement.UI.Screen.Student.DetailHomeWork
import com.example.schoolmanagement.UI.Screen.Student.DetailNilaiScreen
import com.example.schoolmanagement.UI.Screen.Student.DetailPelanggaranScreen
import com.example.schoolmanagement.UI.Screen.Student.HistoryAbsenScreen
import com.example.schoolmanagement.UI.Screen.Student.HomeScreen
import com.example.schoolmanagement.UI.Screen.Student.IzinScreen
import com.example.schoolmanagement.UI.Screen.Student.JadwalScreen
import com.example.schoolmanagement.UI.Screen.Student.MaterialScreen
import com.example.schoolmanagement.UI.Screen.Student.NilaiScreen
import com.example.schoolmanagement.UI.Screen.Student.ProfileScreen
import com.example.schoolmanagement.UI.Screen.Student.TugasScreen
import com.example.schoolmanagement.UI.Screen.Student.ViolationMuridScreen
import com.example.schoolmanagement.UI.Screen.Teacher.DetailIzinScreen
import com.example.schoolmanagement.UI.Screen.Teacher.FormMateriGuruScreen
import com.example.schoolmanagement.UI.Screen.Teacher.FormViolation
import com.example.schoolmanagement.UI.Screen.Teacher.HomeScreenGuru
import com.example.schoolmanagement.UI.Screen.Teacher.HomeWorkScreen
import com.example.schoolmanagement.UI.Screen.Teacher.IzinScreenGuru
import com.example.schoolmanagement.UI.Screen.Teacher.JadwalMengajarScreen
import com.example.schoolmanagement.UI.Screen.Teacher.ListPelanggaranMurid
import com.example.schoolmanagement.UI.Screen.Teacher.MarkAttendanceScreen
import com.example.schoolmanagement.UI.Screen.Teacher.MaterialScreenGuru
import com.example.schoolmanagement.UI.Screen.Teacher.PelanggaranScreenGuru

import com.example.schoolmanagement.UI.Screen.Teacher.SubmissionListScreen
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

    var showGlobalToast by remember { mutableStateOf(false) }
    var globalToastMessage by remember { mutableStateOf("") }
    var globalToastType by remember { mutableStateOf(ToastType.INFO) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(navBackStackEntry) {
        // Cek apakah ada titipan pesan "toast_message"
        val message = navController.currentBackStackEntry?.savedStateHandle?.get<String>("toast_message")
        val type = navController.currentBackStackEntry?.savedStateHandle?.get<ToastType>("toast_type")

        if (message != null) {
            globalToastMessage = message
            globalToastType = type ?: ToastType.INFO
            showGlobalToast = true

            // Langsung hapus biar gak muncul lagi pas back-back-an
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("toast_message")
            navController.currentBackStackEntry?.savedStateHandle?.remove<ToastType>("toast_type")
        }
    }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = "signin",
                modifier = modifier.fillMaxSize()
            ) {
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
                composable("jadwal") {
                    JadwalScreen(navController)
                }
                composable("tugas") {
                    TugasScreen(navController)
                }
                composable("izin") {
                    IzinScreen(navController)
                }
                composable("formizin") {
                    FormIzin(navController)
                }
                composable("absenHistory") {
                    HistoryAbsenScreen(navController)
                }
                composable("nilai") {
                    NilaiScreen(navController)
                }
                composable("siswahadir") {
                    MarkAttendanceScreen(navController)
                }
                composable("izinguru") {
                    IzinScreenGuru(navController)
                }
                composable("jadwalngajar") {
                    JadwalMengajarScreen(navController)
                }
                composable("homework") {
                    HomeWorkScreen(navController)
                }
                composable("detailIzin/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
                    DetailIzinScreen(navController, id)
                }
                composable("formalert") {
                    FormAlert(navController)
                }
                composable("detailtugas/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
                    DetailHomeWork(navController, homeworkId = id)
                }
                composable("attendancereport") {
                    AttendanceReportScreen(navController)
                }
                composable("detailtugasguru/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
                    SubmissionListScreen(navController, homeworkId = id)
                }
                composable("usermanage") {
                    KelolaUserScreen(navController)
                }
                composable("activity") {
                    ActivityLogScreen(navController)
                }
                composable("izinadmin") {
                    IzinScreenAdmin(navController)
                }
                composable ("editprofile"){
                    EditProfileScreen(navController)
                }
                composable("detailnilai/{subject}") { backStackEntry ->
                    val subject = backStackEntry.arguments?.getString("subject") ?: ""
                    DetailNilaiScreen(navController, subjectName = subject)
                }
                composable("adminjadwal"){
                    JadwalPelajaranGuru(navController)
                }
                composable("materiguru"){
                    FormMateriGuruScreen(navController)
                }
                composable("materi"){
                    MaterialScreen(navController)
                }
                composable("materigurus"){
                    MaterialScreenGuru(navController)
                }
                composable("pelanggaran"){
                    PelanggaranScreenGuru(navController)
                }
                composable ("listpelanggaran/{studentId}/{studentName}"){ backStack ->
                    val id = backStack.arguments?.getString("studentId")?.toInt() ?: 0
                    val name = backStack.arguments?.getString("studentName") ?: ""
                    ListPelanggaranMurid(navController, name, id)
                }
                composable ("formpelanggaran/{studentId}/{studentName}"){ backStack ->
                    val id = backStack.arguments?.getString("studentId")?.toInt() ?: 0
                    val name = backStack.arguments?.getString("studentName") ?: ""
                    FormViolation(navController, id, name)
                }
                composable("violationmurid"){
                    ViolationMuridScreen(navController)
                }
                composable("detailviolation/{violationId}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("violationId")?.toInt() ?: 0
                    DetailPelanggaranScreen(navController, id)
                }
            }
            CustomToast(
                message = globalToastMessage,
                isVisible = showGlobalToast,
                type = globalToastType,
                onDismiss = { showGlobalToast = false }
            )
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
                1 -> {
                    println("DEBUG: Current Role in Pager: $userRole")
                    when (userRole) {
                        "student" -> HomeScreen(navController)
                        "teacher" -> HomeScreenGuru(navController)
                        "admin" -> HomeScreenAdmin(navController)
                        else -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Color(0xFF0066FF))
                            }
                        }
                    }
                }
                0 -> AlertScreen(navController)
                2 -> ProfileScreen(navController)
            }
        }
    }
}