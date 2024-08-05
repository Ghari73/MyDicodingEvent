package com.example.mydicodingevent.data.retrofit

import com.example.mydicodingevent.data.response.DetailResponse
import com.example.mydicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("events?active=1")
    fun getUpcomingEvents(): Call<EventResponse>

    @GET("events?active=0")
    fun getFinishedEvents(): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailOfEvent(
        @Path("id") id: String
    ): Call<DetailResponse>
}