package com.example.unitodoapp.data.api.model

data class TodoListResponse(
    val list: List<TodoItemServer>?,
    val revision: Long
)