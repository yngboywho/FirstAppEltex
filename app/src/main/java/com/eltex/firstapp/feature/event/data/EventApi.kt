package com.eltex.firstapp.feature.event.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

object EventApi {

    suspend fun HttpClient.getAllEvents(): List<EventDto> = get("events").body()


    suspend fun HttpClient.saveEvent(eventDto: EventDto): EventDto = post("events") {
        setBody(eventDto)
    }.body()


    suspend fun HttpClient.updateEvent(id: Long, eventDto: EventDto): EventDto = post("events/$id") {
        setBody(eventDto)
    }.body()


    suspend fun HttpClient.like(id: Long): EventDto = post("events/$id/likes").body()


    suspend fun HttpClient.unlike(id: Long): EventDto = post("events/$id/likes").body()


    suspend fun HttpClient.participate(id: Long): EventDto = post("events/$id/participate").body()


    suspend fun HttpClient.unparticipate(id: Long): EventDto = post("events/$id/participate").body()


    suspend fun HttpClient.deleteEvent(id: Long): EventDto = delete("events/$id").body()
}