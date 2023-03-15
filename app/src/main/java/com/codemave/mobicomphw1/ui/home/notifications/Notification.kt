package com.codemave.mobicomphw1.ui.home.notifications

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codemave.mobicomphw1.R
import com.codemave.mobicomphw1.data.entity.Category
import com.codemave.mobicomphw1.data.entity.Notification
import com.codemave.mobicomphw1.ui.maps.geofence.Geofence
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun Notification(
    modifier: Modifier = Modifier,
    context: Context,
    navController: NavController,
    selectedCategory: Category
) {
    val viewModel: NotificationViewModel = viewModel()
    val viewState by viewModel.state.collectAsState()

    Column(modifier = modifier) {
        NotificationList(
            list = viewState.notifications,
            context,
            navController,
            selectedCategory
        )
    }
}

@Composable
private fun NotificationList(
    list: List<Notification>,
    context: Context,
    navController: NavController,
    selectedCategory: Category
) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(list) { item ->
            NotificationListItem(
                notification = item,
                // onClick = {/* TODO */},
                modifier = Modifier.fillParentMaxWidth(),
                context,
                navController,
                selectedCategory
            )
        }
    }
}

@Composable
private fun NotificationListItem(
    notification: Notification,
    // onClick: () -> Unit,
    modifier: Modifier = Modifier,
    context: Context,
    navController: NavController,
    selectedCategory: Category,
    viewModel: NotificationViewModel = viewModel()
) {
    if ((notification.notificationSeen || notification.reminderTime <= Date().time)
        && selectedCategory == Category(1, "Reminders")
        || /*!notification.notificationSeen
        &&*/ selectedCategory == Category(2, "Show all")) {
//notification.notificationSeen && selectedCategory == Category(1,"Reminders")
        ConstraintLayout(modifier = Modifier.fillMaxWidth()/*.clickable { onClick() }*/) {

            val coroutineScope = rememberCoroutineScope()

            val (divider, notificationTitle, notificationTime, notificationDate, editIcon, deleteIcon/*, checkIcon*/) = createRefs()
            Divider(
                Modifier.constrainAs(divider) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                    width = Dimension.fillToConstraints
                }
            )
            Text(
                text = notification.notificationTitle,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.constrainAs(notificationTitle) {
                    linkTo(
                        start = parent.start,
                        end = editIcon.start,
                        startMargin = 24.dp,
                        endMargin = 120.dp,
                        bias = 0f
                    )
                    top.linkTo(parent.top, margin = 10.dp)
                    width = Dimension.preferredWrapContent
                }
            )
            Text(
                text = notification.notificationTime,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.constrainAs(notificationTime) {
                    linkTo(
                        start = parent.start,
                        end = notificationDate.start,
                        startMargin = 8.dp,
                        endMargin = 8.dp,
                        bias = 0f
                    )
                    top.linkTo(notificationTitle.bottom, margin = 5.dp)
                    width = Dimension.preferredWrapContent
                }
            )
            Text(
                text = notification.notificationDate,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.constrainAs(notificationDate) {
                    linkTo(
                        start = notificationTime.end,
                        end = editIcon.start,
                        startMargin = 8.dp,
                        endMargin = 80.dp,
                        bias = 0f
                    )
                    top.linkTo(notificationTitle.bottom, margin = 5.dp)
                    width = Dimension.preferredWrapContent
                }
            )
            // check icon
//        IconButton(
//            onClick = {},
//            modifier = Modifier
//                .size(50.dp)
//                .padding(6.dp)
//                .constrainAs(checkIcon) {
//                    top.linkTo(parent.top, 10.dp)
//                    bottom.linkTo(parent.bottom, 10.dp)
//                    end.linkTo(editIcon.start)
//                }
//        ) {
//            Icon(
//                imageVector = Icons.Filled.Check,
//                contentDescription = stringResource(R.string.check)
//            )
//        }
            // edit icon
            IconButton(
                onClick = {
                    println("Edit clicked. ID: ${notification.notificationId}")
                    navController.navigate(
                        "edit/{id}"
                            .replace(
                                oldValue = "{id}",
                                newValue = notification.notificationId.toString()
                            )
                    )
                    //Toast.makeText(context, "Notification deleted", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .size(50.dp)
                    .padding(6.dp)
                    .constrainAs(editIcon) {
                        top.linkTo(parent.top, 10.dp)
                        bottom.linkTo(parent.bottom, 10.dp)
                        end.linkTo(deleteIcon.start)
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(R.string.edit)
                )
            }
            // delete icon
            IconButton(
                onClick = {
                    val remId = notification.notificationId
                    //this should delete the Geofence associated with the reminder:
                    Geofence.removeGeofenceWithNotificationId(
                        context,
                        notification.notificationId
                    )
                    coroutineScope.launch {
//                        Geofence.removeGeofenceWithNotificationId(
//                            context,
//                            notification.notificationId
//                        )
                        viewModel.deleteNotification(notification)
                    }
                    println("Reminder deleted. ID: $remId")
                    Toast.makeText(context, "Notification deleted", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .size(50.dp)
                    .padding(6.dp)
                    .constrainAs(deleteIcon) {
                        top.linkTo(parent.top, 10.dp)
                        bottom.linkTo(parent.bottom, 10.dp)
                        end.linkTo(parent.end)
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
            }


        }
    }
}