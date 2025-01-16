package org.d3if0021.reminderpartner.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import org.d3if0021.reminderpartner.R
import org.d3if0021.reminderpartners.model.Aktivitas
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SwipableActivityCard(
    item: Aktivitas,
    onEditClick: (Aktivitas) -> Unit,
    onDeleteClick: (Aktivitas) -> Unit,
    onCheckboxClick: (Aktivitas, Boolean) -> Unit,
    onPriorityClick: (Aktivitas, Boolean) -> Unit
) {
    var isChecked by remember { mutableStateOf(item.checkbox == 1) }
    var isPriority by remember { mutableStateOf(item.priority == 1) }
    var showDialog by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }
    val maxOffsetX = -10f // Batas maksimal untuk menggeser ke kiri

    if (showDialog) {
        ConfirmDeleteDialog(
            aktivitas = item,
            onConfirm = {
                onDeleteClick(item)
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        // Kembalikan posisi kartu jika tidak dihapus atau diedit
                        if (offsetX > 0) offsetX = 0f
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        offsetX += dragAmount
                        if (offsetX > 0) offsetX = 0f // Tidak bisa digeser ke kanan
                        if (offsetX < maxOffsetX) offsetX = maxOffsetX // Batas geser ke kiri
                    }
                )
            }
    ) {
        // Kartu Utama
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .offset(x = offsetX.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = Color(0xFFBB9CDF))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Checkbox
                    Image(
                        painter = if (isChecked) painterResource(id = R.drawable.baseline_check_circle_24)
                        else painterResource(id = R.drawable.baseline_radio_button_unchecked_24),
                        contentDescription = if (isChecked) "Checked" else "Unchecked",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                                isChecked = !isChecked
                                onCheckboxClick(item, isChecked)
                            }
                    )

                    // Nama dan Tanggal Aktivitas
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.nama,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textDecoration = if (isChecked) TextDecoration.LineThrough else null // Efek teks tercoret
                        )
                        Text(
                            text = "${item.tanggal.toLocalDateString()} ${item.waktu.toLocalTimeString()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    // Ikon Prioritas
                    Image(
                        painter = if (isPriority) painterResource(id = R.drawable.baseline_star_24)
                        else painterResource(id = R.drawable.baseline_star_border_24),
                        contentDescription = if (isPriority) "Priority On" else "Priority Off",
                        modifier = Modifier.clickable {
                            isPriority = !isPriority
                            onPriorityClick(item, isPriority)
                        }
                    )
                }
            }

            // Ikon Edit dan Delete jika geser ke kiri
            if (offsetX <= maxOffsetX) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 2.dp), // Jarak antar ikon
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tombol Edit
                    IconButton(onClick = { onEditClick(item) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_mode_edit_24),
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    // Tombol Hapus
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_24),
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

// Fungsi Ekstensi untuk Format Tanggal
fun Timestamp.toLocalDateString(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return dateFormat.format(this.toDate())
}

fun Timestamp.toLocalTimeString(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(this.toDate())
}
