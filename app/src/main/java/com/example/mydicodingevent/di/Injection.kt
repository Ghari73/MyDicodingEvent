package com.example.mydicodingevent.di

import android.content.Context
import com.example.mydicodingevent.data.EventRepository
import com.example.mydicodingevent.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        return EventRepository.getInstance(apiService)
    }
}