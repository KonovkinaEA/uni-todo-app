package com.example.unitodoapp.ui.components.edit

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.unitodoapp.R
import com.example.unitodoapp.ui.components.pulsateClick
import com.example.unitodoapp.ui.screens.edit.actions.EditUiAction
import com.example.unitodoapp.ui.theme.Blue
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTopAppBar(
    text: String,
    uiAction: (EditUiAction) -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = { uiAction(EditUiAction.NavigateUp) },
                modifier = Modifier.pulsateClick()
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.edit_close_button)
                )
            }
        },
        actions = {
            val saveButtonColor by animateColorAsState(
                targetValue = if (text.isBlank()) ExtendedTheme.colors.labelDisable else Blue,
                label = "save_button_color_animation"
            )

            TextButton(
                onClick = { uiAction(EditUiAction.SaveTask) },
                enabled = text.isNotBlank(),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = saveButtonColor,
                    disabledContentColor = saveButtonColor
                ),
                modifier = Modifier.pulsateClick()
            ) {
                Text(
                    text = stringResource(R.string.edit_save_button),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ExtendedTheme.colors.backPrimary,
            navigationIconContentColor = ExtendedTheme.colors.labelPrimary
        )
    )
}

@Preview
@Composable
fun PreviewEditTopAppBar(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        EditTopAppBar(
            text = "",
            uiAction = {}
        )
    }
}
