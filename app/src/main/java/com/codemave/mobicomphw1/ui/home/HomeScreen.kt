package com.codemave.mobicomphw1.ui.home

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codemave.mobicomphw1.ui.home.notifications.Notification
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun HomeScreen(
    navController: NavController,
    context: Context
) {
    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navController.navigate("add")},
                modifier = Modifier.padding(all = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
        ) {
            val appBarColor = MaterialTheme.colors.surface.copy(alpha = 0.87f)
            
            HomeAppBar(
                navController,
                backgroundColor = appBarColor
            )

            Notification(
                modifier = Modifier.fillMaxSize(),
                context,
                navController
            )
        }
    }
}

@Composable
private fun HomeAppBar(
    navController: NavController,
    backgroundColor: Color
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(com.codemave.mobicomphw1.R.string.app_name),
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        backgroundColor = backgroundColor,
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(com.codemave.mobicomphw1.R.string.search)
                )
            }
            IconButton(onClick = {navController.navigate("profile")}) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(com.codemave.mobicomphw1.R.string.profile)
                )
            }
        }
    )
}