package org.d3if0021.reminderpartner.ui.screen.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.d3if0021.reminderpartners.model.Aktivitas

@Composable
fun ConfirmDeleteDialog(
    aktivitas: Aktivitas,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Konfirmasi Penghapusan") },
        text = { Text(text = "Apakah Anda yakin ingin menghapus aktivitas ini?") },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = { onConfirm() }) {
                Text("Hapus")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = { onDismiss() }) {
                Text("Batal")
            }
        }
    )
}