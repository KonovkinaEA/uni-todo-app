package com.example.unitodoapp.ui.screens.edit.actions

import com.example.unitodoapp.data.model.Importance

sealed class EditUiAction {
    data class UpdateText(val text: String) : EditUiAction()
    data class UpdateDeadlineSet(val isDeadlineSet: Boolean): EditUiAction()
    data class UpdateDeadline(val deadline: Long) : EditUiAction()
    data class UpdateImportance(val importance: Importance) : EditUiAction()

    object SaveTask: EditUiAction()
    object DeleteTask : EditUiAction()
    object NavigateUp: EditUiAction()
}
