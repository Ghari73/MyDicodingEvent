package com.example.mydicodingevent.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydicodingevent.data.response.EventResponse
import com.example.mydicodingevent.data.response.ListEventsItem
import com.example.mydicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.mydicodingevent.utils.EventHandler


class EventViewModel: ViewModel() {
    private val _listUpcomingEvents = MutableLiveData<EventHandler<List<ListEventsItem>>>()
    val listUpcomingEvents: LiveData<EventHandler<List<ListEventsItem>>> = _listUpcomingEvents

    private val _listFinishedEvents = MutableLiveData<EventHandler<List<ListEventsItem>>>()
    val listFinishedEvents: LiveData<EventHandler<List<ListEventsItem>>> = _listFinishedEvents

    private val _isLoading = MutableLiveData<EventHandler<Boolean>>()
    val isLoading: LiveData<EventHandler<Boolean>> = _isLoading

//    val filteredUpcomingEvents: LiveData<List<ListEventsItem>> = searchUpcomingEvent()
    fun findUpcomingEvent(){
        _isLoading.value = EventHandler(true)
        val client = ApiConfig.getApiService().getUpcomingEvents()
        client.enqueue(object: Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = EventHandler(false)
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _listUpcomingEvents.value = EventHandler(responseBody.listEvents)
                    }
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = EventHandler(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findFinishedEvent(){
        _isLoading.value = EventHandler(true)
        val client = ApiConfig.getApiService().getFinishedEvents()
        client.enqueue(object: Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = EventHandler(false)
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _listFinishedEvents.value = EventHandler(responseBody.listEvents)
                    }
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = EventHandler(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun searchUpcomingEvent(query: String): LiveData<List<ListEventsItem>?> {
        val filteredList = MutableLiveData<List<ListEventsItem>?>()
        _listUpcomingEvents.value?.let { handler ->
            val filtered = handler.peekContent().filter { event ->
                event.name.contains(query, ignoreCase = true)
            }
            filteredList.postValue(filtered)
        }
        return filteredList
    }

    fun searchFinishedEvent(query: String): LiveData<List<ListEventsItem>?> {
        val filteredList = MutableLiveData<List<ListEventsItem>?>()

        _listFinishedEvents.value?.let { handler ->
            val filtered = handler.peekContent().filter { event ->
                event.name.contains(query, ignoreCase = true)
            }
            filteredList.postValue(filtered)
        }
        return filteredList
    }


    companion object{
        private const val TAG = "EventViewModel"
    }
}