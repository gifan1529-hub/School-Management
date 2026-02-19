package com.example.schoolmanagement.UI.Screen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import com.example.schoolmanagement.DI.ToastHelper
import com.example.schoolmanagement.Domain.UseCase.LoginResult
import com.example.schoolmanagement.UI.Component.CustomToast
import com.example.schoolmanagement.UI.Component.ToastType
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.SignIn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import schoolmanagement.composeapp.generated.resources.Res
import schoolmanagement.composeapp.generated.resources.school
import schoolmanagement.composeapp.generated.resources.sekoah


@Composable
fun SignIn(
    navController: NavHostController,
    viewModel: SignIn = koinViewModel()
) {
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }

    val scope = rememberCoroutineScope()

    val poppins = getPoppinsFontFamily()

    var selectedRole by remember { mutableStateOf("Student") }
    val roles = listOf("Teacher","Student","Admin", "Parent")

    val uiState by viewModel.uiState.collectAsState()

    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F5F5)

        LaunchedEffect(viewModel.eventFlow) {
            viewModel.eventFlow.collect { result ->
                if (result is LoginResult.Success) {
                            val helper = com.example.schoolmanagement.DI.FcmHelper()
                            val fcmToken = helper.getFcmToken()
                            val userToken = result.token
                            if (fcmToken != null && userToken.isNotEmpty()) {
                                viewModel.sendFcmToken(fcmToken)
                                println("DEBUG FCM: Token berhasil dikirim!")
                            }
                    navController.navigate("home") {
                        popUpTo("signin") { inclusive = true }
                    }
                } else if (result is LoginResult.Error) {
                    println("Error: ${result.message}")
                    toastMessage = "Isi Email dan Password dengan benar"
                    toastType = ToastType.ERROR
                    showToast = true
                } else if (result is LoginResult.Failure) {
                    toastMessage = "Email atau Password Salah"
                    toastType = ToastType.ERROR
                    showToast = true
                    println("Masalah Koneksi: ${result.error.message}")
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(primaryBlue),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(170.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                Image(
                    painter = painterResource(Res.drawable.sekoah),
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp)
                )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "EduTrack",
                    fontFamily = poppins,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Smart School Management",
                    fontFamily = poppins,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
//                elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Login as",
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = poppins,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    lightGray,
                                    RoundedCornerShape(50.dp)
                                )
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            roles.forEach { role ->
                                val isSelected = selectedRole == role
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .background(
                                            if (isSelected) primaryBlue else Color.Transparent,
                                            RoundedCornerShape(50.dp)
                                        )
                                        .clickable { selectedRole = role },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = role,
                                        fontFamily = poppins,
                                        color = if (isSelected) Color.White else Color.Gray,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text("Email", fontWeight = FontWeight.Medium,fontFamily = poppins, fontSize = 14.sp)
                        TextField(
                            value = uiState.emailValue,
                            onValueChange = { viewModel.onEmailChange(it) },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            placeholder = { Text("Enter your email",fontFamily = poppins, color = Color.Gray) },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = lightGray,
                                unfocusedContainerColor = lightGray,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Password",fontFamily = poppins, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                        TextField(
                            value = uiState.passwordValue,
                            onValueChange = { viewModel.onPasswordChange(it) },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            placeholder = { Text("Enter your password",fontFamily = poppins, color = Color.Gray) },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = lightGray,
                                unfocusedContainerColor = lightGray,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                viewModel.onSignInClick(selectedRole)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                        ) {
                            Text("Login",fontFamily = poppins, color = Color.White, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Forgot Password?",
                            fontFamily = poppins,
                            modifier = Modifier.align(Alignment.CenterHorizontally).clickable { },
                            color = primaryBlue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            CustomToast(
                message = toastMessage,
                type = toastType,
                isVisible = showToast,
                onDismiss = { showToast = false }
            )
        }
    }
