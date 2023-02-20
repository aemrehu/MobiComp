package com.codemave.mobicomphw1.ui.home.profile

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codemave.mobicomphw1.R
import com.codemave.mobicomphw1.SharedPreferences
import com.codemave.mobicomphw1.ui.login.Logged

@Composable
fun ProfileScreen(
    navController: NavController,
    context: Context
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

        Icon(
            painter = rememberVectorPainter(Icons.Filled.Person),
            contentDescription = "login_image",
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp)
        )

        Text(
            text = SharedPreferences(context).username,
            modifier = Modifier
                .padding(start = 4.dp)
                .heightIn(max = 24.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth(0.9F)
                .height(50.dp),
            onClick = { navController.navigate("login") },
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        ) {
            Text(text = "Log out")
        }

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