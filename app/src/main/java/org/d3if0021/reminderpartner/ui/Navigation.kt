package org.d3if0021.reminderpartner.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import org.d3if0021.reminderpartner.ui.screen.main.CalendarScreen
import org.d3if0021.reminderpartner.ui.screen.main.ListScreen
import org.d3if0021.reminderpartner.ui.screen.main.MainViewModel

enum class Screen(val route: String) {
    LIST("list"),
    CALENDAR("calendar")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    user: FirebaseUser,
    viewModel: MainViewModel,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LIST.route,
        modifier = modifier
    ) {
        composable(Screen.LIST.route) {
            ListScreen(
                user = user,
                viewModel = viewModel,
                onFabClick = onFabClick
            )
        }
        composable(Screen.CALENDAR.route) {
            CalendarScreen(
                user = user,
                viewModel = viewModel,
                onFabClick = onFabClick
            )
        }
    }
}