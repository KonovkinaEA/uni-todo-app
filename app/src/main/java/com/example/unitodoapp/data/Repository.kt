package com.example.unitodoapp.data

import androidx.lifecycle.MutableLiveData
import com.example.unitodoapp.data.model.TodoItem
import kotlinx.coroutines.flow.StateFlow

interface Repository {
    val todoItems: StateFlow<List<TodoItem>>
    suspend fun getItem(id: String): TodoItem?
    suspend fun addItem(todoItem: TodoItem)
    suspend fun updateItem(todoItem: TodoItem)
    suspend fun removeItem(id: String)
    suspend fun loadDataFromDB()
    suspend fun loadDataFromServer()
    suspend fun reloadData()
    fun errorListLiveData(): MutableLiveData<Boolean>
    fun errorItemLiveData(): MutableLiveData<Boolean>

    fun numOfCompleted(): Int

    fun undoneTodoItems(): List<TodoItem>
}
