package org.d3if0021.reminderpartner.ui.screen.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    user: FirebaseUser,
    viewModel: MainViewModel,
    onFabClick: () -> Unit
) {
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val today = LocalDate.now()
    val yearMonth = YearMonth.of(currentDate.year, currentDate.month)
    val firstDayOfMonth = yearMonth.atDay(1).dayOfWeek.value % 7 // Adjust for Sunday as 0
    val daysInMonth = yearMonth.lengthOfMonth()

    // Filter tasks based on selectedDate
    val filteredData = if (selectedDate != null) {
        viewModel.data.filter {
            val taskDate = it.tanggal.toDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            taskDate == selectedDate
        }
    } else {
        viewModel.data
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Calendar Section with purple background
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6C3998))
        ) {
            // Header Kalender
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    currentDate = currentDate.minusMonths(1)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous Month",
                        tint = Color.White
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentDate.month.getDisplayName(TextStyle.FULL, Locale("id"))
                            .replaceFirstChar { it.uppercase() },
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = currentDate.year.toString(),
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }

                IconButton(onClick = {
                    currentDate = currentDate.plusMonths(1)
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next Month",
                        tint = Color.White
                    )
                }
            }

            // Header Hari
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                    Text(
                        text = day,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

            // Grid Kalender
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                var dayOfMonth = 1
                for (week in 0 until (firstDayOfMonth + daysInMonth - 1) / 7 + 1) { // Iterate over rows (weeks)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (day in 0..6) { // Days of the week (Sunday to Saturday)
                            if ((week == 0 && day < firstDayOfMonth) || dayOfMonth > daysInMonth) {
                                Spacer(modifier = Modifier.size(40.dp)) // Empty space
                            } else {
                                val dateToShow = yearMonth.atDay(dayOfMonth)
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            when {
                                                dateToShow == today -> Color.White // Today is highlighted
                                                dateToShow == selectedDate -> Color(0xFF3E5879) // Highlight selected date
                                                else -> Color(0xFF6C3998) // Regular calendar day
                                            },
                                            shape = RoundedCornerShape(50)
                                        )
                                        .clickable {
                                            selectedDate = if (selectedDate == dateToShow) null else dateToShow
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = dayOfMonth.toString(),
                                        fontSize = 16.sp,
                                        color = if (dateToShow == today) Color.Black else Color.White
                                    )
                                }
                                dayOfMonth++
                            }
                        }
                    }
                }
            }
        }

        // Task Section (Daftar Tugas) with white background
        // Bagian LazyColumn di dalam CalendarScreen
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            if (filteredData.isEmpty()) {
                item {
                    Text(
                        text = "No tasks found",
                        modifier = Modifier
                            .padding(16.dp),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                items(filteredData) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Nama Aktivitas
                            Column {
                                Text(
                                    text = item.nama,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                // Tampilkan tanggal dan waktu aktivitas
                                Text(
                                    text = "Jam: ${item.tanggal.toLocalTimeString()}",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}