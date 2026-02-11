package com.example.schoolmanagement.UI.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import com.example.schoolmanagement.Data.Remote.UpdateProfileRequest
import com.example.schoolmanagement.UI.Component.EditField
import com.example.schoolmanagement.UI.Component.ToastType
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen (
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val poppins = getPoppinsFontFamily()
    val primaryBlue = Color(0xFF0066FF)

    val userName by viewModel.userName.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.updateResult) {
        viewModel.updateResult.collect { result ->
            if (result.isSuccess) {
                navController.previousBackStackEntry?.savedStateHandle?.set("toast_message", "Profil berhasil diperbarui!")
                navController.previousBackStackEntry?.savedStateHandle?.set("toast_type", ToastType.SUCCESS)
                navController.popBackStack()
            }
        }
    }

    LaunchedEffect(userName) {
        name = userName
        phone = userPhone
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profil", fontFamily = poppins, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F7FA))
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditField(
                label = "Nama Lengkap",
                value = name,
                onValueChange = { name = it },
                icon = Icons.Default.Person,
                poppins = poppins
            )

            Spacer(Modifier.height(16.dp))

            EditField(
                label = "Nomor Telepon",
                value = phone,
                onValueChange = { phone = it },
                icon = Icons.Default.Phone,
                poppins = poppins
            )

            Spacer(Modifier.height(16.dp))

            EditField(
                label = "Alamat",
                value = address,
                onValueChange = { address = it },
                icon = Icons.Default.Home,
                poppins = poppins
            )

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = {
                    viewModel.updateProfile(
                        UpdateProfileRequest(
                            name = name,
                            phone = phone,
                            address = address
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Simpan Perubahan", fontFamily = poppins, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                }
            }
        }
    }
}