package com.example.mydicodingevent.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mydicodingevent.data.local.entity.FavoriteEvent

@Database(entities = [FavoriteEvent::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun newsDao(): EventDao

    companion object {
        @Volatile
        private var instance: EventDatabase? = null
        fun getInstance(context: Context): EventDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java, "News.db"
                ).build()
            }
    }
}