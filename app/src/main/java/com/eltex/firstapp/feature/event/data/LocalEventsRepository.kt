package com.eltex.firstapp.feature.event.data

import android.content.Context
import android.content.SharedPreferences
import com.eltex.firstapp.feature.event.domain.Event
import com.eltex.firstapp.feature.event.domain.EventsRepository
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDateTime

class LocalEventsRepository(context: Context): EventsRepository {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private var nextId: Long
        get() = prefs.getLong(KEY_NEXT_ID, 1L)
        set(value) = prefs.edit().putLong(KEY_NEXT_ID, value).apply()

    private val eventsFile: File = File(context.filesDir, FILE_NAME)

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private var cache: MutableList<Event> = loadFromFile().toMutableList()

    override fun getAll(): List<Event> = cache.toList()

    override fun save(
        content: String,
        author: String,
        status: String,
        visit: String,
        link: String
    ): Event {
        val event = Event(
            id = nextId++,
            content = content,
            author = author,
            publishedAt = LocalDateTime.now().toString(),
            status = status,
            visit = visit,
            link = link,
        )
        cache.add(0, event)
        saveToFile()
        return event
    }

    override fun update(
        id: Long,
        content: String,
    ): Event {
        val updated = requiredById(id).copy(content = content)
        replaceInCache(updated)
        saveToFile()
        return updated
    }

    override fun likeById(id: Long): Event {
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

    override fun participateById(id: Long): Event {
        val current = requiredById(id)
        val updated = if (current.participantsByMe) {
            current.copy(participantsByMe = false, participants = current.participants - 1)
        } else {
            current.copy(participantsByMe = true, participants = current.participants + 1)
        }
        replaceInCache(updated)
        saveToFile()
        return updated
    }

    override fun deleteById(id: Long) {
        cache.removeAll { it.id == id }
        saveToFile()
    }

    private fun requiredById(id: Long): Event =
        cache.firstOrNull { it.id == id } ?: error("Events with id=$id not found")

    private fun replaceInCache(updated: Event) {
        val index = cache.indexOfFirst { it.id == updated.id }
        if (index != -1) cache[index] = updated
    }

    private fun loadFromFile(): List<Event> {
        if (!eventsFile.exists()) return emptyList()
        return try {
            json.decodeFromString<List<Event>>(eventsFile.readText())
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveToFile() {
        eventsFile.writeText(json.encodeToString(cache))
    }

    private companion object {
        const val PREFS_NAME = "events_prefs"
        const val KEY_NEXT_ID = "next_id"
        const val FILE_NAME = "events.json"
    }
}