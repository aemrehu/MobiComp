package com.codemave.mobicomphw1.ui.home.notifications

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codemave.mobicomphw1.R
import com.codemave.mobicomphw1.SharedPreferences
import com.codemave.mobicomphw1.data.entity.Notification
import com.codemave.mobicomphw1.ui.home.speechtotext.SpeechToTextViewModel
import com.codemave.mobicomphw1.ui.home.speechtotext.startSpeechToText
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun EditNotification(
    id: Long,
    navController: NavController,
    context: Context,
    viewModel: NotificationViewModel = viewModel()

) {
    val timeContext = LocalContext.current

    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val time = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }

    val timePickerDialog = TimePickerDialog(
        timeContext,
        {_, hour : Int, minute: Int ->
            time.value = "%02d:%02d".format(hour, minute)},
        hour, minute, true
    )
    val datePickerDialog = DatePickerDialog(
        timeContext,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            date.value = "%02d.%02d.%04d".format(day,month+1,year)},
        year, month, day
    )

    val reminderCalendar = Calendar.getInstance()

    var latitude: Double?
    var longitude: Double?
    val latlng = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<LatLng>("location_data")
        ?.value

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val coroutineScope = rememberCoroutineScope()

        val notification: Notification = viewModel.getNotificationWithId(id)

        val notificationTitle = rememberSaveable { mutableStateOf(notification.notificationTitle) }
        val notificationTime = rememberSaveable { mutableStateOf(notification.notificationTime) }
        val notificationDate = rememberSaveable { mutableStateOf(notification.notificationDate) }

        // set correct time to be displayed
        time.value = notificationTime.value
        date.value = notificationDate.value

//        timeValues.forEach(::println)
//        dateValues.forEach(::println)

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

            Spacer(modifier = Modifier.height(10.dp))

            // Call speech recognition button
            val vm: SpeechToTextViewModel = viewModel()
            var buttonColor = remember { mutableStateOf(Color.DarkGray) }
            Button(
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor.value) ,
                onClick = {
                    buttonColor.value = Color.Green
                    startSpeechToText(
                        vm,
                        context,
                        finished = {
                            buttonColor.value = Color.DarkGray

                        },
                        textChange = {
                            if (vm.textFromSpeech != null) {
                                notificationTitle.value = vm.textFromSpeech!!
                            }
                        }
                    )

                },
                shape = RoundedCornerShape(corner = CornerSize(40.dp))
            ) {
                Text(text = "Speech to text")
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                onClick = { navController.navigate("location") },
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            ) {
                if (latlng == null) {
                    Text(text = "Location")
                } else {
                    Text(text = "Lat: %.2f, Lng: %.2f".format(latlng.latitude, latlng.longitude))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                onClick = {
                    timePickerDialog.show()
                },
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            ) {
                Text(text = "Time: " + time.value)
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                onClick = {
                    datePickerDialog.show()
                },
                shape = RoundedCornerShape(corner = CornerSize(50.dp))
            ) {
                Text(text = "Date: " + date.value)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                onClick = {
                    if (latlng == null) {
                        latitude = notification.latitude
                        longitude = notification.longitude
                    } else {
                        latitude = latlng.latitude
                        longitude = latlng.longitude
                    }
                    val timeValues = time.value.split(":")
                    val dateValues = date.value.split(".")
                    val newyear = Integer.parseInt(dateValues[2])
                    val newmonth = Integer.parseInt(dateValues[1])
                    val newday = Integer.parseInt(dateValues[0])
                    val newhour = Integer.parseInt(timeValues[0])
                    val newminute = Integer.parseInt(timeValues[1])
                    reminderCalendar.set(newyear,newmonth-1,newday,newhour,newminute)
                    coroutineScope.launch {
                        viewModel.updateNotification(
                            com.codemave.mobicomphw1.data.entity.Notification(
                                notificationId = notification.notificationId,
                                notificationTitle = notificationTitle.value,
                                notificationTime = time.value,
                                notificationDate = date.value,
                                reminderTime = reminderCalendar.timeInMillis,
                                creationTime = notification.creationTime,
                                creatorId = notification.creatorId,
                                notificationSeen = notification.notificationSeen,
                                latitude = latitude,
                                longitude = longitude,
                                timeEnabled = notification.timeEnabled,
                                locationEnabled = notification.locationEnabled
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
                text = stringResource(R.string.editnotification),
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        backgroundColor = backgroundColor
    )
}