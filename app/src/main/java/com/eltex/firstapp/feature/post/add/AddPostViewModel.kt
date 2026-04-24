package com.eltex.firstapp.feature.post.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AddPostViewModel : ViewModel() {
    var state by mutableStateOf(AddPostState())
        private set
    private val _effects = MutableSharedFlow<AddPostEffect>(extraBufferCapacity = 64)
    val effects = _effects.asSharedFlow()

    fun accept(message: AddPostMessage) {
        when (message) {
            AddPostMessage.Save -> _effects.tryEmit(AddPostEffect.Saved(state.text))
            is AddPostMessage.TextChanged -> state = state.copy(text = message.value)
        }
    }
}