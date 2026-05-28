package com.eltex.firstapp.feature.event.data

import com.eltex.firstapp.feature.data.RetrofitFactory
import retrofit2.Call
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EventApi {
    @GET("events")
    fun getEvents(): Call<List<EventDto>>

    @POST("events")
    fun saveEvent(@Body eventRequest: EventRequest): Call<EventDto>

    @PUT("events/{id}")
    fun updateEvent(@Path("id") id: Long, @Body eventRequest: EventRequest): Call<EventDto>

    @POST("events/{id}/likes")
    fun like(@Path("id") id: Long): Call<EventDto>

    @DELETE("events/{id}/likes")
    fun unlike(@Path("id") id: Long): Call<EventDto>

    @POST("events/{id}/participate")
    fun participate(@Path("id") id: Long): Call<EventDto>

    @DELETE("events{id}/participate")
    fun unparticipate(@Path("id") id: Long): Call<EventDto>

    @DELETE("events/{id}")
    fun delete(@Path("id") id: Long): Call<Unit>

    companion object {
        val value: EventApi by lazy {
            RetrofitFactory.retrofit.create()
        }
    }
}