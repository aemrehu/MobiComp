package com.codemave.mobicomphw1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.codemave.mobicomphw1.ui.theme.MobiCompHW1Theme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

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
                    MobiCompApp(context = applicationContext, getGoogleLoginAuth = getGoogleLoginAuth())
                }

            }
        }
    }

    private fun getGoogleLoginAuth(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.gcp_id))
            .requestId()
            .requestProfile()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }
}
