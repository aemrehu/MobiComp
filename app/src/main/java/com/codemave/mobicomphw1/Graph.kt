package com.codemave.mobicomphw1

import android.content.Context
import androidx.room.Room
import com.codemave.mobicomphw1.data.repository.NotificationRepository
import com.codemave.mobicomphw1.data.room.MobiCompDatabase

/**
 * A simple singelton dependecy graph
 */
object Graph {
    lateinit var database: MobiCompDatabase
        private set

    val notificationRepository by lazy {
        NotificationRepository(
            notificationDao = database.notificationDao()
        )
    }

    lateinit var appContext: Context

    fun provide(context: Context) {
        appContext = context
        database = Room.databaseBuilder(context, MobiCompDatabase::class.java, "data.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}