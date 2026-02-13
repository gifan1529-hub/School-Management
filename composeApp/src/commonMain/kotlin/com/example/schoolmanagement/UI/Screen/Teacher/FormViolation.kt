package com.example.schoolmanagement.UI.Screen.Teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.example.schoolmanagement.DI.readFileBytes
import com.example.schoolmanagement.UI.Component.CustomToast
import com.example.schoolmanagement.UI.Component.ToastType
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.ViolationViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormViolation (
    navController: NavController,
    studentId: Int,
    studentName: String,
    viewModel: ViolationViewModel = koinViewModel()
) {
    val poppins = getPoppinsFontFamily()
    val primaryBlue = Color(0xFF0066FF)
    val scope = rememberCoroutineScope()

    var violationName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Ringan") }
    var points by remember { mutableStateOf("5") } // Default poin ringan
    var description by remember { mutableStateOf("") }

    var showFilePicker by remember { mutableStateOf(false) }
    var fileName by remember { mutableStateOf("") }
    var fileBytes by remember { mutableStateOf<ByteArray?>(null) }

    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val errorMessage by viewModel.isPostError.collectAsState()

    val categoryToPoints = mapOf(
        "Ringan" to "5",
        "Sedang" to "15",
        "Berat" to "35"
    )

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            viewModel.resetStatus()
            navController.popBackStack()
        }
    }

    FilePicker(show = showFilePicker, fileExtensions = listOf("jpg", "jpeg", "png", "gif")) { file ->
        showFilePicker = false
        file?.let {
            scope.launch {
                val bytes = it.readFileBytes()
                if (bytes != null) {
                    fileBytes = bytes
                    fileName = it.path.split("/").last().split("\\").last()
                }
            }
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Input Pelanggaran", fontFamily = poppins, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ){ padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F7FA))
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card (
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = primaryBlue.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp)
            ){
                Column (
                    modifier = Modifier.padding(16.dp)
                ){
                    Text("Target Murid:", fontSize = 12.sp, color = primaryBlue, fontFamily = poppins)
                    Text(studentName, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = poppins)
                    Text("NISN: $studentId", fontSize = 12.sp, color = Color.Gray, fontFamily = poppins)
                }
            }
            OutlinedTextField(
                value = violationName,
                onValueChange = { violationName = it },
                label = { Text("Jenis Pelanggaran (Contoh: Bolos)", fontFamily = poppins) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Text("Tingkat Pelanggaran", fontWeight = FontWeight.SemiBold, fontFamily = poppins)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categoryToPoints.keys.forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = {
                            selectedCategory = cat
                            points = categoryToPoints[cat] ?: "0"
                        },
                        label = { Text(cat, fontFamily = poppins) }
                    )
                }
            }

            OutlinedTextField(
                value = points,
                onValueChange = { if (it.all { char -> char.isDigit() }) points = it },
                label = { Text("Poin Pelanggaran", fontFamily = poppins) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                prefix = { Text("+", color = Color.Red) }
            )

            Text("Foto Bukti (Opsional)", fontWeight = FontWeight.SemiBold, fontFamily = poppins)
            OutlinedTextField(
                value = fileName.ifEmpty { "Klik untuk pilih foto" },
                onValueChange = {},
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth().clickable { showFilePicker = true },
                trailingIcon = {
                    if (fileName.isNotEmpty()) {
                        IconButton(onClick = { fileName = ""; fileBytes = null }) {
                            Icon(Icons.Default.Close, null, tint = Color.Red)
                        }
                    } else {
                        Icon(Icons.Default.AddAPhoto, null, tint = primaryBlue)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = if (fileName.isEmpty()) Color.Gray else Color.Black,
                    disabledBorderColor = if (fileName.isEmpty()) Color.Gray else primaryBlue
                )
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Kronologi / Keterangan", fontFamily = poppins) },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.submitViolation(
                        studentId = studentId,
                        name = violationName,
                        category = selectedCategory,
                        points = points.toIntOrNull() ?: 0,
                        description = description,
                        imageBytes = fileBytes,
                        imageName = fileName
                    )
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                enabled = violationName.isNotEmpty() && !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Catat Pelanggaran", fontFamily = poppins, fontWeight = FontWeight.Bold)
            }
        }
    }

    errorMessage?.let {
        CustomToast(message = it, isVisible = true, type = ToastType.ERROR, onDismiss = { viewModel.resetStatus() })
    }
}