package org.d3if0021.shared.ui

import android.content.Intent
import android.graphics.drawable.AdaptiveIconDrawable
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import org.d3if0021.reminderpartners.R

@Composable
private fun adaptiveIconPainResource(@DrawableRes id: Int): Painter {
    val res = LocalContext.current.resources
    val theme = LocalContext.current.theme

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val adaptiveIcon = ResourcesCompat.getDrawable(res, id, theme) as? AdaptiveIconDrawable
        if (adaptiveIcon != null) {
            BitmapPainter(adaptiveIcon.toBitmap().asImageBitmap())
        } else {
            painterResource(id)
        }
    } else {
        painterResource(id)
    }
}

@Composable
fun WelcomeScreen(
    @DrawableRes appLogo: Int,
    @StringRes appName: Int,
    modifier: Modifier = Modifier
) {
    val contract = FirebaseAuthUIActivityResultContract()
    val launcher = rememberLauncherForActivityResult(contract) { }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3E6FF), Color(0xFFA6A9FF))
                )
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display App Logo with Border
        Image(
            painter = adaptiveIconPainResource(appLogo),
            contentDescription = stringResource(appName),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(bottom = 16.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape) // Thin border around the app logo
        )

        // App Name Title in Bold with Black Color
        Text(
            text = stringResource(appName),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold, // Make the app name text bold
            color = Color.Black,          // Set text color to black
            modifier = Modifier.padding(bottom = 32.dp)
        )


        // Google Sign-In Button
        Button(
            onClick = { launcher.launch(getSigninIntent()) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(0.85f)  // Slightly wider button
                .height(56.dp)        // Taller button for a larger appearance
                .border(1.dp, Color.LightGray, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp) // Adjusted shape for larger button
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Larger Google Icon
                Image(
                    painter = painterResource(R.drawable.g_icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp) // Larger icon
                )

                // Button Text with increased font size and reduced padding
                Text(
                    text = "Sign in with Google",
                    fontSize = 18.sp,   // Larger text
                    color = Color.Black,
                    modifier = Modifier.padding(start = 4.dp) // Reduced padding between icon and text
                )
            }
        }

    }
}


private fun getSigninIntent(): Intent {
    return AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(
            arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        )
        .build()
}
