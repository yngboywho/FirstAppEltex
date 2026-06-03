package com.eltex.firstapp.feature.event.data

import com.eltex.firstapp.feature.data.RetrofitFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventApi {
    @GET("events")
    suspend fun getEvents(): List<EventDto>

    @POST("events")
    suspend fun saveEvent(@Body eventDto: EventDto): EventDto

    @POST("events/{id}")
    suspend fun updateEvent(@Path("id") id: Long, @Body eventDto: EventDto): EventDto

    @POST("events/{id}/likes")
    suspend fun like(@Path("id") id: Long): EventDto

    @DELETE("events/{id}/likes")
    suspend fun unlike(@Path("id") id: Long): EventDto

    @POST("events/{id}/participate")
    suspend fun participate(@Path("id") id: Long): EventDto

    @DELETE("events{id}/participate")
    suspend fun unparticipate(@Path("id") id: Long): EventDto

    @DELETE("events/{id}")
    suspend fun delete(@Path("id") id: Long)

    companion object {
        val value: EventApi by lazy {
            RetrofitFactory.retrofit.create()
        }
    }
}