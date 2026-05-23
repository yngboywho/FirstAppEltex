package com.eltex.firstapp.feature.event.data

import kotlinx.serialization.Serializable

@Serializable
data class EventRequest(
    val content: String,
    val type: String,
    val datetime: String,
    val link: String? = null,
)
