package com.example.unitodoapp.ui.components.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DismissDirection.*
import androidx.compose.material3.DismissValue.*
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
import com.example.unitodoapp.utils.toDoList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListToDoes(
    toDoes: List<TodoItem>,
    onItemClick: (TodoItem) -> Unit,
    onDelete: (TodoItem) -> Unit,
    onUpdate: (TodoItem) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
            )
            .background(ExtendedTheme.colors.backSecondary)
    ) {
        items(toDoes, key = { it.id }) { todo ->
            ListTodoItem(
                todo = todo,
                onCheckboxClick = { onUpdate(todo) },
                onItemClick = { onItemClick(todo) },
                onDeleteSwipe = onDelete,
                onUpdateSwipe = onUpdate,
                Modifier.animateItemPlacement()
            )
        }
    }


}


@Preview(widthDp = 360, heightDp = 720)
@Composable
fun PreviewToDoItemList(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        Surface(
            color = ExtendedTheme.colors.backPrimary,
        ) {
            ListToDoes(
                toDoes = toDoList,
                onItemClick = {},
                onDelete = {},
                onUpdate = {}
            )
        }
    }
}