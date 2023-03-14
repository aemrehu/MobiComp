package com.codemave.mobicomphw1.ui.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.codemave.mobicomphw1.Graph
import com.codemave.mobicomphw1.util.rememberMapViewWithLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun NotificationLocationMap(
    navController: NavController
) {
    lateinit var fusedLocationClient: FusedLocationProviderClient

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(Graph.appContext)

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
                Text(text = "Save & return")
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

                    setMapLongClick(map, navController)
                }
            }


        }
    }
}

private fun setMapLongClick(
    map: GoogleMap,
    navController: NavController
) {
    map.setOnMapLongClickListener { latlng ->
        map.clear()
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.2f, Lng: %2$.2f",
            latlng.latitude,
            latlng.longitude
        )
        map.addMarker(
            MarkerOptions().position(latlng).title("Reminder location").snippet(snippet)
        ).apply {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("location_data", latlng)
            //navController.popBackStack()
        }
    }
}