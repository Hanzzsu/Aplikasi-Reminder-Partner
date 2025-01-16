package org.d3if0021.reminderpartner.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.d3if0021.reminderpartner.R

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = Color(0xFFE0E0E0)
    ) {
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.baseline_list_24), contentDescription = "List") },
            label = { Text("List") },
            selected = currentDestination == "list",
            onClick = { navController.navigate("list") { launchSingleTop = true } }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.baseline_calendar_month_24), contentDescription = "Calendar") },
            label = { Text("Calendar") },
            selected = currentDestination == "calendar",
            onClick = { navController.navigate("calendar") { launchSingleTop = true } }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.baseline_account_circle_24), contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentDestination == "profile",
            onClick = { navController.navigate("profile") { launchSingleTop = true } }
        )
    }
}
