package org.d3if0021.reminderpartner.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser
import org.d3if0021.reminderpartner.R
import org.d3if0021.reminderpartners.model.Aktivitas
import org.d3if0021.reminderpartners.ui.LanguageAndLogoutMenu
import org.d3if0021.reminderpartners.ui.UserProfileCard

@Composable
fun ProfileScreen(user: FirebaseUser, mainViewModel: MainViewModel) {
    // Observe state from ViewModel
    val totalTasks by mainViewModel.totalTasks
    val totalPriorities by mainViewModel.totalPriorities

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Profile Card
            UserProfileCard(user)

            Spacer(modifier = Modifier.height(16.dp))

            // Settings Section
            SettingsSection()

            // Task Summary Section
            Text(
                text = "Task Summary",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TaskSummaryItem(number = totalTasks, label = "Total Task")
                Spacer(modifier = Modifier.width(8.dp))
                TaskSummaryItem(number = totalPriorities, label = "Priority")
            }

            // Upcoming Tasks Section
            UpcomingTasksSection(mainViewModel.data)
        }
    }
}
@Composable
fun TaskSummaryItem(number: Int, label: String) {
    Column(
        modifier = Modifier
            .background(Color(0xFFF1E4FF), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .width(150.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$number",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSection() {
    var isSheetVisible by remember { mutableStateOf(false) }

    if (isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isSheetVisible = false }
        ) {
            LanguageAndLogoutMenu()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1E4FF), shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { isSheetVisible = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_settings_24),
                    contentDescription = "Settings Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun UpcomingTasksSection(tasks: List<Aktivitas>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFEDE7F6), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "To do list",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (tasks.isNotEmpty()) {
            tasks.forEach { task ->
                UpcomingTaskItem(task.nama)
            }
        } else {
            Text(
                text = "There is no activity yet.",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
@Composable
fun UpcomingTaskItem(taskName: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_calendar_today_24),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = taskName,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}