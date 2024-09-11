package com.example.mydicodingevent.di

import android.content.Context
import com.example.mydicodingevent.data.EventRepository
import com.example.mydicodingevent.data.local.room.EventDatabase
import com.example.mydicodingevent.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val eventDao = database.newsDao()
        return EventRepository.getInstance(apiService, eventDao)
    }
}