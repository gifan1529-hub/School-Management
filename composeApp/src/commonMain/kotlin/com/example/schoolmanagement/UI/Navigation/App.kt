package com.example.schoolmanagement.UI.Navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.schoolmanagement.UI.Screen.ScannerScreen
import com.example.schoolmanagement.UI.Screen.SignIn
import com.example.schoolmanagement.UI.Screen.Student.HomeScreen
import com.example.schoolmanagement.UI.Screen.Student.ProfileScreen
import com.example.schoolmanagement.ViewModel.AuthViewModel
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
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
                SignIn(navController)
            }
            composable("home") {
                HomeScreen(navController)
            }
            composable("profile") {
                ProfileScreen(navController)
            }
            composable("scanner") {
                ScannerScreen(navController)
            }
        }
    }
}