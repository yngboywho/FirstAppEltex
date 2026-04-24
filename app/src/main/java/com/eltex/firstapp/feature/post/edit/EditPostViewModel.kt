package com.eltex.firstapp.feature.post.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EditEventViewModel(
    private val initialId: Long,
    private val initialContent: String,
) : ViewModel() {

    var state by mutableStateOf(
        EditPostState(id = initialId, content = initialContent)
    )
        private set

    private val _effects = MutableSharedFlow<EditPostEffect>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    fun accept(message: EditPostMessage) {
        state = reduce(state, message)
    }

    private fun reduce(
        current: EditPostState,
        message: EditPostMessage,
    ): EditPostState = when (message) {
        is EditPostMessage.ContentChanged ->
            current.copy(content = message.value)

        EditPostMessage.Save -> {
            _effects.tryEmit(EditPostEffect.NavigateBack(current.id, current.content))
            current
        }
    }
}