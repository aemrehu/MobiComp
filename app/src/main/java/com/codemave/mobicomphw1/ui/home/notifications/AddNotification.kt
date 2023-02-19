package com.codemave.mobicomphw1.ui.home.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codemave.mobicomphw1.R

@Composable
fun AddNotification(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val appBarColor = MaterialTheme.colors.surface.copy(alpha = 0.87f)

        HomeAppBar(
            backgroundColor = appBarColor
        )

    }
}

@Composable
private fun HomeAppBar(
    backgroundColor: Color
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.addnotification),
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        backgroundColor = backgroundColor
    )
}