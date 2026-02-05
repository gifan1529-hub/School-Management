package com.example.schoolmanagement.UI.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.schoolmanagement.DI.Location
import com.example.schoolmanagement.DI.ToastHelper
import com.example.schoolmanagement.Utils.HandleException
import com.example.schoolmanagement.ViewModel.HomeViewModel
import com.example.schoolmanagement.getAttendanceStatus
import com.example.schoolmanagement.getTodayTime
import com.example.schoolmanagement.isLate
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import io.github.aakira.napier.Napier.e
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import qrgenerator.qrkitpainter.location
import qrscanner.CameraLens
import qrscanner.QrCodeScanner
import qrscanner.QrScanner

@Composable
fun ScannerScreen (
    navController: NavHostController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val exceptionHandler = remember { HandleException() }

    val locationHelper : Location = koinInject ()

    val userRole by viewModel.userRole.collectAsState()

    // permission untuk camera
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) { factory.createPermissionsController() }
    val scope = rememberCoroutineScope()

    BindEffect(controller)

    var hasCameraPermission by remember { mutableStateOf(false) }
    var isScanningActive by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            controller.providePermission(Permission.CAMERA)
            controller.providePermission(Permission.LOCATION)
            hasCameraPermission = true
        } catch (e: Exception) {
            hasCameraPermission = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)

    ) {
        if (hasCameraPermission) {
            if (isScanningActive) {
            // nampilin scanner kalo udah di izinin
            QrScanner(
                modifier = Modifier.fillMaxSize(),
                cameraLens = CameraLens.Back,
                onCompletion = { qrCode ->
                    if (qrCode.isNotEmpty()) {
                        isScanningActive = false
                        val timeNow = getTodayTime()
                        val telat = isLate()
                        scope.launch {
                            try {
                                val secretKey = if (userRole == "teacher" || userRole == "admin") {
                                    "GURU-HADIR-2026"
                                } else {
                                    "HADIR-QR-2026"
                                }
                                if (qrCode != secretKey) {
                                    val rolePesan = if (userRole == "Teacher") "QR untuk Guru" else "QR untuk Murid"
                                    ToastHelper().Toast("QR Code Tidak Valid! Gunakan $rolePesan.")
                                    delay(1000)
                                    isScanningActive = true
                                } else {
                                    val cord = locationHelper.getCurrentLocation()
                                    if (cord != null ) {
                                            try {
                                                viewModel.submitAbsen(
                                                    qrCode,
                                                    lat = cord.first,
                                                    long = cord.second
                                                )
                                                delay(1200)
                                                val errorByVM = viewModel.errorSubmitMessage.value
                                                if (viewModel.isAlreadyAbsen.value) {
                                                    navController.popBackStack()
                                                    val status = getAttendanceStatus()
                                                    val pesan = when (status) {
                                                        "Late" -> "Yah, kamu Telat Absen jam $timeNow"
                                                        "Absent" -> "Waduh, Kamu Alpa (Sudah lewat jam 11)!"
                                                        else -> "Mantap! Kamu Hadir jam $timeNow"
                                                    }
                                                    ToastHelper().Toast(pesan)
                                                    println("Scanner Result: $qrCode | Loc: ${cord.first}, ${cord.second}")
                                                } else if (errorByVM != null){
                                                    ToastHelper().Toast(errorByVM)
                                                    isScanningActive = true
                                                } else {
                                                    ToastHelper().Toast("Gagal Absen : Data Tidak Valid")
                                                    isScanningActive = true
                                                }
                                            } catch (e: Exception) {
                                                val errorManual = exceptionHandler.handleException(e)
                                                ToastHelper().Toast(errorManual.message ?: "Gagal Absens")
                                                isScanningActive = true
                                            }
                                    } else {
                                        ToastHelper().Toast("Gagal mendapatkan lokasi. Pastikan GPS aktif!")
                                        isScanningActive = true
                                    }
                                }
                            } catch (e: Exception) {
                                val errorManual = exceptionHandler.handleException(e)
                                ToastHelper().Toast(errorManual.message ?: "Gagal Absenss")
                                isScanningActive = true
                                println("Scanner Error: $e")
                            }
                        }
                    }
                },

                flashlightOn = false,
                openImagePicker = false,
                onFailure = { error ->
                    val erorr = exceptionHandler.handleException(error as Exception)
                    ToastHelper().Toast(erorr.message ?: "Gagal Men")
                    println("Scanner Error: $error")
                },
                imagePickerHandler = { _ -> }
            )
        }
//            Box(
//                modifier = Modifier
//                    .padding(top = 155.dp)
//                    .fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "Arahkan Kamera ke QR Code Guru",
//                        color = Color.White,
//                        modifier = Modifier
//                            .background(Color.Black.copy(alpha = 0.5f))
//                            .padding(8.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(20.dp))
//
//                    Box(
//                        modifier = Modifier
//                            .size(260.dp)
//                            .border(
//                                2.dp,
//                                Color.Cyan,
//                                RoundedCornerShape(12.dp)
//                            )
//                    )
//                }
//            }
        } else {
            // nampilin pesan jika ga diksi izin
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Izin Kamera Diperlukan", color = Color.White)
                Button(onClick = {
                    // minta izin lagi jika user klik tombol
                    scope.launch {
                        try {
                            controller.providePermission(Permission.CAMERA)
                            hasCameraPermission = true
                        } catch (e: Exception) {
                        }
                    }
                }) {
                    Text("Berikan Izin")
                }
            }
        }

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 40.dp, start = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}
