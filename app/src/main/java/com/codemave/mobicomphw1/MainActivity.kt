package com.codemave.mobicomphw1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.codemave.mobicomphw1.ui.home.HomeScreen
import com.codemave.mobicomphw1.ui.login.LoginScreen
import com.codemave.mobicomphw1.ui.theme.MobiCompHW1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val creds = SharedPreferences(this)
        creds.username = "admin"
        creds.password = "admin"

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