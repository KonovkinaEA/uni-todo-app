package com.example.unitodoapp.ui.components.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.unitodoapp.data.model.Importance
import com.example.unitodoapp.data.model.TodoItem
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTodoItem(
    todo: TodoItem,
    onCheckboxClick: () -> Unit,
    onItemClick: () -> Unit,
    onDeleteSwipe: (TodoItem) -> Unit,
    onUpdateSwipe: (TodoItem) -> Unit,
    modifier: Modifier = Modifier
) {

    var show by remember { mutableStateOf(true) }
    val dismissState = DismissState(
        initialValue = if (show)
            DismissValue.Default
        else
            DismissValue.DismissedToStart,
        confirmValueChange = {
            when (it) {
                DismissValue.Default -> false
                DismissValue.DismissedToEnd -> {
                    onUpdateSwipe(todo)
                    false
                }

                DismissValue.DismissedToStart -> {
                    show = false
                    true
                }
            }
        }, positionalThreshold = { 150.dp.toPx() }
    )
    SwipeToDismiss(
        state = dismissState,
        modifier = modifier,
        background = {
            DismissBackground(dismissState = dismissState)
        },
        dismissContent = {
            ListToDoItemCard(
                todo = todo,
                onCheckboxClick = onCheckboxClick,
                onItemClick = onItemClick
            )
        }
    )

    LaunchedEffect(show) {
        if (!show) {
            delay(100)
            onDeleteSwipe(todo)
            show = true
        }
    }
}

@Preview
@Composable
fun PreviewBaseToDoItem(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        Column {
            ListTodoItem(
                todo = TodoItem(
                    isDone = false,
                    text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, " +
                            "но точно нужно чтобы показать как обрезается " +
                            "эта часть текста не видна",
                    importance = Importance.BASIC
                ),
                onCheckboxClick = {},
                onItemClick = {},
                onDeleteSwipe = {},
                onUpdateSwipe = {}
            )

            ListTodoItem(
                todo = TodoItem(
                    isDone = false,
                    text = "Купить что-то",
                    importance = Importance.IMPORTANT
                ),
                onCheckboxClick = {},
                onItemClick = {},
                onDeleteSwipe = {},
                onUpdateSwipe = {}
            )
        }

    }
}