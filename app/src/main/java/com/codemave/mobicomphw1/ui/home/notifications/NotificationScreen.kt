package com.codemave.mobicomphw1.ui.home.notifications

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codemave.mobicomphw1.R
import com.codemave.mobicomphw1.data.entity.Notification

/*
* Discarded for now
*/

@Composable
fun NotificationScreen(
    navController: NavController,
    notification: Notification
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val appBarColor = MaterialTheme.colors.surface.copy(alpha = 0.87f)

        TopBar(
            backgroundColor = appBarColor
        )

        Spacer(modifier = Modifier.height(300.dp))
    }
}

@Composable
private fun TopBar(
    backgroundColor: Color
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        backgroundColor = backgroundColor
    )
}