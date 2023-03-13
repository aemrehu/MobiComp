package com.codemave.mobicomphw1.ui.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.codemave.mobicomphw1.util.rememberMapViewWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch

@Composable
fun AllMarkersMap(
    navController: NavController
) {
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView({mapView}) { mapView ->
            coroutineScope.launch {
                val map = mapView.awaitMap()
                map.uiSettings.isZoomControlsEnabled = true
                val location = LatLng(65.059243, 25.467069)

                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(location, 14.5f)
                )
            }
        }
    }
}