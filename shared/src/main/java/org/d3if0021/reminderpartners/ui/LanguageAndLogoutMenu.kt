package org.d3if0021.reminderpartners.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.firebase.ui.auth.AuthUI
import org.d3if0021.reminderpartners.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageAndLogoutMenu() {
    val context = LocalContext.current

    // Tampilan background untuk menampilkan menu secara langsung
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1E4FF)) // Warna background menu
            .padding(16.dp)
    ) {
        // Pilihan Language
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable {
                    // Tambahkan logika untuk memilih bahasa di sini
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_language_24), // Ganti dengan ikon bahasa
                contentDescription = "Language Icon",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.language),
                color = Color.Black,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Garis pemisah (opsional)
        Divider(color = Color.Gray, thickness = 0.5.dp)

        // Pilihan Logout
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable {
                    AuthUI.getInstance().signOut(context) // Fungsi logout
                }
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Exit Icon",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.logout),
                color = Color.Black,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
