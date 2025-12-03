package com.example.android_assignment.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.android_assignment.MainActivity
import com.example.android_assignment.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // Send token to server if needed
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if app is in foreground
        val isAppInForeground = isAppInForeground()

        // Check if message contains data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            val title = remoteMessage.data["title"] ?: "New Notification"
            val message = remoteMessage.data["message"] ?: "You have a new notification"

            if (isAppInForeground) {
                // Show alert dialog when app is in foreground
                showForegroundNotification(title, message)
            } else {
                handleDataMessage(remoteMessage.data)
            }
        }

        // Check if message contains notification payload
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            val title = it.title ?: "New Notification"
            val message = it.body ?: ""

            if (isAppInForeground) {
                showForegroundNotification(title, message)
            } else {
                sendNotification(title, message)
            }
        }
    }

    private fun isAppInForeground(): Boolean {
        // Simple check - in production, use ActivityManager or LifecycleObserver
        // For now, we'll always show notification but MainActivity will handle dialog
        return false
    }

    private fun showForegroundNotification(title: String, message: String) {
        // Send broadcast or use EventBus to notify MainActivity
        val intent = Intent("com.soundverse.app.NOTIFICATION_RECEIVED").apply {
            putExtra("title", title)
            putExtra("message", message)
        }
        sendBroadcast(intent)

        // Also show notification in notification bar
        sendNotification(title, message)
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val title = data["title"] ?: "New Notification"
        val message = data["message"] ?: "You have a new notification"
        sendNotification(title, message)
    }

    private fun sendNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("notification_clicked", true)
            putExtra("title", title)
            putExtra("message", messageBody)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Soundverse Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for Soundverse app"
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    companion object {
        private const val TAG = "FirebaseMsgService"
    }
}

