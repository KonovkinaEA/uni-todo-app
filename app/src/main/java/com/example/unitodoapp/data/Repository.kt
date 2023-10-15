package com.example.unitodoapp.data

import com.example.unitodoapp.data.model.TodoItem

interface Repository {
    suspend fun getItem(id: String): TodoItem?
    suspend fun addItem(todoItem: TodoItem)
    suspend fun updateItem(todoItem: TodoItem)
    suspend fun removeItem(id: String)
}
