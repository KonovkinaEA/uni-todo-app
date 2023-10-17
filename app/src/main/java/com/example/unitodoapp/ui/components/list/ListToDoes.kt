package com.example.unitodoapp.ui.components.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.unitodoapp.data.model.TodoItem
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme

@Composable
fun ListToDoes(
    toDoes: List<TodoItem>,
    onCheckboxClick: (TodoItem) -> Unit,
    onItemClick: (TodoItem) -> Unit,
) {
    Surface(
        color = ExtendedTheme.colors.backSecondary,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .fillMaxSize()
    ) {
        LazyColumn {
            items(toDoes) { todo ->
                ListToDoItem(
                    todo = todo,
                    onCheckboxClick = { onCheckboxClick(todo) },
                    onItemClick = { onItemClick(todo) }
                )
            }
        }
    }
}


@Preview(widthDp = 360, heightDp = 720)
@Composable
fun PreviewToDoItemList(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        Surface(color = ExtendedTheme.colors.backPrimary) {
            ListToDoes(
                toDoes = listOf(),
                onCheckboxClick = {},
                onItemClick = {}
            )
        }
    }
}