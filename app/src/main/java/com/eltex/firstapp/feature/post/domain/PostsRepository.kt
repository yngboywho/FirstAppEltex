package com.eltex.firstapp.feature.post.domain

interface PostsRepository {
    fun getAll(): List<Post>
    fun save(content: String, author: String, link: String = ""): Post
    fun update(id: Long, content: String): Post
    fun likeById(id: Long): Post
    fun deleteById(id: Long)
}