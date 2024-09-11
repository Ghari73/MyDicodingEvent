package com.example.mydicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mydicodingevent.data.local.entity.FavoriteEvent

@Dao
interface EventDao {
    @Query("SELECT * FROM favorite_event ORDER BY name")
    fun getFavEvent(): LiveData<List<FavoriteEvent>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavEvent(event: FavoriteEvent)

    @Query("DELETE FROM favorite_event WHERE id = :id")
    suspend fun deleteFavEvent(id: String)

    @Query("SELECT * FROM favorite_event WHERE id = :id")
    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent>
}