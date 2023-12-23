package com.example.unitodoapp.data.api.model

data class RecoveryResponse (
    val list: List<TodoItemServer>?,
    val revision: Long,
    val userId: Long
)