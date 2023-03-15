package com.codemave.mobicomphw1.ui.maps.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.codemave.mobicomphw1.Graph
import com.codemave.mobicomphw1.data.entity.Notification
import com.codemave.mobicomphw1.ui.home.notifications.NotificationViewModel
import com.codemave.mobicomphw1.ui.home.notifications.createReminderNotification
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.Geofence

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            val geofencingTransition = geofencingEvent?.geofenceTransition

            if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                println("Geofence: ENTERED")
                val id = intent.getLongExtra("reminderID",0)
                val reminder: Notification = Graph.notificationRepository.getNotificationWithId(id)
                if (reminder != null) {
                    createReminderNotification(reminder, id)
                } else {
                    println("Reminder was deleted, geofence should've been deleted aswell...")
                }
                val triggeringGeofences = geofencingEvent.triggeringGeofences
                if (triggeringGeofences != null) {
                    com.codemave.mobicomphw1.ui.maps.geofence.Geofence.removeGeofences(
                        context,
                        triggeringGeofences
                    )
                }
            }
        }
    }

}