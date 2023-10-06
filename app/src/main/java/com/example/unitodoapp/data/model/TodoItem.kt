package com.example.unitodoapp.data.model

import java.util.UUID

data class TodoItem(
    val id: String = UUID.randomUUID().toString(),
    var text: String = "",
    var importance: Importance = Importance.LOW,
    var deadline: Long? = null,
    var isDone: Boolean = false,
    val creationDate: Long = System.currentTimeMillis(),
    var modificationDate: Long = System.currentTimeMillis()
)
