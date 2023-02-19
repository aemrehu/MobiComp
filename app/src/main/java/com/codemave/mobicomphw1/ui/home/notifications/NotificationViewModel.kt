package com.codemave.mobicomphw1.ui.home.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemave.mobicomphw1.data.entity.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel :  ViewModel() {
    private val _state = MutableStateFlow(NotificationViewModelState())
    val state: StateFlow<NotificationViewModelState>
        get() = _state

    init {
        val list = mutableListOf<Notification>()
        for (x in 1..20) {
            list.add(
                Notification(
                    notificationId = x.toLong(),
                    notificationTitle = "$x. notification"
                )
            )
        }

        viewModelScope.launch {
            _state.value = NotificationViewModelState(
                notifications = list
            )
        }
    }
}

data class NotificationViewModelState(
    val notifications: List<Notification> = emptyList()
)