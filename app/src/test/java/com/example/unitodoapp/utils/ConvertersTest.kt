package com.example.unitodoapp.utils

import com.example.unitodoapp.data.api.model.TodoItemServer
import com.example.unitodoapp.data.db.entities.Todo
import com.example.unitodoapp.data.model.Importance
import com.example.unitodoapp.data.model.TodoItem
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class ConvertersTest {

    @RunWith(Parameterized::class)
    class ConvertToDateFormatTest(
        private val parameterValue: Long,
        private val expectedDate: String
    ) {

        @Test
        fun testConvertToDateFormat() {
            val date = parameterValue.convertToDateFormat()
            Assert.assertEquals(date, expectedDate)
        }

        companion object {

            @JvmStatic
            @Parameterized.Parameters(name = "parameterValue = {0}, expectedDate = {1}")
            fun params(): Collection<Array<Any>> {
                return listOf(
                    arrayOf(1701441480000, "1 December 2023"),
                    arrayOf(1715267552000, "9 May 2024"),
                    arrayOf(1, "1 January 1970"),
                    arrayOf(0, "1 January 1970")
                )
            }
        }
    }

    @RunWith(Parameterized::class)
    class ConvertToImportanceTest(
        private val parameterValue: String,
        private val expectedImportance: Importance
    ) {

        @Test
        fun testConvertToImportance() {
            val importance = parameterValue.convertToImportance()
            Assert.assertEquals(importance, expectedImportance)
        }

        companion object {

            @JvmStatic
            @Parameterized.Parameters(name = "parameterValue = {0}, expectedImportance = {1}")
            fun params(): Collection<Array<Any>> {
                return listOf(
                    arrayOf("important", Importance.IMPORTANT),
                    arrayOf("basic", Importance.BASIC),
                    arrayOf("low", Importance.LOW),
                    arrayOf("a", Importance.LOW),
                    arrayOf("", Importance.LOW)
                )
            }
        }
    }

    @RunWith(Parameterized::class)
    class GetImportanceIdTest(
        private val parameterValue: Importance,
        private val expectedId: Int
    ) {

        @Test
        fun testGetImportanceId() {
            val id = getImportanceId(parameterValue)
            Assert.assertEquals(id, expectedId)
        }

        companion object {

            @JvmStatic
            @Parameterized.Parameters(name = "parameterValue = {0}, expectedId = {1}")
            fun params(): Collection<Array<Any>> {
                return listOf(
                    arrayOf(Importance.IMPORTANT, IMPORTANCE_IMPORTANT_ID),
                    arrayOf(Importance.BASIC, IMPORTANCE_BASIC_ID),
                    arrayOf(Importance.LOW, IMPORTANCE_LOW_ID)
                )
            }
        }
    }

    @RunWith(Parameterized::class)
    class CreateTodoTest(
        private val parameterValue: TodoItem,
        private val expectedTodo: Todo
    ) {

        @Test
        fun testCreateTodo() {
            val todo = createTodo(parameterValue)
            Assert.assertEquals(todo, expectedTodo)
        }

        companion object {

            @JvmStatic
            @Parameterized.Parameters(name = "parameterValue = {0}, expectedTodo = {1}")
            fun params(): Collection<Array<Any>> {
                return listOf(
                    arrayOf(
                        TodoItem(
                            "1", "text", Importance.IMPORTANT, 2, true,
                            3, 4
                        ),
                        Todo(
                            "1", 3, "text", 2, true,
                            3, 4
                        )
                    ),
                    arrayOf(
                        TodoItem(id = "1", creationDate = 0, modificationDate = 0),
                        Todo(
                            "1", 1, "", null, false,
                            0, 0
                        )
                    )
                )
            }
        }
    }

    @RunWith(Parameterized::class)
    class ToTodoItemServerTest(
        private val parameterValue: TodoItem,
        private val expectedTodoItemServer: TodoItemServer
    ) {

        @Test
        fun testCreateTodo() {
            val todoItemServer = toTodoItemServer(parameterValue)
            Assert.assertEquals(todoItemServer, expectedTodoItemServer)
        }

        companion object {

            @JvmStatic
            @Parameterized.Parameters(name = "parameterValue = {0}, expectedTodoItemServer = {1}")
            fun params(): Collection<Array<Any>> {
                return listOf(
                    arrayOf(
                        TodoItem(
                            "1", "text", Importance.IMPORTANT, 2, true,
                            3, 4
                        ),
                        TodoItemServer(
                            "1", "text", "important", 2, true,
                            null, 3, 4, "cf1"
                        )
                    ),
                    arrayOf(
                        TodoItem(id = "1", creationDate = 0, modificationDate = 0),
                        TodoItemServer(
                            "1", "", "low", null, false,
                            null, 0, 0, "cf1"
                        )
                    )
                )
            }
        }
    }
}
