package com.example.android_assignment


import com.example.android_assignment.ui.theme.Android_assignmentTheme
import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.messaging.FirebaseMessaging
import com.example.android_assignment.navigation.SoundverseNavigation
import com.example.android_assignment.utils.LocalNotificationHelper
import com.example.android_assignment.utils.NotificationPermissionHelper
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    private var notificationTitle by mutableStateOf<String?>(null)
    private var notificationMessage by mutableStateOf<String?>(null)
    private var showNotificationDialog by mutableStateOf(false)
    private var initialRoute by mutableStateOf<String?>(null)

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.soundverse.app.NOTIFICATION_RECEIVED") {
                notificationTitle = intent.getStringExtra("title")
                notificationMessage = intent.getStringExtra("message")
                showNotificationDialog = true
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            initializeFCM()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create notification channel
        LocalNotificationHelper.createNotificationChannel(this)

        // Register broadcast receiver for foreground notifications
        registerReceiver(
            notificationReceiver,
            IntentFilter("com.soundverse.app.NOTIFICATION_RECEIVED"), RECEIVER_NOT_EXPORTED
        )

        // Request notification permission
        if (!NotificationPermissionHelper.hasNotificationPermission(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            initializeFCM()
        }

        // Handle notification click from intent
        handleNotificationIntent(intent)

        setContent {
            Android_assignmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotificationDialogHandler(
                        showDialog = showNotificationDialog,
                        title = notificationTitle ?: "",
                        message = notificationMessage ?: "",
                        onDismiss = {
                            showNotificationDialog = false
                            notificationTitle = null
                            notificationMessage = null
                        }
                    )
                    SoundverseNavigation(initialRoute = initialRoute)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(notificationReceiver)
        } catch (e: Exception) {
            // Receiver might not be registered
        }
    }

    override fun onNewIntent(intent: Intent) {
        if (intent != null) {
            super.onNewIntent(intent)
        }
        intent?.let { handleNotificationIntent(it) }
    }

    private fun handleNotificationIntent(intent: Intent) {
        if (intent.getBooleanExtra("notification_clicked", false)) {
            initialRoute = "export_state"
            notificationTitle = intent.getStringExtra("title")
            notificationMessage = intent.getStringExtra("message")
        }
    }

private fun initializeFCM() {
    // Try to initialize if not already
    if (FirebaseApp.getApps(this).isEmpty()) {
        FirebaseApp.initializeApp(this)
    }

    // If still not initialized, just skip to avoid crash
    if (FirebaseApp.getApps(this).isEmpty()) {
        Log.e("MainActivity", "Firebase not initialized, skipping FCM setup")
        return
    }

    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.e("MainActivity", "Fetching FCM registration token failed", task.exception)
            return@addOnCompleteListener
        }
        val token = task.result
        Log.d("MainActivity", "FCM token: $token")
        // send to server if needed
    }
}

}

@Composable
fun NotificationDialogHandler(
    showDialog: Boolean,
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    if (showDialog && title.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("OK")
                }
            }
        )
    }
}

