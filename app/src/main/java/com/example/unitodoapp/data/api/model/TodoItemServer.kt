package com.example.unitodoapp.data.api.model

import com.example.unitodoapp.data.model.TodoItem
import com.example.unitodoapp.utils.convertToImportance
import com.google.gson.annotations.SerializedName

data class TodoItemServer(
    @SerializedName("id") val id: String?,
    @SerializedName("text") val text: String? = null,
    @SerializedName("importance") val importance: String? = null,
    @SerializedName("deadline") val deadline: Long? = null,
    @SerializedName("done") val done: Boolean? = null,
    @SerializedName("color") val color: String? = null,
    @SerializedName("created_at") val createdAt: Long? = null,
    @SerializedName("changed_at") val changedAt: Long? = null,
    @SerializedName("last_updated_by") val lastUpdatedBy: String? = null
) {

    fun toTodoItem(): TodoItem {
        return TodoItem(
            id = id!!,
            text = text!!,
            importance =  importance!!.convertToImportance(),
            deadline = deadline,
            isDone = done!!,
            creationDate = createdAt!!,
            modificationDate = changedAt!!
        )
    }
}