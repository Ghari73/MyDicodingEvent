package com.example.mydicodingevent.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydicodingevent.data.EventRepository
import com.example.mydicodingevent.data.response.EventResponse
import com.example.mydicodingevent.data.response.ListEventsItem
import com.example.mydicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.mydicodingevent.utils.EventHandler
import kotlinx.coroutines.launch


class EventViewModel(private val eventRepository: EventRepository): ViewModel() {
    fun getUpcomingEvent() = eventRepository.listUpcomingEvents
    fun setUpcomingEvent() {
        viewModelScope.launch {
            eventRepository.setUpcomingEvent()
        }
    }

    fun getFinishedEvent() = eventRepository.listFinishedEvents
    fun setFinishedEvent(){
        viewModelScope.launch {
            eventRepository.setFinishedEvent()
        }
    }

    fun getLoading() = eventRepository.isLoading
    fun searchUpcomingEvent(query: String) = eventRepository.searchUpcomingEvent(query)
    fun searchFinishedEvent(query: String) = eventRepository.searchFinishedEvent(query)
}