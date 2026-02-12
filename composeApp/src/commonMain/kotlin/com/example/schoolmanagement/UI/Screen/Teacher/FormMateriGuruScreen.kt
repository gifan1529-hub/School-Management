package com.example.schoolmanagement.UI.Screen.Teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.example.schoolmanagement.DI.readFileBytes
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.MaterialTeacherViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormMateriGuruScreen (
    navController: NavController,
    viewModel: MaterialTeacherViewModel = koinViewModel()
) {
    val poppins = getPoppinsFontFamily()
    val primaryBlue = Color(0xFF0066FF)
    val scope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(false) }
    val selectedClass by viewModel.targetClass.collectAsState()

    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()
    val subject by viewModel.subject.collectAsState()
    val type by viewModel.type.collectAsState()
    val linkContent by viewModel.linkContent.collectAsState()
    val fileName by viewModel.fileName.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    var showFilePicker by remember { mutableStateOf(false) }

    FilePicker(show = showFilePicker, fileExtensions = listOf("pdf","doc","docx","ppt","pptx","xls","xlsx","jpg","jpeg","png","mp4","mov","avi","mkv")) { file ->
        showFilePicker = false
        file?.let {
            scope.launch {
                val bytes = it.readFileBytes()
                if (bytes != null) viewModel.onFileSelected(it.path.split("/").last(), bytes)
            }
        }
    }

    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bagikan Materi", fontFamily = poppins, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp).verticalScroll(
                rememberScrollState()
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title, onValueChange = viewModel::onTitleChange,
                label = { Text("Judul Materi", fontFamily = poppins) },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = subject, onValueChange = viewModel::onSubjectChange,
                label = { Text("Mata Pelajaran", fontFamily = poppins) },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedClass.ifEmpty { "Pilih Tujuan Kelas" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tujuan Kelas", fontFamily = poppins) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBlue,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    viewModel.availableClasses.forEach { className ->
                        DropdownMenuItem(
                            text = { Text(className, fontFamily = poppins) },
                            onClick = {
                                viewModel.onClassChange(className)
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Deskripsi / Intruksi", fontFamily = poppins) },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                minLines = 3
            )

            Text("Jenis Materi", fontFamily = poppins, fontWeight = FontWeight.SemiBold)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("file", "link", "video").forEach { t ->
                    FilterChip(
                        selected = type == t,
                        onClick = { viewModel.onTypeChange(t) },
                        label = { Text(t.uppercase(), fontFamily = poppins) }
                    )
                }
            }

            if (type == "link") {
                OutlinedTextField(
                    value = linkContent, onValueChange = viewModel::onLinkChange,
                    label = { Text("URL Link (YouTube/Drive)", fontFamily = poppins) },
                    modifier = Modifier.fillMaxWidth(), shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                )
            } else {
                OutlinedTextField(
                    value = fileName, onValueChange = {},
                    readOnly = true, enabled = false,
                    label = { Text("Pilih File", fontFamily = poppins) },
                    modifier = Modifier.fillMaxWidth().clickable { showFilePicker = true },
                    trailingIcon = { Icon(Icons.Default.AttachFile, null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(disabledBorderColor = primaryBlue, disabledTextColor = Color.Black)
                )
            }

            Button(
                onClick = { viewModel.submitMaterial() },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                enabled = !isLoading
            ) {
                  if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                Text("Kirim Materi", fontFamily = poppins, fontWeight = FontWeight.Bold)
            }
        }
    }
}