package com.codemave.mobicomphw1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.codemave.mobicomphw1.ui.theme.MobiCompHW1Theme

class MainActivity : ComponentActivity() {

//    lateinit var fusedLocationClient: FusedLocationProviderClient
//    lateinit var geofencingClient: GeofencingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val creds = SharedPreferences(this)
        creds.username = "admin"
        creds.password = "admin"

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        geofencingClient = LocationServices.getGeofencingClient(this)

        setContent {
            MobiCompHW1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    MobiCompApp(context = applicationContext)
                }

            }
        }
    }
}
