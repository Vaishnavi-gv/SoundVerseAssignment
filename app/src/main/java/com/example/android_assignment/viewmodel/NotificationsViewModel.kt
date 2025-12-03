package com.example.android_assignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_assignment.screens.NotificationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {
    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem(
                id = "1",
                title = "Export Complete",
                message = "Your audio export has been completed successfully.",
                timestamp = System.currentTimeMillis() - 3600000,
                isRead = false
            ),
            NotificationItem(
                id = "2",
                title = "New Message",
                message = "You have a new message from a follower.",
                timestamp = System.currentTimeMillis() - 7200000,
                isRead = true
            ),
            NotificationItem(
                id = "3",
                title = "Export Ready",
                message = "Your export is ready to be shared.",
                timestamp = System.currentTimeMillis() - 10800000,
                isRead = false
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    fun markAsRead(id: String) {
        viewModelScope.launch {
            _notifications.value = _notifications.value.map {
                if (it.id == id) it.copy(isRead = true) else it
            }
        }
    }

    fun addNotification(notification: NotificationItem) {
        viewModelScope.launch {
            _notifications.value = listOf(notification) + _notifications.value
        }
    }
}

