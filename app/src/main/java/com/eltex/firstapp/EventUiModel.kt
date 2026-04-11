package com.eltex.firstapp

data class EventUiModel(
    val id: Long = 0,
    val published: String = "",
    val status: String = "",
    val visit: String = "",
    val content: String = "",
    val author: String = "",
    val likes: Int = 0,
    val likedByMe: Boolean = false,
    val participants: Int = 0,
    val participantsByMe: Boolean = false,
)