package com.example.unitodoapp.data

import com.example.unitodoapp.data.model.Importance
import com.example.unitodoapp.data.model.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class Repository @Inject constructor() : TodoItemsRepository {
    private val _todoItems: MutableStateFlow<List<TodoItem>> = MutableStateFlow(listOf())
    val todoItems = _todoItems.asStateFlow()

    init {
        _todoItems.update { getHardcodedTodoItems() }
    }

    override suspend fun getTodoItem(id: String) = _todoItems.value.firstOrNull { it.id == id }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        _todoItems.update { currentList ->
            val updatedList = currentList.toMutableList()
            updatedList.add(todoItem)
            updatedList.toList()
        }
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        _todoItems.update { currentList ->
            currentList.map {
                when (it.id) {
                    todoItem.id -> todoItem
                    else -> it
                }
            }
        }
    }

    override suspend fun removeTodoItem(id: String) {
        _todoItems.update { currentList ->
            currentList.filter { it.id != id }
        }
    }

    private fun getHardcodedTodoItems(): List<TodoItem> {
        return listOf(
            TodoItem(
                text = "Закончить проект",
                importance = Importance.IMPORTANT
            ),
            TodoItem(
                text = "Купить продукты",
                importance = Importance.BASIC
            ),
            TodoItem(
                text = "Подготовить презентацию",
                importance = Importance.IMPORTANT,
                deadline = 1693774800000L
            ),
            TodoItem(
                text = "Прочитать книгу",
                deadline = 1696453200000L
            ),
            TodoItem(
                text = "Сходить в спортзал",
                importance = Importance.BASIC,
                isDone = true
            ),
            TodoItem(
                text = "Записаться на курс программирования",
                importance = Importance.IMPORTANT,
                deadline = 1699650000000L
            ),
            TodoItem(
                text = "Организовать семейный ужин",
                importance = Importance.BASIC,
                deadline = 1699131600000L
            ),
            TodoItem(
                text = "Приготовить подарок к дню рождения друга",
                isDone = true
            ),
            TodoItem(
                text = "Проверить и отредактировать доклад для конференции по программированию." +
                        "Подготовить презентацию, составить план выступления и подобрать иллюстрации." +
                        "Уделить особое внимание структуре и логической последовательности." +
                        "Проверить правильность использования терминов и грамматических конструкций.",
                importance = Importance.IMPORTANT,
                deadline = 1698526800000L
            ),
            TodoItem(
                text = "Прогуляться в парке"
            ),
            TodoItem(
                text = "Завершить исследовательскую работу",
                importance = Importance.IMPORTANT
            ),
            TodoItem(
                text = "Оплатить счета",
                importance = Importance.BASIC,
                isDone = true
            ),
            TodoItem(
                text = "Разработать новый дизайн интерфейса",
                importance = Importance.IMPORTANT,
                deadline = 1697317200000L
            ),
            TodoItem(
                text = "Посмотреть новый фильм",
                deadline = 1703970000000L
            ),
            TodoItem(
                text = "Подготовиться к собеседованию",
                importance = Importance.BASIC,
                isDone = true
            )
        )
    }
}
