package com.diwan.myprofileapp.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diwan.myprofileapp.shared.ai.ChatMessage
import com.diwan.myprofileapp.shared.ai.ChatUiState
import com.diwan.myprofileapp.shared.ai.GeminiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val geminiService: GeminiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(message: String) {
        if (message.isBlank()) return

        _uiState.update {
            it.copy(
                messages = it.messages + ChatMessage(message, isUser = true),
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            geminiService.chat(message)
                .onSuccess { reply ->
                    _uiState.update {
                        it.copy(
                            messages = it.messages + ChatMessage(reply, isUser = false),
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Terjadi kesalahan",
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun clearChat() {
        geminiService.clearHistory()
        _uiState.value = ChatUiState()
    }

    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
}
