package com.example.unitodoapp.data.db.entities

data class Todo(
    val id: String,
    var importanceId: Int,
    var text: String,
    var deadline: Long?,
    var done: Boolean,
    val createdAt: Long,
    var changedAt: Long
) {

    fun toTodoDbEntity(): TodoDbEntity = TodoDbEntity(
        id = id,
        importanceId = importanceId,
        text = text,
        deadline = deadline,
        done = done,
        createdAt = createdAt,
        changedAt = changedAt
    )
}