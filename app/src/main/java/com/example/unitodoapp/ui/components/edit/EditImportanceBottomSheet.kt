package com.example.unitodoapp.ui.components.edit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.unitodoapp.R
import com.example.unitodoapp.data.model.Importance
import com.example.unitodoapp.ui.screens.edit.actions.EditUiAction
import com.example.unitodoapp.ui.theme.ExtendedTheme
import com.example.unitodoapp.ui.theme.Red
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportanceModalBottomSheet(
    oldImportance: Importance,
    showModalBottomSheet: Boolean,
    uiAction: (EditUiAction) -> Unit,
    closeBottomSheet: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val skipPartially by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartially)
    var newImportance by remember { mutableStateOf(oldImportance) }

    if (showModalBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { closeBottomSheet() },
            sheetState = bottomSheetState,
            containerColor = ExtendedTheme.colors.backPrimary,
            modifier = Modifier
                .fillMaxHeight(0.25f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButtonForBottomSheet(
                        closeBottomSheet = { closeBottomSheet() },
                        updateImportance = {},
                        scope = scope,
                        bottomSheetState = bottomSheetState,
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.importance_close_button),
                        save = false
                    )
                    IconButtonForBottomSheet(
                        closeBottomSheet = { closeBottomSheet() },
                        updateImportance = { uiAction(EditUiAction.UpdateImportance(newImportance)) },
                        scope = scope,
                        bottomSheetState = bottomSheetState,
                        imageVector = Icons.Default.Done,
                        contentDescription = stringResource(id = R.string.importance_save_button),
                        save = true
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (importance in Importance.values()) {
                        ImportanceItem(
                            changeImportance = { newImportance = importance },
                            importance = importance,
                            selected = newImportance == importance
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImportanceItem(
    changeImportance: () -> Unit,
    importance: Importance,
    selected: Boolean
) {
    val color = when {
        selected && importance == Importance.IMPORTANT -> Red
        selected -> ExtendedTheme.colors.labelPrimary
        else -> ExtendedTheme.colors.labelTertiary
    }

    val scale = remember { Animatable(initialValue = 1f) }
    val clickEnabled = remember { mutableStateOf(true) }

    LaunchedEffect(key1 = selected) {
        if (selected) {
            clickEnabled.value = false

            val job = launch {
                scale.animateTo(
                    targetValue = 0.9f,
                    animationSpec = tween(
                        durationMillis = 50
                    )
                )
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }

            job.join()
            clickEnabled.value = true
        }
    }

    Box(
        modifier = Modifier
            .scale(scale = scale.value)
            .clip(RoundedCornerShape(10.dp))
            .clickable { changeImportance() }
    ) {
        Text(
            text = stringResource(id = importance.toStringResource()),
            modifier = Modifier.padding(all = 10.dp),
            color = color,
            style = MaterialTheme.typography.bodyLarge
        )
    }
    if (importance != Importance.IMPORTANT) Spacer(modifier = Modifier.size(10.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconButtonForBottomSheet(
    closeBottomSheet: () -> Unit,
    updateImportance: () -> Unit,
    scope: CoroutineScope,
    bottomSheetState: SheetState,
    imageVector: ImageVector,
    contentDescription: String,
    save: Boolean
) {
    IconButton(
        onClick = {
            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                if (!bottomSheetState.isVisible) {
                    if (save) updateImportance()
                    closeBottomSheet()
                }
            }
        }
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = ExtendedTheme.colors.labelPrimary
        )
    }
}
