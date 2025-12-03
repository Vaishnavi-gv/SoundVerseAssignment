package com.example.android_assignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_assignment.screens.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(
        listOf(
            Message("1", "Hello! How can I help you today?", false),
            Message("2", "Hi! I'm looking for some information.", true),
            Message("3", "Sure! What would you like to know?", false),
            Message("4", "Can you tell me about Soundverse?", true),
            Message("5", "Soundverse is a platform for creating and sharing audio content. It offers various features for content creators.", false)
        )
    )
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isMenuOpen = MutableStateFlow(false)
    val isMenuOpen: StateFlow<Boolean> = _isMenuOpen.asStateFlow()

    fun toggleMenu() {
        _isMenuOpen.value = !_isMenuOpen.value
    }

    fun openMenu() {
        _isMenuOpen.value = true
    }

    fun closeMenu() {
        _isMenuOpen.value = false
    }

    fun addMessage(text: String, isUser: Boolean) {
        viewModelScope.launch {
            val newMessage = Message(
                id = System.currentTimeMillis().toString(),
                text = text,
                isUser = isUser
            )
            _messages.value = _messages.value + newMessage
        }
    }
}

