package com.example.unitodoapp.ui.components.edit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun EditDivider(
    padding: PaddingValues
) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        color = ExtendedTheme.colors.supportSeparator
    )
}

@Preview
@Composable
fun PreviewEditDivider(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        EditDivider(padding = PaddingValues(all = 15.dp))
    }
}
