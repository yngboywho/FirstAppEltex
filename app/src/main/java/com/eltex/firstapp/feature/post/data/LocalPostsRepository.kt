package com.eltex.firstapp.feature.post.data

import android.content.Context
import android.content.SharedPreferences
import com.eltex.firstapp.feature.post.domain.Post
import com.eltex.firstapp.feature.post.domain.PostsRepository
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDateTime

class LocalPostsRepository(context: Context) : PostsRepository {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private var nextId: Long
        get() = prefs.getLong(KEY_NEXT_ID, 1L)
        set(value) = prefs.edit().putLong(KEY_NEXT_ID, value).apply()

    private val postsFile: File = File(context.filesDir, FILE_NAME)

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private var cache: MutableList<Post> = loadFromFile().toMutableList()

    override fun getAll(): List<Post> = cache.toList()

    override fun save(content: String, author: String, link: String): Post {
        val post = Post(
            id = nextId++,
            content = content,
            author = author,
            publishedAt = LocalDateTime.now().toString(),
            link = link,
        )
        cache.add(0, post)
        saveToFile()
        return post
    }

    override fun update(id: Long, content: String): Post {
        val updated = requiredById(id).copy(content = content)
        replaceInCache(updated)
        saveToFile()
        return updated
    }

    override fun likeById(id: Long): Post {
        val current = requiredById(id)
        val updated = if (current.likedByMe) {
            current.copy(likedByMe = false, likes = current.likes - 1)
        } else {
            current.copy(likedByMe = true, likes = current.likes + 1)
        }
        replaceInCache(updated)
        saveToFile()
        return updated
    }

    override fun deleteById(id: Long) {
        cache.removeAll { it.id == id }
        saveToFile()
    }

    private fun requiredById(id: Long): Post =
        cache.firstOrNull { it.id == id } ?: error("Post with id=$id not found")

    private fun replaceInCache(updated: Post) {
        val index = cache.indexOfFirst { it.id == updated.id }
        if (index != -1) cache[index] = updated
    }

    private fun loadFromFile(): List<Post> {
        if (!postsFile.exists()) return emptyList()
        return try {
            json.decodeFromString<List<Post>>(postsFile.readText())
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveToFile() {
        postsFile.writeText(json.encodeToString(cache))
    }

    private companion object {
        const val PREFS_NAME = "posts_prefs"
        const val KEY_NEXT_ID = "next_id"
        const val FILE_NAME = "posts.json"
    }
}