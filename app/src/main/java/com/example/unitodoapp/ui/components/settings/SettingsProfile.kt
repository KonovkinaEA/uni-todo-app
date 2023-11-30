package com.example.unitodoapp.ui.components.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unitodoapp.ui.screens.settings.SettingsState
import com.example.unitodoapp.ui.screens.settings.actions.SettingsUiAction
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.Red
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun ProfileCard(
    uiState: SettingsState,
    onUiAction: (SettingsUiAction) -> Unit
) {

    val uiAction = if (uiState.isLogged) SettingsUiAction.LogOutUser
    else SettingsUiAction.NavigateToLoginScreen

    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = ExtendedTheme.colors.backPrimary,
        ),
        leadingContent = {
            Icon(
                Icons.Rounded.Person,
                contentDescription = "",
            )
        },
        overlineContent = {
            Text(
                text = "Personal data:",
                fontSize = 18.sp,
                color = ExtendedTheme.colors.labelPrimary
            )
        },
        headlineContent = {
            Column {
                Text(
                    text = if (uiState.isLogged) "email: ${uiState.email}" else "you haven't authorized",
                    color = if (uiState.isLogged) ExtendedTheme.colors.labelPrimary else Red,
                    modifier = Modifier.padding(12.dp)
                )
            }
        },
        supportingContent = {
            TextButton(
                onClick = {
                    onUiAction(uiAction)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ExtendedTheme.colors.supportOverlay,
                    contentColor = ExtendedTheme.colors.labelPrimary
                )
            ) {
                Text(
                    text = if (uiState.isLogged) "Log out"
                    else "Log in",
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Icon(
                    if (uiState.isLogged) Icons.Filled.ExitToApp
                    else Icons.Filled.Home,
                    contentDescription = "",
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewProfileCard(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        Column {
            ProfileCard(
                uiState = SettingsState(
                    isLogged = true,
                    email = "thatUser_email@mail.com"
                ),
                {}
            )
            ProfileCard(
                uiState = SettingsState(
                    isLogged = false
                ),
                {}
            )
        }
    }
}