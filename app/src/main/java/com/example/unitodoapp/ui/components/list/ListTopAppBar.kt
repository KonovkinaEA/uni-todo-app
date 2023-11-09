package com.example.unitodoapp.ui.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unitodoapp.R
import com.example.unitodoapp.ui.theme.Blue
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.TodoAppTheme
import com.example.unitodoapp.utils.toDoList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTopAppBar(
    doneTasks: Int = 0,
    isFiltered: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior,
    onSettingsClick: () -> Unit = {},
    onVisibilityClick: (Boolean) -> Unit = {}
) {
    val collapsed = scrollBehavior.state.collapsedFraction

    LargeTopAppBar(
        title = {
            val startPadding =
                if (collapsed < 0.7f) 60.dp
                else 0.dp

            val topPadding =
                if (collapsed < 0.75f) 18.dp
                else 0.dp

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier.padding(start = startPadding, top = topPadding)
                ) {
                    Text(
                        text = stringResource(R.string.list_title),
                        color = ExtendedTheme.colors.labelPrimary,
                        fontSize = 32.sp
                    )
                    if (collapsed < 0.75f)
                        Text(
                            text = stringResource(R.string.list_title_tasks_count, doneTasks),
                            color = ExtendedTheme.colors.labelTertiary,
                            fontSize = 14.sp
                        )
                }
                if (collapsed < 0.62f)
                    VisibilityIcon(
                        modifier = Modifier
                            .size(
                                height = 25.dp,
                                width = 48.dp
                            ),
                        isFiltered
                    ) {
                        onVisibilityClick(isFiltered)
                    }
            }

        },
        actions = {
            if (collapsed > 0.62f)
                VisibilityIcon(isFiltered = isFiltered) {
                    onVisibilityClick(isFiltered)
                }
        },
        navigationIcon = {
            IconButton(
                onClick = onSettingsClick
            ) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Settings"
                )

            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = ExtendedTheme.colors.backPrimary,

            )
    )

}

@Composable
fun VisibilityIcon(
    modifier: Modifier = Modifier,
    isFiltered: Boolean = false,
    onClick: () -> Unit
) {
    IconButton(
        onClick = {
            onClick()
        },
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = Blue
        ),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(
                id = if (isFiltered)
                    R.drawable.visibility_off
                else
                    R.drawable.visibility
            ),
            contentDescription = "Filter done tasks"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewListTopAppBar() {
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    TodoAppTheme(darkTheme = false) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                ListTopAppBar(
                    scrollBehavior = scrollBehavior
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(ExtendedTheme.colors.backSecondary)
                    .fillMaxSize()
                    .padding(it)
            ) {
                items(toDoList) { item ->
                    ListToDoItemCard(
                        todo = item,
                        onCheckboxClick = {},
                        onItemClick = {}
                    )
                }
            }
        }
    }
}
