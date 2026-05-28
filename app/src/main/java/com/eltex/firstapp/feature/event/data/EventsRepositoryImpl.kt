package com.eltex.firstapp.feature.event.data

import com.eltex.firstapp.feature.event.domain.Callback
import com.eltex.firstapp.feature.event.domain.Event
import com.eltex.firstapp.feature.event.domain.EventsRepository
import retrofit2.Call
import retrofit2.Response

class EventsRepositoryImpl : EventsRepository {

    override fun getEvents(callback: Callback<List<Event>>) {
        EventApi.value.getEvents()
            .enqueue(
                object : retrofit2.Callback<List<EventDto>> {
                    override fun onResponse(
                        call: Call<List<EventDto>>,
                        response: Response<List<EventDto>?>,
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.map(EventDto::toDomain)?.let {
                                callback.onSuccess(it)
                            } ?: run {
                                callback.onError(
                                    RuntimeException(
                                        response.errorBody()?.string()
                                    )
                                )
                            }
                        } else {
                            callback.onError(RuntimeException(response.errorBody()?.string()))
                        }
                    }

                    override fun onFailure(
                        call: Call<List<EventDto>>,
                        t: Throwable,
                    ) {
                        callback.onError(RuntimeException(t))
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
        val request = EventRequest(
            content = content,
            type = status,
            datetime = visit,
            link = link.ifBlank { null },
        )
        EventApi.value.saveEvent(request)
            .enqueue(object : retrofit2.Callback<EventDto> {
                override fun onResponse(
                    call: Call<EventDto>,
                    response: Response<EventDto?>,
                ) {
                    if (response.isSuccessful) {
                        response.body()?.toDomain()?.let {
                            callback.onSuccess(it)
                        } ?: callback.onError(RuntimeException(response.errorBody()?.string()))
                    } else {
                        callback.onError(RuntimeException(response.errorBody()?.string()))
                    }
                }

                override fun onFailure(
                    call: Call<EventDto>,
                    t: Throwable,
                ) {
                    callback.onError(RuntimeException(t))
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
                val request = EventRequest(
                    content = content,
                    type = existing.status,
                    datetime = existing.visit,
                    link = existing.link.ifBlank { null },
                )

                EventApi.value.updateEvent(id, request)
                    .enqueue(object : retrofit2.Callback<EventDto> {
                        override fun onResponse(
                            call: Call<EventDto?>,
                            response: Response<EventDto?>,
                        ) {
                            if (response.isSuccessful) {
                                response.body()?.toDomain()?.let {
                                    callback.onSuccess(it)
                                } ?: callback.onError(
                                    RuntimeException(
                                        response.errorBody()?.string()
                                    )
                                )
                            } else {
                                callback.onError(RuntimeException(response.errorBody()?.string()))
                            }
                        }

                        override fun onFailure(
                            call: Call<EventDto?>,
                            t: Throwable,
                        ) {
                            callback.onError(RuntimeException(t))
                        }

                    })
            }

            override fun onError(error: Exception) = callback.onError(error)
        })
    }

    override fun likeById(
        id: Long,
        likedByMe: Boolean,
        callback: Callback<Event>
    ) {
        val call = if (likedByMe) EventApi.value.unlike(id) else EventApi.value.like(id)
            call.enqueue(object : retrofit2.Callback<EventDto> {
                override fun onResponse(
                    call: Call<EventDto?>,
                    response: Response<EventDto?>,
                ) {
                    if (response.isSuccessful) {
                        response.body()?.toDomain()?.let {
                            callback.onSuccess(it)
                        } ?: callback.onError(RuntimeException(response.errorBody()?.string()))
                    } else {
                        callback.onError(RuntimeException(response.errorBody()?.string()))
                    }
                }

                override fun onFailure(
                    call: Call<EventDto?>,
                    t: Throwable,
                ) {
                    callback.onError(RuntimeException(t))
                }

            })
    }

    override fun participateById(
        id: Long,
        participatedByMe: Boolean,
        callback: Callback<Event>
    ) {
        val call = if (participatedByMe) EventApi.value.unparticipate(id) else EventApi.value.participate(id)
            call.enqueue(object : retrofit2.Callback<EventDto> {
                override fun onResponse(
                    call: Call<EventDto?>,
                    response: Response<EventDto?>,
                ) {
                    if (response.isSuccessful) {
                        response.body()?.toDomain()?.let {
                            callback.onSuccess(it)
                        } ?: callback.onError(RuntimeException(response.errorBody()?.string()))
                    } else {
                        callback.onError(RuntimeException(response.errorBody()?.string()))
                    }
                }

                override fun onFailure(
                    call: Call<EventDto?>,
                    t: Throwable,
                ) {
                    callback.onError(RuntimeException(t))
                }

            })
    }

    override fun deleteById(
        id: Long,
        callback: Callback<Unit>,
    ) {
        EventApi.value.delete(id)
            .enqueue(object : retrofit2.Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit?>,
                    response: Response<Unit?>,
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(Unit)
                    } else {
                        callback.onError(RuntimeException(response.errorBody()?.string()))
                    }
                }

                override fun onFailure(call: Call<Unit?>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }

}