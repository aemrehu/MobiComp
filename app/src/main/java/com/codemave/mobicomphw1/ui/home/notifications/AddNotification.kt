package com.codemave.mobicomphw1.ui.home.notifications

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codemave.mobicomphw1.R
import com.codemave.mobicomphw1.SharedPreferences
import com.codemave.mobicomphw1.data.entity.Notification
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AddNotification(
    navController: NavController,
    context: Context,
    viewModel: NotificationViewModel = viewModel()

) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val coroutineScope = rememberCoroutineScope()

        val notificationTitle = rememberSaveable { mutableStateOf("") }
        val notificationTime = rememberSaveable { mutableStateOf("") }

//        val locationX = remember { mutableStateOf("") }
//        val locationY = remember { mutableStateOf("") }

        val appBarColor = MaterialTheme.colors.surface.copy(alpha = 0.87f)

        TopBar(
            backgroundColor = appBarColor
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.9f),
                value = notificationTitle.value,
                onValueChange = {notificationTitle.value = it},
                label = {Text(text = "Title")},
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            )
            Spacer(modifier = Modifier.height(20.dp))
//        Row() {
//            OutlinedTextField(
//                //modifier = Modifier.fillMaxWidth(0.9f),
//                value = locationX.value,
//                onValueChange = {text -> locationX.value = text },
//                label = {Text(text = "X-coordinate")},
//                shape = RoundedCornerShape(corner = CornerSize(50.dp))
//            )
//            OutlinedTextField(
//                //modifier = Modifier.fillMaxWidth(0.9f),
//                value = locationY.value,
//                onValueChange = {text -> locationY.value = text },
//                label = {Text(text = "Y-coordinate")},
//                shape = RoundedCornerShape(corner = CornerSize(50.dp))
//            )
//
//        }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.9f),
                value = notificationTime.value,
                onValueChange = {notificationTime.value = it},
                label = {Text(text = "Time")},
                shape = RoundedCornerShape(corner = CornerSize(50.dp)),
                keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveNotification(
                            com.codemave.mobicomphw1.data.entity.Notification(
                                notificationTitle = notificationTitle.value,
                                notificationTime = notificationTime.value,
                                creationTime = Date().time,
                                creatorId = SharedPreferences(context).username,
                                notificationSeen = false,
                                locationX = null,
                                locationY = null
                            )
                        )
                    }
                    navController.popBackStack()
                },
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            ) {
                Text(text = "Save notification")
            }
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