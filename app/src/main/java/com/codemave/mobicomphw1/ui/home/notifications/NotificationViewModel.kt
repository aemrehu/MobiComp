package com.codemave.mobicomphw1.ui.home.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.from
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.codemave.mobicomphw1.Graph
import com.codemave.mobicomphw1.R
import com.codemave.mobicomphw1.data.entity.Notification
import com.codemave.mobicomphw1.data.repository.NotificationRepository
import com.codemave.mobicomphw1.ui.maps.geofence.Geofence
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class NotificationViewModel(
    private val notificationRepository: NotificationRepository = Graph.notificationRepository
): ViewModel() {
    private val _state = MutableStateFlow(NotificationViewModelState())

    val state: StateFlow<NotificationViewModelState>
        get() = _state

    suspend fun saveNotification(notification: Notification): Long {
        val result = notificationRepository.addNotification(notification)
        println("Saved. ID: $result")

        if (notification.timeEnabled) {
            setOneTimeNotification(notification, result)
        } else if (!notification.locationEnabled) {
            updateSeen(notification, result, true)
        }
        if (notification.locationEnabled) {
            Geofence.addGeofence(notification, result)
//            updateSeen(notification, result, false)
        }
        return result
    }

    suspend fun deleteNotification(notification: Notification): Int {
        return notificationRepository.deleteNotification(notification)
    }

    suspend fun updateNotification(notification: Notification) {
        val result = notificationRepository.updateNotification(notification)
        println("Updated. ID: ${notification.notificationId}")
        if (notification.timeEnabled) {
            setOneTimeNotification(notification, notification.notificationId)
        } else if (!notification.locationEnabled) {
            updateSeen(notification, notification.notificationId, true)
        }
        return result
    }

    suspend fun updateWithoutPushNotification(notification: Notification) {
        return notificationRepository.updateNotification(notification)
    }

    fun getNotificationWithId(id: Long): Notification {
        return notificationRepository.getNotificationWithId(id)
    }

    init {
        createNotificationChannel(context = Graph.appContext)

        viewModelScope.launch() {
            notificationRepository.getNotifications().collect { notifications ->
                _state.value = NotificationViewModelState(notifications)
            }
        }
    }
}

data class NotificationViewModelState(
    val notifications: List<Notification> = emptyList()
)

private fun setOneTimeNotification(reminder: Notification, id: Long) {


    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val delay: Long = (reminder.reminderTime!! - reminder.creationTime) / 1000

    println("reminderTime: ${reminder.reminderTime}")
    println("creationTime: ${reminder.creationTime}")

    val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delay-20, TimeUnit.SECONDS)
        .setConstraints(constraints)
        .build()

    workManager.enqueue(notificationWorker)

    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            try {
                // check if reminder has been deleted
                val reminder2: Notification = Graph.notificationRepository.getNotificationWithId(id)

                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    createReminderNotification(reminder, id)
//                    updateSeen(reminder)
                }
            } catch (e: Exception) {
                println("Reminder deleted, no notification")
            }

        }
}

fun createReminderNotification(reminder: Notification, id: Long) {

    if (!reminder.notificationSeen) {
        val notificationId = id.toInt()
        val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(reminder.notificationTitle)
            .setContentText(
                reminder.notificationTime
                + " "
                + reminder.notificationDate
                + " "
                + "Lat: %.2f, Lng: %.2f".format(reminder.latitude, reminder.longitude)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(from(Graph.appContext)) {
            notify(notificationId, builder.build())
        }
        updateSeen(reminder, id, true)
    }
}

private fun updateSeen(
    reminder: Notification,
    id: Long,
    seen: Boolean,
    notificationRepository: NotificationRepository = Graph.notificationRepository
) {
    notificationRepository.update2(
        Notification(
            notificationId = id,
            notificationTitle = reminder.notificationTitle,
            notificationTime = reminder.notificationTime,
            notificationDate = reminder.notificationDate,
            reminderTime = reminder.reminderTime,
            creationTime = reminder.creationTime,
            creatorId = reminder.creatorId,
            notificationSeen = seen,
            latitude = reminder.latitude,
            longitude = reminder.longitude,
            timeEnabled = reminder.timeEnabled,
            locationEnabled = reminder.locationEnabled
        )
    )
}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "NotificationChannelName"
        val descriptionText = "NotificationChannelDescription"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}

private fun createSuccessNotification() {
    val notificationId = 1
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("Test reminder")
        .setContentText("")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(from(Graph.appContext)) {
        notify(notificationId, builder.build())
    }
}
