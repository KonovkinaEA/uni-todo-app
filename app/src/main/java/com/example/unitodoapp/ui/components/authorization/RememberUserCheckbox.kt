package com.example.unitodoapp.ui.components.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.unitodoapp.R
import com.example.unitodoapp.ui.theme.Blue
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun RememberUserCheckbox(
    checkedValue: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedValue,
            onCheckedChange = {
                onCheckedChange(it)
            },
            colors = CheckboxDefaults.colors(
                uncheckedColor = ExtendedTheme.colors.supportSeparator,
                checkedColor = Blue
            )
        )

        Text(
            text = stringResource(R.string.don_t_log_out),
            color = if (checkedValue) Blue else ExtendedTheme.colors.labelSecondary,
        )

    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 120)
@Composable
fun PreviewRememberUserCheckbox(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        Column(modifier = Modifier.background(ExtendedTheme.colors.backPrimary)){

            RememberUserCheckbox(
                checkedValue = false,
                onCheckedChange = {}
            )

            RememberUserCheckbox(
                checkedValue = true,
                onCheckedChange = {}
            )

        }
    }
}