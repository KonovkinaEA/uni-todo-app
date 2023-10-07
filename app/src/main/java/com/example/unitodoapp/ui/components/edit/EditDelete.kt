package com.example.unitodoapp.ui.components.edit

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.unitodoapp.R
import com.example.unitodoapp.ui.components.pulsateClick
import com.example.unitodoapp.ui.screens.edit.actions.EditUiAction
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.Red
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun EditDelete(
    enabled: Boolean,
    uiAction: (EditUiAction) -> Unit
) {
    val deleteButtonColor by animateColorAsState(
        targetValue = if (enabled) Red else ExtendedTheme.colors.labelDisable,
        label = "delete_button_color_animation"
    )

    TextButton(
        onClick = { uiAction(EditUiAction.DeleteTask) },
        modifier = Modifier.padding(horizontal = 5.dp).pulsateClick(),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = deleteButtonColor,
            disabledContentColor = deleteButtonColor
        )
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(id = R.string.delete_title),
            modifier = Modifier.size(25.dp)
        )
        Text(
            text = stringResource(id = R.string.delete_title),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Preview
@Composable
fun PreviewAddTodoItemDelete(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        EditDelete(
            enabled = false,
            uiAction = {}
        )
    }
}
