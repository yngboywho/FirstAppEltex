package com.eltex.firstapp.feature.event.data

import com.eltex.firstapp.BuildConfig
import com.eltex.firstapp.feature.event.domain.Callback
import com.eltex.firstapp.feature.event.domain.Event
import com.eltex.firstapp.feature.event.domain.EventsRepository
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class EventsRepositoryImpl : EventsRepository {

    private val json = Json { ignoreUnknownKeys = true }
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        })
        .build()

    private data class EventRequest(
        val content: String,
        val type: String,
        val datetime: String,
        val link: String? = null,
    )

    override fun getEvents(callback: Callback<List<Event>>) {
        client.newCall(
            Request.Builder()
                .url("https://eltex-android.ru/api/events")
                .header("Api-Key", BuildConfig.API_KEY)
                .build()
        )
            .enqueue(
                object: okhttp3.Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            callback.onSuccess(json.decodeFromString<List<EventDto>>(response.body.string()).map { it.toDomain() })
                        } else {
                            callback.onError(RuntimeException(response.body.string()))
                        }
                    }
                }
            )
    }

    override fun save(
        content: String,
        author: String,
        status: String,
        visit: String,
        link: String,
        callback: Callback<Event>,
    ) {
        val body = json.encodeToString(
            EventRequest(
                content = content,
                type = status,
                datetime = visit,
                link = link.ifBlank { null },
            )
        ).toRequestBody("application/json".toMediaType())

        client.newCall(
            Request.Builder()
                .url("https://eltex-android.ru/api/events")
                .header("Api-Key", BuildConfig.API_KEY)
                .header("Authorization", BuildConfig.Authorization)
                .post(body)
                .build()
        ).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) = callback.onError(e)

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback.onSuccess(json.decodeFromString<EventDto>(response.body.string()).toDomain())
                } else {
                    callback.onError(RuntimeException(response.body.string()))
                }
            }
        })
    }

    override fun update(
        id: Long,
        content: String,
        callback: Callback<Event>,
    ) {
        getEvents(object : Callback<List<Event>> {
            override fun onSuccess(value: List<Event>) {
                val existing = value.first { it.id == id }
                val body = json.encodeToString(
                    EventRequest(
                        content = content,
                        type = existing.status,
                        datetime = existing.visit,
                        link = existing.link.ifBlank { null },
                    )
                ).toRequestBody("application/json".toMediaType())

                client.newCall(
                    Request.Builder()
                        .url("https://eltex-android.ru/api/events/$id")
                        .header("Api-Key", BuildConfig.API_KEY)
                        .header("Authorization", BuildConfig.Authorization)
                        .put(body)
                        .build()
                ).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(RuntimeException(e))
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            callback.onSuccess(json.decodeFromString<EventDto>(response.body.string()).toDomain())
                        } else {
                            callback.onError(RuntimeException(response.body.string()))
                        }
                    }
                })
            }
            override fun onError(error: Exception) = callback.onError(error)
        })
    }

    override fun likeById(
        id: Long,
        callback: Callback<Event>,
    ) {
        client.newCall(
            Request.Builder()
                .url("https://eltex-android.ru/api/events/$id/likes")
                .post(RequestBody.EMPTY)
                .header("Api-Key", BuildConfig.API_KEY)
                .header("Authorization", BuildConfig.Authorization)
                .build()
        ).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(RuntimeException(e))
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback.onSuccess(json.decodeFromString<EventDto>(response.body.string()).toDomain())
                } else {
                    callback.onError(RuntimeException(response.body.string()))
                }
            }

        })
    }

    override fun participateById(
        id: Long,
        callback: Callback<Event>,
    ) {
        client.newCall(
            Request.Builder()
                .url("https://eltex-android.ru/api/events/$id/participants")
                .post(RequestBody.EMPTY)
                .header("Api-Key", BuildConfig.API_KEY)
                .header("Authorization", BuildConfig.Authorization)
                .build()
        ).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(RuntimeException(e))
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback.onSuccess(json.decodeFromString<EventDto>(response.body.string()).toDomain())
                } else {
                    callback.onError(RuntimeException(response.body.string()))
                }
            }
        })
    }

    override fun deleteById(
        id: Long,
        callback: Callback<Unit>,
    ) {
        client.newCall(
            Request.Builder()
                .url("https://eltex-android.ru/api/events/$id")
                .delete()
                .header("Api-Key", BuildConfig.API_KEY)
                .header("Authorization", BuildConfig.Authorization)
                .build()
        ).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(RuntimeException(e))
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError(RuntimeException(response.body.string()))
                }
            }

        })
    }

}