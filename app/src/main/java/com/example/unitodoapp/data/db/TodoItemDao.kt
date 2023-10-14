package com.example.unitodoapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.unitodoapp.data.db.entities.TodoDbEntity

@Dao
interface TodoItemDao {

    @Insert(entity = TodoDbEntity::class)
    fun insertNewTodoItemData(todo: TodoDbEntity)

    @Query("SELECT todo.id, importance_name, text, deadline, done, createdAt, changedAt FROM todo\n" +
            "INNER JOIN importance_levels ON todo.importance_id = importance_levels.id")
    fun getAllTodoData(): List<TodoItemInfoTuple>

    @Query("DELETE FROM todo WHERE id = :todoId")
    fun deleteTodoDataById(todoId: String)

    @Query("SELECT todo.id, importance_name, text, deadline, done, createdAt, changedAt FROM todo\n" +
            "INNER JOIN importance_levels ON todo.importance_id = importance_levels.id\n" +
            "WHERE todo.id = :todoId")
    fun getTodoDataById(todoId: String): TodoItemInfoTuple?

    @Update(entity = TodoDbEntity::class)
    fun updateTodoData(todo: TodoDbEntity)

    @Transaction
    fun replaceAllTodoItems(todoItems: List<TodoDbEntity>) {
        deleteAllTodoItems()
        todoItems.forEach {
            insertNewTodoItemData(it)
        }
    }

    @Query("DELETE FROM todo")
    fun deleteAllTodoItems()
}