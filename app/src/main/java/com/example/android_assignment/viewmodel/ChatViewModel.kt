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
            Message("1", "Hello there! I’m Soundverse Assistant, your music AI co-pilot. Let's get started with your project. ", false),
            Message("2", "You can write a prompt in the text box below. Try mentioning instruments, scene, story, genre, scale etc to generate an audio clip. More things you mention, the better the output will be.",false),
            Message("3", "For e.g. you can write \"Compose a hauntingly beautiful piano solo that captures the essence of melancholy and nostalgia. The melody should evoke a sense of longing and introspection, while the harmonies add depth and emotion to the piece.\"", false),
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

