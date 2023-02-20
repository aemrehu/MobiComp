package com.codemave.mobicomphw1.ui.home.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemave.mobicomphw1.Graph
import com.codemave.mobicomphw1.data.entity.Notification
import com.codemave.mobicomphw1.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationRepository: NotificationRepository = Graph.notificationRepository
): ViewModel() {
    private val _state = MutableStateFlow(NotificationViewModelState())

    val state: StateFlow<NotificationViewModelState>
        get() = _state

    suspend fun saveNotification(notification: Notification): Long {
        return notificationRepository.addNotification(notification)
    }

    suspend fun deleteNotification(notification: Notification): Int {
        return notificationRepository.deleteNotification(notification)
    }

    suspend fun updateNotification(notification: Notification) {
        return notificationRepository.updateNotification(notification)
    }

    fun getNotificationWithId(id: Long): Notification {
        return notificationRepository.getNotificationWithId(id)
    }

    init {
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

//class NotificationViewModel :  ViewModel() {
//    private val _state = MutableStateFlow(NotificationViewModelState())
//    val state: StateFlow<NotificationViewModelState>
//        get() = _state
//
//    init {
//        val list = mutableListOf<Notification>()
////        for (x in 1..20) {
////            list.add(
////                Notification(
////                    notificationId = x.toLong(),
////                    notificationTitle = "$x. notification"
////                )
////            )
////        }
//
//        viewModelScope.launch {
//            _state.value = NotificationViewModelState(
//                notifications = list
//            )
//        }
//    }
//}
//
//data class NotificationViewModelState(
//    val notifications: List<Notification> = emptyList()
//)