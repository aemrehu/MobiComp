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
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AddNotification(
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
            time.value = "%02d:%02d".format(hour, minute)}, //"$hour:$minute"
        hour, minute, true
    )

    val datePickerDialog = DatePickerDialog(
        timeContext,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            date.value = "%02d.%02d.%04d".format(day,month+1,year)},
        year, month, day
    )

    // update time to current time
    timePickerDialog.updateTime(hour, minute)
    time.value = "%02d:%02d".format(hour, minute)
    datePickerDialog.updateDate(year,month,day)
    date.value = "%02d.%02d.%04d".format(day,month+1,year)

    val reminderCalendar = Calendar.getInstance()

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
                    // if (CheckTimeDate()) {
                    if (notificationTitle.value == "") {
                        notificationTitle.value = "Reminder"
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
                        viewModel.saveNotification(
                            com.codemave.mobicomphw1.data.entity.Notification(
                                notificationTitle = notificationTitle.value,
                                notificationTime = time.value, // notificationTime.value,
                                notificationDate = date.value,
                                reminderTime = reminderCalendar.timeInMillis,
                                creationTime = Date().time,
                                creatorId = SharedPreferences(context).username,
                                notificationSeen = false,
                                latitude = latlng?.latitude,
                                longitude = latlng?.longitude
                            )
                        )
                    }
                    navController.popBackStack()
                    // }
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

private fun CheckTimeDate(): Boolean {
    // TODO
    return false
}