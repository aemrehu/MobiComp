package com.codemave.mobicomphw1.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codemave.mobicomphw1.data.entity.Notification

@Database(
    entities = [Notification::class],
    version = 8,
    exportSchema = false
)
abstract class MobiCompDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}