package com.eltex.firstapp.feature.event.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.eltex.firstapp.feature.event.domain.Event
import com.eltex.firstapp.feature.event.domain.EventsRepository
import java.time.LocalDateTime

class SQLiteEventRepository(context: Context) : EventsRepository {

    private val helper = EventsDbHelper(context)

    override fun getAll(): List<Event> {
        val db = helper.readableDatabase
        val cursor = db.query(TABLE_EVENTS, null, null, null, null, null, "$COL_ID DESC")
        return cursor.use { c ->
            buildList {
                while (c.moveToNext()) add(c.toEvent())
            }
        }
    }

    override fun save(content: String, author: String, status: String, visit: String, link: String): Event {
        val db = helper.writableDatabase
        val values = ContentValues().apply {
            put(COL_PUBLISHED_AT, LocalDateTime.now().toString())
            put(COL_PUBLISHED, "")
            put(COL_STATUS, status)
            put(COL_VISIT, visit)
            put(COL_CONTENT, content)
            put(COL_AUTHOR, author)
            put(COL_LINK, link)
            put(COL_LIKES, 0)
            put(COL_LIKED_BY_ME, 0)
            put(COL_PARTICIPANTS, 0)
            put(COL_PARTICIPANTS_BY_ME, 0)
        }
        val id = db.insert(TABLE_EVENTS, null, values)
        return requireById(id)
    }

    override fun update(id: Long, content: String): Event {
        val db = helper.writableDatabase
        val values = ContentValues().apply { put(COL_CONTENT, content) }
        db.update(TABLE_EVENTS, values, "$COL_ID = ?", arrayOf(id.toString()))
        return requireById(id)
    }

    override fun likeById(id: Long): Event {
        val current = requireById(id)
        val db = helper.writableDatabase
        val values = ContentValues().apply {
            if (current.likedByMe) {
                put(COL_LIKED_BY_ME, 0)
                put(COL_LIKES, current.likes - 1)
            } else {
                put(COL_LIKED_BY_ME, 1)
                put(COL_LIKES, current.likes + 1)
            }
        }
        db.update(TABLE_EVENTS, values, "$COL_ID = ?", arrayOf(id.toString()))
        return requireById(id)
    }

    override fun participateById(id: Long): Event {
        val current = requireById(id)
        val db = helper.writableDatabase
        val values = ContentValues().apply {
            if (current.participantsByMe) {
                put(COL_PARTICIPANTS_BY_ME, 0)
                put(COL_PARTICIPANTS, current.participants - 1)
            } else {
                put(COL_PARTICIPANTS_BY_ME, 1)
                put(COL_PARTICIPANTS, current.participants + 1)
            }
        }
        db.update(TABLE_EVENTS, values, "$COL_ID = ?", arrayOf(id.toString()))
        return requireById(id)
    }

    override fun deleteById(id: Long) {
        helper.writableDatabase.delete(TABLE_EVENTS, "$COL_ID = ?", arrayOf(id.toString()))
    }

    private fun requireById(id: Long): Event {
        val cursor = helper.readableDatabase.query(
            TABLE_EVENTS, null, "$COL_ID = ?", arrayOf(id.toString()), null, null, null
        )
        return cursor.use { c ->
            check(c.moveToFirst()) { "Event with id=$id not found" }
            c.toEvent()
        }
    }

    private fun Cursor.toEvent() = Event(
        id = getLong(getColumnIndexOrThrow(COL_ID)),
        publishedAt = getString(getColumnIndexOrThrow(COL_PUBLISHED_AT)),
        published = getString(getColumnIndexOrThrow(COL_PUBLISHED)),
        status = getString(getColumnIndexOrThrow(COL_STATUS)),
        visit = getString(getColumnIndexOrThrow(COL_VISIT)),
        content = getString(getColumnIndexOrThrow(COL_CONTENT)),
        author = getString(getColumnIndexOrThrow(COL_AUTHOR)),
        link = getString(getColumnIndexOrThrow(COL_LINK)),
        likes = getInt(getColumnIndexOrThrow(COL_LIKES)),
        likedByMe = getInt(getColumnIndexOrThrow(COL_LIKED_BY_ME)) == 1,
        participants = getInt(getColumnIndexOrThrow(COL_PARTICIPANTS)),
        participantsByMe = getInt(getColumnIndexOrThrow(COL_PARTICIPANTS_BY_ME)) == 1,
    )

    private class EventsDbHelper(context: Context) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE $TABLE_EVENTS (
                    $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COL_PUBLISHED_AT TEXT NOT NULL,
                    $COL_PUBLISHED TEXT NOT NULL DEFAULT '',
                    $COL_STATUS TEXT NOT NULL DEFAULT '',
                    $COL_VISIT TEXT NOT NULL DEFAULT '',
                    $COL_CONTENT TEXT NOT NULL DEFAULT '',
                    $COL_AUTHOR TEXT NOT NULL DEFAULT '',
                    $COL_LINK TEXT NOT NULL DEFAULT '',
                    $COL_LIKES INTEGER NOT NULL DEFAULT 0,
                    $COL_LIKED_BY_ME INTEGER NOT NULL DEFAULT 0,
                    $COL_PARTICIPANTS INTEGER NOT NULL DEFAULT 0,
                    $COL_PARTICIPANTS_BY_ME INTEGER NOT NULL DEFAULT 0
                )
                """.trimIndent()
            )
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_EVENTS")
            onCreate(db)
        }
    }

    private companion object {
        const val DB_NAME = "events.db"
        const val DB_VERSION = 1
        const val TABLE_EVENTS = "events"
        const val COL_ID = "id"
        const val COL_PUBLISHED_AT = "published_at"
        const val COL_PUBLISHED = "published"
        const val COL_STATUS = "status"
        const val COL_VISIT = "visit"
        const val COL_CONTENT = "content"
        const val COL_AUTHOR = "author"
        const val COL_LINK = "link"
        const val COL_LIKES = "likes"
        const val COL_LIKED_BY_ME = "liked_by_me"
        const val COL_PARTICIPANTS = "participants"
        const val COL_PARTICIPANTS_BY_ME = "participants_by_me"
    }
}