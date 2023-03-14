package com.codemave.mobicomphw1.ui.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codemave.mobicomphw1.Graph
import com.codemave.mobicomphw1.data.entity.Notification
import com.codemave.mobicomphw1.ui.home.notifications.NotificationViewModel
import com.codemave.mobicomphw1.ui.home.notifications.NotificationViewModelState
import com.codemave.mobicomphw1.util.rememberMapViewWithLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch

@Composable
fun AllMarkersMap(
    navController: NavController
) {
    lateinit var fusedLocationClient: FusedLocationProviderClient

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(Graph.appContext)

    val viewModel: NotificationViewModel = viewModel()
    val viewState by viewModel.state.collectAsState()

    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            Button(
                enabled = true,
                modifier = Modifier
                    .padding(end = 110.dp)
                    .fillMaxWidth(0.5f)
                    .height(50.dp),
                shape = RoundedCornerShape(corner = CornerSize(50.dp)),
                onClick = { navController.popBackStack() }
            ) {
                Text(text = "Back")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AndroidView({ mapView }) { mapView ->
                coroutineScope.launch {
                    val map = mapView.awaitMap()
                    map.uiSettings.isZoomControlsEnabled = true
                    val location = LatLng(65.059243, 25.467069)

                    map.isMyLocationEnabled = true

                    fusedLocationClient.lastLocation.addOnSuccessListener {
                        if (it != null) {
                            val latLng = LatLng(it.latitude, it.longitude)
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(latLng, 14.5f)
                            )
                        } else {
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(location, 14.5f)
                            )
                        }
                    }
//                    map.moveCamera(
//                        CameraUpdateFactory.newLatLngZoom(location, 14.5f)
//                    )

                    addReminderMarkers(map, viewState)
                }
            }
        }
    }
}

private fun addReminderMarkers(
    map: GoogleMap,
    viewState: NotificationViewModelState
) {
    // Get all reminders from database
    val notifications: List<Notification> = viewState.notifications

    // Add marker for each reminder
    for (reminder in notifications)
    {
        if (reminder.latitude != null && reminder.longitude != null)
        {
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(reminder.latitude, reminder.longitude))
                    .title(reminder.notificationTitle)
                    .snippet(
                        "Time: "
                        + reminder.notificationTime
                        + ", Date: "
                        + reminder.notificationDate
                    )
            )
        }
    }
}