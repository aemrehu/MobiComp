package com.codemave.mobicomphw1.ui.home.notifications

import android.app.admin.DevicePolicyManager.OnClearApplicationUserDataListener
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.sourceInformationMarkerEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.os.persistableBundleOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codemave.mobicomphw1.R
import com.codemave.mobicomphw1.data.entity.Notification

@Composable
fun Notification(
    modifier: Modifier = Modifier
) {
    val viewModel: NotificationViewModel = viewModel()
    val viewState by viewModel.state.collectAsState()

    Column(modifier = modifier) {
        NotificationList(
            list = viewState.notifications
        )
    }
}

@Composable
private fun NotificationList(
    list: List<Notification>
) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(list) { item ->
            NotificationListItem(
                notification = item,
                onClick = {/* TODO */},
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}

@Composable
private fun NotificationListItem(
    notification: Notification,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        val (divider, notificationTitle, icon) = createRefs()
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
                    end = icon.start,
                    startMargin = 24.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
                top.linkTo(parent.top, margin = 10.dp)
                width = Dimension.preferredWrapContent
            }
        )
        IconButton(
            onClick = {},
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = stringResource(R.string.check)
            )
        }
    }
}