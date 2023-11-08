package com.example.unitodoapp.utils

import com.example.unitodoapp.data.api.model.TodoItemServer
import com.example.unitodoapp.data.db.entities.Todo
import com.example.unitodoapp.data.model.Importance
import com.example.unitodoapp.data.model.TodoItem
import java.text.SimpleDateFormat
import java.util.Locale


fun Long.convertToDateFormat(): String =
    SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(this)

fun String.convertToImportance(): Importance =
    when (this) {
        "important" -> Importance.IMPORTANT
        "basic" -> Importance.BASIC
        else -> Importance.LOW
    }
fun createTodo(todoItem: TodoItem): Todo {
    return Todo(
        id = todoItem.id,
        text = todoItem.text,
        importanceId = getImportanceId(todoItem.importance),
        deadline = todoItem.deadline,
        done = todoItem.isDone,
        createdAt = todoItem.creationDate,
        changedAt = todoItem.modificationDate
    )
}

fun getImportanceId(importance: Importance): Int {
    return when (importance) {
        Importance.IMPORTANT -> IMPORTANCE_IMPORTANT_ID
        Importance.BASIC -> IMPORTANCE_BASIC_ID
        else -> IMPORTANCE_LOW_ID
    }
}

fun toTodoItemServer(todoItem: TodoItem): TodoItemServer {
    return TodoItemServer(
        id = todoItem.id,
        text = todoItem.text,
        importance = todoItem.importance.toStringForItemServer(),
        deadline = todoItem.deadline,
        done = todoItem.isDone,
        createdAt = todoItem.creationDate,
        changedAt = todoItem.modificationDate,
        lastUpdatedBy = "cf1"
    )
}
