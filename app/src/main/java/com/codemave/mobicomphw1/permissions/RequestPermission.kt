package com.codemave.mobicomphw1.permissions

import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import com.google.accompanist.permissions.*

/* Discarded for now */

@ExperimentalPermissionsApi
@Composable
fun RequestPermission(
    permission: String,
    content: @Composable () -> Unit,
    message: String = "To use this app, you need to give location permissions"
) {
    val permissionState = rememberPermissionState(permission)

    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                message = message,
                shouldShowRationale = shouldShowRationale
            ) { permissionState.launchPermissionRequest() }
        },
        content = {
            content()
        }
    )
}



@ExperimentalPermissionsApi
@Composable
fun HandleRequest(
    permissionState: PermissionState,
    deniedContent: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    when (permissionState.status) {
        is PermissionStatus.Granted -> {
            content()
        }
        is PermissionStatus.Denied -> {
            deniedContent(permissionState.status.shouldShowRationale)
        }
    }
}

//@Composable
//fun Content(
//    showButton: Boolean = true,
//    onClick: () -> Unit
//) {
//    if (showButton) {
//        val enableLocation = remember { mutableStateOf(true) }
//        if (enableLocation.value) {
//            CustomDialogLocation(
//                title = "Turn on location service",
//                desc = "Please grant access to location",
//                enableLocation,
//                onClick
//            )
//        }
//    }
//}

@ExperimentalPermissionsApi
@Composable
fun PermissionDeniedContent(
    message: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {
    if (shouldShowRationale) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Permission request"
                )
            },
            text = {Text(message)},
            confirmButton = {
                Button(onClick = onRequestPermission) {
                    Text("Give permission")
                }
            }
        )
    } else {
        //Content(onClick = onRequestPermission)
    }
}