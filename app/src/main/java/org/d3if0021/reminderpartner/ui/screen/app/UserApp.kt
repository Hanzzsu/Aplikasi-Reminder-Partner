package org.d3if0021.reminderpartner.ui.screen.app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.d3if0021.reminderpartner.R
import org.d3if0021.reminderpartner.ui.screen.main.MainScreen
import org.d3if0021.shared.ui.WelcomeScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserApp(){
    val viewModel: AppViewModel = viewModel()
    val userFlow by viewModel.userFlow.collectAsState()

    if (userFlow == null){
        Scaffold { innerPadding ->
            WelcomeScreen(
                appLogo = R.mipmap.ic_launcher ,
                appName = R.string.app_name,
                modifier = Modifier.padding(innerPadding)
            )
        }

    }
    userFlow?.let{ MainScreen(it) }
}
