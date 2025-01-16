package org.d3if0021.reminderpartner.ui.screen.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import org.d3if0021.reminderpartner.R
import org.d3if0021.reminderpartner.ui.BottomNavigationBar
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(user: FirebaseUser) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val factory = ViewModelFactory(user.uid)
    val viewModel: MainViewModel = viewModel(factory = factory)

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentRoute) {
                            "list" -> stringResource(id = R.string.list_title)
                            "calendar" -> stringResource(id = R.string.calendar_title)
                            "profile" -> stringResource(id = R.string.profile_title)
                            else -> stringResource(id = R.string.app_name)
                        },
                        color = Color.White // Menambahkan warna putih untuk teks
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF6C3998),
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            if (currentRoute == "list" || currentRoute == "calendar") {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = stringResource(R.string.tambah_aktivitas),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = "list"
            ) {
                composable("list") {
                    ListScreen(user, viewModel) {
                        showDialog = true
                    }
                }
                composable("calendar") {
                    CalendarScreen(user, viewModel) {
                        showDialog = true
                    }
                }
                composable("profile") { ProfileScreen(user, mainViewModel = viewModel) }
            }
        }

        if (showDialog) {
            AddActivityDialog(
                onDismissRequest = { showDialog = false },
                onSave = { name, date, time -> // Tambahkan parameter date
                    val formattedDate = date.split("-").let {
                        // Mengonversi tanggal string "yyyy-MM-dd" menjadi Calendar
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.YEAR, it[0].toInt())
                        calendar.set(Calendar.MONTH, it[1].toInt() - 1) // Bulan dimulai dari 0
                        calendar.set(Calendar.DAY_OF_MONTH, it[2].toInt())
                        calendar
                    }

                    val formattedTime = time.split(":").let {
                        // Menambahkan waktu ke Calendar
                        formattedDate.set(Calendar.HOUR_OF_DAY, it[0].toInt())
                        formattedDate.set(Calendar.MINUTE, it[1].toInt())
                        formattedDate.time
                    }

                    viewModel.insert(name, formattedTime) // Simpan aktivitas
                    showDialog = false
                }
            )
        }
    }
}