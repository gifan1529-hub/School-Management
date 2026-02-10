package com.example.schoolmanagement.UI.Screen.Admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.schoolmanagement.DI.ToastHelper
import com.example.schoolmanagement.Data.Remote.AddUserRequest
import com.example.schoolmanagement.UI.Component.UserCard
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily
import com.example.schoolmanagement.ViewModel.UpdateUserViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.text.contains

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KelolaUserScreen (
    navController: NavController,
    viewModel: UpdateUserViewModel = koinViewModel()
) {
    val primaryBlue = Color(0xFF0066FF)
    val lightGray = Color(0xFFF5F7FA)

    val allUsers by viewModel.allUsers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorUpdate by viewModel.errorUpdateMessage.collectAsState()
    val isAddSucces by viewModel.isAddSuccess.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("student") }
    var userClass by remember { mutableStateOf("") }
    var nisn by remember { mutableStateOf("") }

    val poppins = getPoppinsFontFamily()

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedUserForEdit by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var selectedUserForDelete by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var selectedNewRole by remember { mutableStateOf("student") }

    var selectedTabIndex by remember { mutableStateOf(0) } // 0: Siswa, 1: Guru
    var searchQuery by remember { mutableStateOf("") }

    val filteredUsers = allUsers.filter { user ->
        val matchRole = if (selectedTabIndex == 0) user.role == "student" else user.role == "teacher"
        val matchSearch = user.name.contains(searchQuery, ignoreCase = true) ||
                (user.nisn?.contains(searchQuery) ?: false)
        matchRole && matchSearch
    }

    LaunchedEffect(isAddSucces){
        if (isAddSucces) {
            name = ""
            email = ""
            nisn = ""
            userClass = ""
            password = ""
            role = "student"
        }
    }
    if (showAddDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showAddDialog = false },
            title = { Text("Tambah Pengguna Baru",fontFamily = poppins,) },
            text = {
                Column (verticalArrangement = Arrangement.spacedBy(8.dp)){
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nama Lengkap") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = nisn,
                        onValueChange = { nisn = it },
                        label = { Text("NISN") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Text("Role:", fontSize = 14.sp,fontFamily = poppins, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = CenterVertically) {
                        RadioButton(
                            selected = role == "student",
                            onClick = { role = "student" },
                            colors = RadioButtonDefaults.colors(primaryBlue)
                        )
                        Text("Siswa",fontFamily = poppins,)
                        Spacer(Modifier.padding(horizontal = 4.dp))
                        RadioButton(
                            selected = role == "teacher",
                            onClick = { role = "teacher" },
                            colors = RadioButtonDefaults.colors(primaryBlue)
                        )
                        Text("Guru",fontFamily = poppins,)
                    }
                    if (role == "student") {
                        OutlinedTextField(
                            value = userClass,
                            onValueChange = { userClass = it },
                            label = { Text("Kelas (Contoh: 12-RPL-1)",fontFamily = poppins,) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.registerUser(AddUserRequest(name, email, password, nisn ,role, userClass))
                    showAddDialog = false
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryBlue,
                    contentColor = Color.White
                ),
                ) { Text("Simpan",fontFamily = poppins,) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDialog = false
                },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = primaryBlue
                    ),
                ) { Text("Batal",fontFamily = poppins,) }
            }
        )
    }

    if (showEditDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showEditDialog = false },
            title = { Text("Ubah Role Pengguna", fontWeight = FontWeight.SemiBold, fontFamily = poppins,) },
            text = {
                Column {
                    Text("Pilih role baru untuk ${selectedUserForEdit?.second}:", fontSize = 14.sp, fontFamily = poppins,)
                    Spacer(Modifier.height(12.dp))
                    listOf("admin", "teacher", "student").forEach { role ->
                        Row(
                            verticalAlignment = CenterVertically,
                            modifier = Modifier.fillMaxWidth().clickable { selectedNewRole = role }
                        ) {
                            RadioButton(
                                colors = RadioButtonDefaults.colors(
                                    primaryBlue
                                ),
                                selected = selectedNewRole == role,
                                onClick = { selectedNewRole = role }
                            )
                            Text(role.replaceFirstChar { it.uppercase() },fontFamily = poppins,)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedUserForEdit?.let { (id, _) ->
                            viewModel.updateUser(id, selectedNewRole)
                        }
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBlue,
                        contentColor = Color.White
                    ),
                ) { Text("Simpan", fontFamily = poppins,) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = primaryBlue
                    ),
                ) { Text("Batal",fontFamily = poppins,) }
            }
        )
    }

    if (showConfirmDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showEditDialog = false },
            title = { Text("Yakin Ingin Menghapus ${selectedUserForDelete?.second}?", fontWeight = FontWeight.SemiBold, fontFamily = poppins,) },
            confirmButton = {
                Button(
                    onClick = {
                        selectedUserForDelete?.let { (id) ->
                            viewModel.deleteUser(userId = id)
                        }
                        showConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBlue,
                        contentColor = Color.White
                    ),
                ) { Text("Hapus Saja", fontFamily = poppins,) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = primaryBlue
                    ),
                ) { Text("Batal", fontFamily = poppins,) }
            }
        )
    }

    LaunchedEffect(Unit){
        viewModel.loadAllUsers()
    }

    LaunchedEffect(errorUpdate) {
        errorUpdate?.let {
            ToastHelper().Toast(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Pengguna", fontWeight = FontWeight.SemiBold, fontFamily = poppins,) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(end = 20.dp, bottom = 750.dp),
                onClick = { showAddDialog = true },
                containerColor = primaryBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah User")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightGray)
        ) {
            Surface(
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        placeholder = { Text("Cari nama atau NISN...", fontFamily = poppins,) },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = Color.White,
                        contentColor = primaryBlue,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = primaryBlue
                            )
                        }
                    ) {
                        Tab(
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 },
                            text = { Text("Siswa", fontWeight = FontWeight.SemiBold,fontFamily = poppins,) }
                        )
                        Tab(
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 },
                            text = { Text("Guru", fontWeight = FontWeight.SemiBold,fontFamily = poppins) }
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isLoading && allUsers.isEmpty()) {
                    item {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            color = primaryBlue
                        )
                    }
                }

                if (filteredUsers.isEmpty() && !isLoading) {
                    item {
                        Text(
                            "Tidak ada data pengguna",
                            fontFamily = poppins,
                            modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }

                items(filteredUsers) { user ->
                    UserCard(
                        name = user.name,
                        idNumber = user.nisn ?: "ID: ${user.id}",
                        subInfo = if (user.role == "student") "Kelas: ${user.`class` ?: "-"}" else "Guru / Staff",
                        onDelete = {
                             selectedUserForDelete = Pair(user.id, user.name)
                             showConfirmDialog = true
                        },
                        onEdit = {
                            selectedUserForEdit = Pair(user.id, user.name)
                            selectedNewRole = user.role
                            showEditDialog = true
                        }
                    )
                }
            }
        }
    }
}