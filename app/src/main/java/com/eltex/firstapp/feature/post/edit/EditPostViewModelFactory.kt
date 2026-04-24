package com.eltex.firstapp.feature.post.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditEventViewModelFactory(
    private val id: Long,
    private val content: String,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        EditEventViewModel(initialId = id, initialContent = content) as T
}