package com.example.unitodoapp.data

import com.example.unitodoapp.data.db.TodoItemDao
import com.example.unitodoapp.data.model.TodoItem
import com.example.unitodoapp.utils.createTodo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoItemsRepository @Inject constructor(
    private val todoItemDao: TodoItemDao
) : Repository {
    private val _todoItems: MutableStateFlow<List<TodoItem>> = MutableStateFlow(listOf())
    override val todoItems = _todoItems.asStateFlow()
    override suspend fun getItem(id: String) = _todoItems.value.firstOrNull { it.id == id }

    override suspend fun addItem(todoItem: TodoItem) {
        _todoItems.update { currentList ->
            val updatedList = currentList.toMutableList()
            updatedList.add(todoItem)
            updatedList.toList()
        }
        val newTodo = createTodo(todoItem)
        todoItemDao.insertNewTodoItemData(newTodo.toTodoDbEntity())
    }

    override suspend fun updateItem(todoItem: TodoItem) {
        _todoItems.update { currentList ->
            currentList.map {
                when (it.id) {
                    todoItem.id -> todoItem
                    else -> it
                }
            }
        }
        val updatedTodo = createTodo(todoItem)
        todoItemDao.updateTodoData(updatedTodo.toTodoDbEntity())
    }

    override suspend fun removeItem(id: String) {
        _todoItems.update { currentList ->
            currentList.filter { it.id != id }
        }
        todoItemDao.deleteTodoDataById(id)
    }

    override suspend fun loadDataFromDB() {
        withContext(Dispatchers.IO) {
            _todoItems.value = todoItemDao.getAllTodoData().map { it.toTodoItem() }
        }
    }
}
