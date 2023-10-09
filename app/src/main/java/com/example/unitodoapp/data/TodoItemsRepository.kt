package com.example.unitodoapp.data

import com.example.unitodoapp.data.model.TodoItem

interface TodoItemsRepository {
    suspend fun getTodoItem(id: String): TodoItem?
    suspend fun addTodoItem(todoItem: TodoItem)
    suspend fun updateTodoItem(todoItem: TodoItem)
    suspend fun removeTodoItem(id: String)
}