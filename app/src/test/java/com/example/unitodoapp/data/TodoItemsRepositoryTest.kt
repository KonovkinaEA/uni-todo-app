package com.example.unitodoapp.data

import androidx.lifecycle.MutableLiveData
import com.example.unitodoapp.data.api.ApiService
import com.example.unitodoapp.data.db.RevisionDao
import com.example.unitodoapp.data.db.TodoItemDao
import com.example.unitodoapp.data.model.Importance
import com.example.unitodoapp.data.model.TodoItem
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.robolectric.util.ReflectionHelpers

class TodoItemsRepositoryTest {

    private val todoItemDao = mockk<TodoItemDao>(relaxed = true)
    private val apiService = mockk<ApiService>(relaxed = true)
    private val revisionDao = mockk<RevisionDao>(relaxed = true)
    private val _todoItems = mockk<MutableStateFlow<List<TodoItem>>> {
        every { value } returns list
    }
    private val todoItems = mockk<StateFlow<List<TodoItem>>>()
    private val errorListLiveData = mockk<MutableLiveData<Boolean>>()
    private val errorItemLiveData = mockk<MutableLiveData<Boolean>>()

    private lateinit var todoItemsRepository: TodoItemsRepository

    @Before
    fun setUp() {
        todoItemsRepository = TodoItemsRepository(todoItemDao, apiService, revisionDao)

        ReflectionHelpers.setField(
            todoItemsRepository, MUTABLE_STATE_FLOW_TODO_ITEMS, _todoItems
        )
        ReflectionHelpers.setField(todoItemsRepository, STATE_FLOW_TODO_ITEMS, todoItems)
        ReflectionHelpers.setField(todoItemsRepository, ERROR_LIST_LIVE_DATA, errorListLiveData)
        ReflectionHelpers.setField(todoItemsRepository, ERROR_ITEM_LIVE_DATA, errorItemLiveData)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testGetItem() = runTest {
        val item = todoItemsRepository.getItem("1")
        Assert.assertEquals(item, list[0])
    }

    @Test
    fun testGetItemNull() = runTest {
        val item = todoItemsRepository.getItem("2")
        Assert.assertEquals(item, null)
    }

    @Test
    fun testErrorListLiveData() {
        val liveData = todoItemsRepository.errorListLiveData()
        Assert.assertEquals(liveData, errorListLiveData)
    }

    @Test
    fun testErrorItemLiveData() {
        val liveData = todoItemsRepository.errorItemLiveData()
        Assert.assertEquals(liveData, errorItemLiveData)
    }

    @Test
    fun testNumOfCompleted() {
        val expectedCount = list.count { it.isDone }
        val count = todoItemsRepository.numOfCompleted()

        Assert.assertEquals(count, expectedCount)
    }

    @Test
    fun testUndoneTodoItems() {
        val expectedCount = list.filter { !it.isDone }
        val count = todoItemsRepository.undoneTodoItems()

        Assert.assertEquals(count, expectedCount)
    }

    companion object {

        private const val MUTABLE_STATE_FLOW_TODO_ITEMS = "_todoItems"
        private const val STATE_FLOW_TODO_ITEMS = "todoItems"
        private const val ERROR_LIST_LIVE_DATA = "errorListLiveData"
        private const val ERROR_ITEM_LIVE_DATA = "errorItemLiveData"

        private val list = listOf(
            TodoItem(
                id = "1",
                text = "AAA",
                importance = Importance.IMPORTANT,
                deadline = 1,
                isDone = true,
                creationDate = 1,
                modificationDate = 1
            ),
            TodoItem(text = "BBB"),
            TodoItem(text = "CCC", importance = Importance.IMPORTANT),
            TodoItem(text = "DDD", isDone = true),
            TodoItem(text = "EEE", isDone = true),
            TodoItem(text = "FFF", deadline = 1654665100000L),
            TodoItem(text = "XXX", importance = Importance.BASIC),
            TodoItem(text = "YYY", importance = Importance.LOW),
            TodoItem(text = "ZZZ", importance = Importance.LOW)
        )
    }
}
