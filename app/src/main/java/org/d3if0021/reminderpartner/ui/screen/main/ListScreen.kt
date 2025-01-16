package org.d3if0021.reminderpartner.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser
import org.d3if0021.reminderpartner.R
import org.d3if0021.reminderpartners.model.Aktivitas
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ListScreen(
    user: FirebaseUser,
    viewModel: MainViewModel,
    onFabClick: () -> Unit
) {
    val data = viewModel.data

    // Pisahkan aktivitas berdasarkan status checkbox
    val completedActivities = data.filter { it.checkbox == 1 }
    val pendingActivities = data.filter { it.checkbox == 0 }

    var isEditDialogOpen by remember { mutableStateOf(false) }
    var selectedActivity by remember { mutableStateOf<Aktivitas?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (data.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_24),
                    contentDescription = "No activities icon",
                    modifier = Modifier.size(150.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            LazyColumn {
                // Menampilkan aktivitas belum selesai
                if (pendingActivities.isNotEmpty()) {
                    items(pendingActivities) { item ->
                        SwipableActivityCard(
                            item = item,
                            onEditClick = { aktivitas ->
                                selectedActivity = aktivitas
                                isEditDialogOpen = true
                            },
                            onDeleteClick = { aktivitas ->
                                viewModel.delete(aktivitas)
                            },
                            onCheckboxClick = { aktivitas, newChecked ->
                                viewModel.updateCheckbox(aktivitas, newChecked)
                            },
                            onPriorityClick = { aktivitas, isPriority ->
                                viewModel.updatePriority(aktivitas, isPriority)
                            }
                        )
                    }
                }

                // Separator untuk aktivitas selesai
                if (completedActivities.isNotEmpty()) {
                    item {
                        Text(
                            text = "Activity completed",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    // Menampilkan aktivitas selesai
                    items(completedActivities) { item ->
                        SwipableActivityCard(
                            item = item,
                            onEditClick = { aktivitas ->
                                selectedActivity = aktivitas
                                isEditDialogOpen = true
                            },
                            onDeleteClick = { aktivitas ->
                                viewModel.delete(aktivitas)
                            },
                            onCheckboxClick = { aktivitas, newChecked ->
                                viewModel.updateCheckbox(aktivitas, newChecked)
                            },
                            onPriorityClick = { aktivitas, isPriority ->
                                viewModel.updatePriority(aktivitas, isPriority)
                            }
                        )
                    }
                }
            }
        }

        // Dialog edit aktivitas
        if (isEditDialogOpen && selectedActivity != null) {
            EditActivityDialog(
                initialName = selectedActivity!!.nama,
                initialDate = selectedActivity!!.tanggal.toLocalDateString(),
                initialTime = selectedActivity!!.waktu.toLocalTimeString(),
                onDismissRequest = {
                    isEditDialogOpen = false
                },
                onSave = { name, date, time ->
                    val newDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        .parse("$date $time")
                    if (newDate != null) {
                        viewModel.updateActivity(selectedActivity!!, name, newDate)
                        isEditDialogOpen = false
                    }
                }
            )
        }
    }
}



