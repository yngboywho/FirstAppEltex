package com.eltex.firstapp.feature.post.add

interface AddPostEffect {
    data class Saved(val text: String): AddPostEffect
}