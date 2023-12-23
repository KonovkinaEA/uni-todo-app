package com.example.unitodoapp.ui.components.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.unitodoapp.R
import com.example.unitodoapp.ui.screens.edit.actions.EditUiAction
import com.example.unitodoapp.ui.theme.Blue
import com.example.unitodoapp.ui.theme.BlueTranslucent
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme
import com.example.unitodoapp.utils.convertToDateFormat

@Composable
fun Deadline(
    deadline: Long,
    isDateVisible: Boolean,
    uiAction: (EditUiAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val dateText = remember(deadline) { deadline.convertToDateFormat() }
        var isDialogOpen by remember { mutableStateOf(false) }

        Column {
            Text(
                text = stringResource(id = R.string.deadline_title),
                modifier = Modifier.padding(start = 5.dp),
                color = ExtendedTheme.colors.labelPrimary
            )
            AnimatedVisibility(visible = isDateVisible) {
                Box(modifier = Modifier.padding(5.dp)) {
                    Text(text = dateText, color = Blue)
                }
            }
        }
        Switch(
            checked = isDateVisible,
            onCheckedChange = { checked ->
                if (checked) {
                    isDialogOpen = true
                } else {
                    uiAction(EditUiAction.UpdateDeadlineSet(false))
                }
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Blue,
                checkedTrackColor = BlueTranslucent,
                uncheckedThumbColor = ExtendedTheme.colors.backElevated,
                uncheckedTrackColor = ExtendedTheme.colors.supportOverlay,
                uncheckedBorderColor = ExtendedTheme.colors.supportOverlay,
            )
        )
        DeadlineDatePicker(
            isDialogOpen = isDialogOpen,
            deadline = deadline,
            uiAction = uiAction,
            closeDialog = { isDialogOpen = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeadlineDatePicker(
    isDialogOpen: Boolean,
    deadline: Long,
    uiAction: (EditUiAction) -> Unit,
    closeDialog: () -> Unit
) {
    if (isDialogOpen) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = deadline
        )
        val confirmEnabled by remember(datePickerState.selectedDateMillis) {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }

        DatePickerDialog(
            onDismissRequest = closeDialog,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            uiAction(EditUiAction.UpdateDeadline(it))
                            uiAction(EditUiAction.UpdateDeadlineSet(true))
                        }
                        closeDialog()
                    },
                    enabled = confirmEnabled
                ) {
                    Text(stringResource(R.string.deadline_calendar_ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = closeDialog
                ) {
                    Text(stringResource(R.string.deadline_calendar_cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview
@Composable
fun PreviewDeadline(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        Deadline(
            deadline = 1696693800000L,
            isDateVisible = true,
            uiAction = {}
        )
    }
}
