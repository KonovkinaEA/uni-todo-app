package com.example.unitodoapp.ui.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unitodoapp.data.model.Importance
import com.example.unitodoapp.data.model.TodoItem
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.ThemeModePreview
import com.example.unitodoapp.ui.theme.TodoAppTheme
import com.example.unitodoapp.utils.convertToDateFormat


@Composable
fun ListToDoItemCard(
    todo: TodoItem,
    onCheckboxClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onItemClick() }
            .background(ExtendedTheme.colors.backPrimary),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = { onCheckboxClick() },
            colors = CheckboxDefaults.colors(
                uncheckedColor = if (todo.importance == Importance.IMPORTANT) Color.Red
                else ExtendedTheme.colors.supportSeparator,
                checkedColor = Color.Green
            ),
            modifier = if (todo.importance == Importance.IMPORTANT)
                Modifier
                    .padding(start = 4.dp)
                    .drawBehind {
                        scale(0.4f) {
                            drawRect(
                                color = Color.Red,
                                alpha = 0.15f,
                            )
                        }

                    } else Modifier.padding(start = 4.dp)
        )
        Row(
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(
                    text = todo.text,
                    color = if (todo.isDone) ExtendedTheme.colors.labelTertiary
                    else ExtendedTheme.colors.labelPrimary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        textDecoration = if (todo.isDone) TextDecoration.LineThrough
                        else TextDecoration.None
                    )
                )
                if (todo.deadline != null)
                    Text(
                        text = todo.deadline!!.convertToDateFormat(),
                        color = ExtendedTheme.colors.labelTertiary,
                        fontSize = 14.sp
                    )
            }

            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Todo info",
                tint = ExtendedTheme.colors.supportSeparator,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewBaseToDoItemCard(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {
        ListToDoItemCard(
            todo = TodoItem(
                isDone = false,
                text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, " +
                        "но точно нужно чтобы показать как обрезается " +
                        "эта часть текста не видна",
                importance = Importance.BASIC
            ),
            onCheckboxClick = {},
            onItemClick = {}
        )
    }
}

@Preview
@Composable
fun PreviewHighPriorityToDoItemCard(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {

        ListToDoItemCard(
            todo = TodoItem(
                isDone = false,
                text = "Купить что-то",
                importance = Importance.IMPORTANT
            ),
            onCheckboxClick = {},
            onItemClick = {}
        )


    }
}

@Preview
@Composable
fun PreviewDoneToDoItemCard(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {

        ListToDoItemCard(
            todo = TodoItem(
                isDone = true,
                text = "Купить что-то",
                importance = Importance.BASIC
            ),
            onCheckboxClick = {},
            onItemClick = {}
        )


    }
}

@Preview
@Composable
fun PreviewDeadlineToDoItemCard(
    @PreviewParameter(ThemeModePreview::class) darkTheme: Boolean
) {
    TodoAppTheme(darkTheme = darkTheme) {

        ListToDoItemCard(
            todo = TodoItem(
                isDone = false,
                text = "Купить что-то",
                importance = Importance.BASIC,
                deadline = 1654665100000L
            ),
            onCheckboxClick = {},
            onItemClick = {}
        )

    }
}

