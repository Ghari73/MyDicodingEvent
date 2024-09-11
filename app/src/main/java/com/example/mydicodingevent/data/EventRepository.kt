package com.example.mydicodingevent.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mydicodingevent.data.local.entity.FavoriteEvent
import com.example.mydicodingevent.data.local.room.EventDao
import com.example.mydicodingevent.data.response.EventResponse
import com.example.mydicodingevent.data.response.ListEventsItem
import com.example.mydicodingevent.data.retrofit.ApiService
import com.example.mydicodingevent.utils.EventHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao
    ) {
    private val _listUpcomingEvents = MutableLiveData<EventHandler<List<ListEventsItem>>>()
    val listUpcomingEvents: LiveData<EventHandler<List<ListEventsItem>>> = _listUpcomingEvents

    private val _listFinishedEvents = MutableLiveData<EventHandler<List<ListEventsItem>>>()
    val listFinishedEvents: LiveData<EventHandler<List<ListEventsItem>>> = _listFinishedEvents

    private val _isLoading = MutableLiveData<EventHandler<Boolean>>()
    val isLoading: LiveData<EventHandler<Boolean>> = _isLoading

    //    val filteredUpcomingEvents: LiveData<List<ListEventsItem>> = searchUpcomingEvent()
    fun setUpcomingEvent(){
        _isLoading.value = EventHandler(true)
        val client = apiService.getUpcomingEvents()
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

    fun setFinishedEvent(){
        _isLoading.value = EventHandler(true)
        val client = apiService.getFinishedEvents()
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

    fun getFavEvent(): LiveData<List<FavoriteEvent>>{
        return eventDao.getFavEvent()
    }

    fun getFavEventbyId(id: String) = eventDao.getFavoriteEventById(id)

    suspend fun insertFavEvent(event: FavoriteEvent){
        eventDao.insertFavEvent(event)
    }

    suspend fun deleteFavEvent(id: String){
        eventDao.deleteFavEvent(id)
    }


    companion object{
        private const val TAG = "EventViewModel"

        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService,eventDao)
            }.also { instance = it }
    }
}