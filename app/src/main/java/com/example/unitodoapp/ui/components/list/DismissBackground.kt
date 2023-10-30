package com.example.unitodoapp.ui.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.unitodoapp.ui.theme.GrayLight
import com.example.unitodoapp.ui.theme.Green
import com.example.unitodoapp.ui.theme.Red
import com.example.unitodoapp.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(
    dismissState: DismissState,
    isTaskDone: Boolean
) {
    val direction = dismissState.dismissDirection ?: return
    val color = when (direction) {
        DismissDirection.EndToStart -> Red
        DismissDirection.StartToEnd -> if (isTaskDone) GrayLight else Green
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (direction == DismissDirection.StartToEnd) Icon(
            if (isTaskDone) Icons.Default.Close else Icons.Default.Done,
            contentDescription = "change task status",
            tint = White
        )
        Spacer(modifier = Modifier)
        if (direction == DismissDirection.EndToStart) Icon(
            Icons.Default.Delete,
            contentDescription = "delete task",
            tint = White
        )
    }
}