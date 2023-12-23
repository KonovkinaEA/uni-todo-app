package com.example.unitodoapp.data.api.model

data class TodoListResponse(
    val id: Int,
    val list: List<TodoItemServer>?,
    val revision: Long
)