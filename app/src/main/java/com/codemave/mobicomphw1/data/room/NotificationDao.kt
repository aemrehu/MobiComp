package com.codemave.mobicomphw1.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.codemave.mobicomphw1.data.entity.Notification
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NotificationDao {

    @Query(value = "SELECT * FROM notifications WHERE title = :title")
    abstract fun getNotificationWithTitle(title: String): Notification?

    @Query("SELECT * FROM notifications WHERE id = :id")
    abstract fun getNotificationWithId(id: Long): Notification

    @Query("SELECT * FROM notifications")
    abstract fun getNotifications(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: Notification): Long

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    abstract suspend fun insertAll(entities: Collection<Notification>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: Notification)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update2(entity: Notification)

    @Delete
    abstract suspend fun delete(entity: Notification): Int
}