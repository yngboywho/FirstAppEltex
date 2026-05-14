package com.eltex.firstapp.feature.event.data

import com.eltex.firstapp.feature.event.domain.Event
import com.eltex.firstapp.feature.event.domain.EventsRepository
import java.time.LocalDateTime

class EventsRepositoryImpl(private val dao: EventDao) : EventsRepository {
    override fun getAll(): List<Event> =
        dao.getAll().map { it.toDomain() }

    override fun save(
        content: String,
        author: String,
        status: String,
        visit: String,
        link: String,
    ): Event {
        val entity = EventEntity(
            publishedAt = LocalDateTime.now().toString(),
            status = status,
            visit = visit,
            content = content,
            author = author,
            link = link,
        )
        val id = dao.insert(entity)
        return dao.getById(id).toDomain()
    }

    override fun update(
        id: Long,
        content: String,
    ): Event {
        val entity = dao.getById(id)
        dao.update(entity.copy(content = content))
        return dao.getById(id).toDomain()
    }

    override fun likeById(id: Long): Event {
        val entity = dao.getById(id)
        val updated = if (entity.likedByMe) {
            entity.copy(likedByMe = false, likes = entity.likes - 1)
        } else {
            entity.copy(likedByMe = true, likes = entity.likes + 1)
        }

        dao.update(updated)
        return updated.toDomain()
    }

    override fun participateById(id: Long): Event {
        val entity = dao.getById(id)
        val updated = if (entity.participantsByMe) {
            entity.copy(participantsByMe = false, participants = entity.participants - 1)
        } else {
            entity.copy(participantsByMe = true, participants = entity.participants + 1)
        }

        dao.update(updated)
        return updated.toDomain()
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
    }

}