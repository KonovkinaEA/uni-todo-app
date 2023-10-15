package com.example.unitodoapp.data.db

import androidx.room.ColumnInfo
import com.example.unitodoapp.data.model.TodoItem
import com.example.unitodoapp.utils.stringToImportance


data class TodoItemInfoTuple(
    val id: String,
    @ColumnInfo(name = "importance_name") var importance: String,
    var text: String,
    var deadline: Long?,
    var done: Boolean,
    val createdAt: Long,
    var changedAt: Long
) {

    fun toTodoItem(): TodoItem = TodoItem(
        id = id,
        text = text,
        importance = stringToImportance(importance),
        deadline = deadline,
        isDone = done,
        creationDate = createdAt,
        modificationDate = changedAt
    )
}