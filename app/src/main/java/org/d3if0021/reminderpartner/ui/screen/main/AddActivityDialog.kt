package org.d3if0021.reminderpartner.ui.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

@SuppressLint("DefaultLocale")
@Composable
fun AddActivityDialog(
    onDismissRequest: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("00:00") }
    var date by remember { mutableStateOf("yyyy-MM-dd") }
    var showError by remember { mutableStateOf(false) }
    var showDateError by remember { mutableStateOf(false) }
    var showTimeError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = remember {
        android.app.TimePickerDialog(
            context,
            { _, hour, minute ->
                time = String.format("%02d:%02d", hour, minute)
                showTimeError = false
            },
            currentHour,
            currentMinute,
            true
        )
    }

    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                showDateError = false
            },
            currentYear,
            currentMonth,
            currentDay
        ).apply {
            datePicker.minDate = calendar.timeInMillis
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Add New Activity") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        showError = false // Reset error jika pengguna mulai mengetik
                    },
                    label = { Text("Activity Name") },
                    isError = showError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (showError && name.isEmpty()) {
                    Text(
                        text = "Activity name cannot be empty",
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Date: $date", modifier = Modifier.weight(1f))
                    Button(onClick = { datePickerDialog.show() }) {
                        Text("Choose Date")
                    }
                }
                if (showDateError && date == "yyyy-MM-dd") {
                    Text(
                        text = "Please choose a date",
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Time: $time", modifier = Modifier.weight(1f))
                    Button(onClick = { timePickerDialog.show() }) {
                        Text("Choose Time")
                    }
                }
                if (showTimeError && time == "00:00") {
                    Text(
                        text = "Please choose a time",
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    showError = name.isBlank()
                    showDateError = date == "yyyy-MM-dd"
                    showTimeError = time == "00:00"
                    if (!showError && !showDateError && !showTimeError) {
                        onSave(name, date, time)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun EditActivityDialog(
    initialName: String,
    initialDate: String,
    initialTime: String,
    onDismissRequest: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var time by remember { mutableStateOf(initialTime) }
    var date by remember { mutableStateOf(initialDate) }
    var showError by remember { mutableStateOf(false) }
    var showDateError by remember { mutableStateOf(false) }
    var showTimeError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val timePickerDialog = remember {
        android.app.TimePickerDialog(
            context,
            { _, hour, minute ->
                time = String.format("%02d:%02d", hour, minute)
                showTimeError = false
            },
            time.split(":")[0].toInt(),
            time.split(":")[1].toInt(),
            true
        )
    }

    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                showDateError = false
            },
            date.split("-")[0].toInt(),
            date.split("-")[1].toInt() - 1,
            date.split("-")[2].toInt()
        )
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Edit Activity") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        showError = false
                    },
                    label = { Text("Activity Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (showError && name.isEmpty()) {
                    Text(
                        text = "Activity name cannot be empty",
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Date: $date", modifier = Modifier.weight(1f))
                    Button(onClick = { datePickerDialog.show() }) {
                        Text("Choose Date")
                    }
                }
                if (showDateError && date.isBlank()) {
                    Text(
                        text = "Please choose a date",
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Time: $time", modifier = Modifier.weight(1f))
                    Button(onClick = { timePickerDialog.show() }) {
                        Text("Choose Time")
                    }
                }
                if (showTimeError && time.isBlank()) {
                    Text(
                        text = "Please choose a time",
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    showError = name.isBlank()
                    showDateError = date.isBlank()
                    showTimeError = time.isBlank()
                    if (!showError && !showDateError && !showTimeError) {
                        onSave(name, date, time)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

