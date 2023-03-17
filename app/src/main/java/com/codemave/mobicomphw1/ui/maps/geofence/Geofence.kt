package com.codemave.mobicomphw1.ui.maps.geofence

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.codemave.mobicomphw1.Graph
import com.codemave.mobicomphw1.data.entity.Notification
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

object Geofence {
    private val geofencingClient = LocationServices.getGeofencingClient(Graph.appContext)

    fun addGeofence(
        reminder: Notification,
        id: Long,
//        success: () -> Unit,
//        failure: (error: String) -> Unit
    ) {
        // 1
        val geofence = buildGeofence(reminder, id)
        if (geofence != null
            && ContextCompat.checkSelfPermission(
                Graph.appContext,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 2
            geofencingClient
                .addGeofences(buildGeofencingRequest(geofence), createPendingIntent(id))
                // 3
                .addOnSuccessListener {
                    println("Geofence created!!!")
//                    success()
                }
                // 4
                .addOnFailureListener {
//                    failure("Error")
                    println("Geofence ERROR")
                }
        }
    }

    fun removeGeofences(
        context: Context,
        triggeringGeofenceList: MutableList<Geofence>
    ) {
        val geofenceIdList = mutableListOf<String>()
        for (entry in triggeringGeofenceList) {
            geofenceIdList.add(entry.requestId)
        }
        LocationServices.getGeofencingClient(context).removeGeofences(geofenceIdList)
    }

    fun removeGeofenceWithNotificationId(
        context: Context,
        id: Long
    ) {
        val geofenceIdList = mutableListOf<String>()
        geofenceIdList.add(id.toString())
        LocationServices.getGeofencingClient(context).removeGeofences(geofenceIdList)
    }

    private fun buildGeofence(reminder: Notification, id: Long): Geofence? {
        val latitude: Double? = reminder.latitude
        val longitude: Double? = reminder.longitude
        val radius: Float? = 100.0f

        if (latitude != null && longitude != null && radius != null) {
            return Geofence.Builder()
                // 1
                .setRequestId(reminder.notificationId.toString())
                // 2
                .setCircularRegion(
                    latitude,
                    longitude,
                    radius.toFloat()
                )
                // 3
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL)
                // 4
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(5000)
                .build()
        }

        return null
    }

    private fun buildGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(0)
            .addGeofences(listOf(geofence))
            .build()
    }

    private fun createPendingIntent(
        id: Long
    ): PendingIntent {
        val intent = Intent(Graph.appContext, GeofenceBroadcastReceiver::class.java)
            .putExtra("reminderID", id)
        val geofencePendingIntent = PendingIntent.getBroadcast(
                Graph.appContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        return geofencePendingIntent
    }
//    private val geofencePendingIntent: PendingIntent by lazy {
//        val intent = Intent(Graph.appContext, GeofenceBroadcastReceiver::class.java)
//        PendingIntent.getBroadcast(
//            Graph.appContext,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT)
//    }
}