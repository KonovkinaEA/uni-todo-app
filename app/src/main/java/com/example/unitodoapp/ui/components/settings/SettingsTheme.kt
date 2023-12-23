package com.example.unitodoapp.ui.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.unitodoapp.R
import com.example.unitodoapp.ui.screens.settings.actions.SettingsUiAction
import com.example.unitodoapp.ui.screens.settings.model.ThemeMode
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun ThemePicker(
    themeMode: ThemeMode,
    uiAction: (SettingsUiAction) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(all = 15.dp)
            .clickable { expanded = true }
    ) {
        Text(
            text = stringResource(id = R.string.settings_theme_title),
            color = ExtendedTheme.colors.labelPrimary
        )
        Text(
            text = stringResource(id = themeMode.toStringResource()),
            modifier = Modifier.padding(top = 5.dp),
            color = ExtendedTheme.colors.labelTertiary
        )
        ThemeDropdownMenu(
            uiAction = uiAction,
            closeMenu = { expanded = false },
            expanded = expanded
        )
    }
}

@Composable
fun ThemeDropdownMenu(
    uiAction: (SettingsUiAction) -> Unit,
    closeMenu: () -> Unit,
    expanded: Boolean
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = closeMenu,
        modifier = Modifier.background(ExtendedTheme.colors.backElevated),
    ) {
        for (themeMode in ThemeMode.values()) {
            DropdownMenuItem(
                text = { Text(stringResource(id = themeMode.toStringResource())) },
                onClick = {
                    uiAction(SettingsUiAction.UpdateThemeMode(themeMode))
                    closeMenu()
                },
                colors = MenuDefaults.itemColors(
                    textColor = ExtendedTheme.colors.labelPrimary
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewThemePicker(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        ThemePicker(
            themeMode = if (darkTheme) ThemeMode.DARK else ThemeMode.LIGHT,
            uiAction = {}
        )
    }
}
