package com.eltex.firstapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eltex.firstapp.feature.event.data.EventDao
import com.eltex.firstapp.feature.event.data.EventEntity

@Database(entities = [EventEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract val eventDao: EventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getInstance(context: Context): AppDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context): AppDb = Room.databaseBuilder(
            context,
            AppDb::class.java,
            "app.db",
        )
            .createFromAsset("mybd.db")
            .fallbackToDestructiveMigration(true)
            .allowMainThreadQueries()
            .build()
    }
}