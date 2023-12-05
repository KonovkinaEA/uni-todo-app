package com.example.unitodoapp.data

import androidx.lifecycle.MutableLiveData
import com.example.unitodoapp.MainCoroutineRule
import com.example.unitodoapp.data.api.ApiService
import com.example.unitodoapp.data.api.model.AuthResponse
import com.example.unitodoapp.data.api.model.User
import com.example.unitodoapp.data.api.model.UserResponse
import com.example.unitodoapp.data.db.RevisionDao
import com.example.unitodoapp.data.db.TodoItemDao
import com.example.unitodoapp.data.model.TodoItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.fest.assertions.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.util.ReflectionHelpers
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class TodoItemsRepositoryTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val todoItemDao = mockk<TodoItemDao>(relaxed = true)
    private val apiService = mockk<ApiService>(relaxed = true)
    private val revisionDao = mockk<RevisionDao>(relaxed = true)
    private val todoItem = mockk<TodoItem>(relaxed = true) {
        every { isDone } returns false
    }
    private val list = mutableListOf<TodoItem>()
    private val _todoItems = mockk<MutableStateFlow<List<TodoItem>>> {
        every { value } returns list
    }
    private val errorListLiveData = mockk<MutableLiveData<Boolean>>()
    private val errorItemLiveData = mockk<MutableLiveData<Boolean>>()

    private lateinit var todoItemsRepository: TodoItemsRepository
    private lateinit var spyTodoItemsRepository: TodoItemsRepository

    @Before
    fun setUp() {
        repeat(15) { list.add(todoItem) }
        todoItemsRepository = TodoItemsRepository(todoItemDao, apiService, revisionDao)
        spyTodoItemsRepository = spyk(todoItemsRepository, recordPrivateCalls = true)
        ReflectionHelpers.setField(todoItemsRepository, MUTABLE_STATE_FLOW_TODO_ITEMS, _todoItems)
        ReflectionHelpers.setField(todoItemsRepository, ERROR_LIST_LIVE_DATA, errorListLiveData)
        ReflectionHelpers.setField(todoItemsRepository, ERROR_ITEM_LIVE_DATA, errorItemLiveData)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testGetItem() = runTest {
        val newItem = mockk<TodoItem>(relaxed = true) {
            every { id } returns "1"
        }
        list.add(newItem)
        list.shuffle()
        val item = todoItemsRepository.getItem(id = "1")
        advanceUntilIdle()

        Assertions.assertThat(item).isEqualTo(newItem)
    }

    @Test
    fun testGetItemNull() = runTest {
        list.removeIf { it.id == "1" }
        val item = todoItemsRepository.getItem("1")
        advanceUntilIdle()

        Assertions.assertThat(item).isEqualTo(null)
    }

    @Test
    fun testErrorListLiveData() {
        val liveData = todoItemsRepository.errorListLiveData()
        Assertions.assertThat(liveData).isEqualTo(errorListLiveData)
    }

    @Test
    fun testErrorItemLiveData() {
        val liveData = todoItemsRepository.errorItemLiveData()
        Assertions.assertThat(liveData).isEqualTo(errorItemLiveData)
    }

    @Test
    fun testNumOfCompleted() {
        val newItem = mockk<TodoItem>(relaxed = true) {
            every { isDone } returns true
        }
        repeat(3) { list.add(newItem) }
        val count = todoItemsRepository.numOfCompleted()

        Assertions.assertThat(count).isEqualTo(3)
    }

    @Test
    fun testNumOfCompletedZero() {
        val count = todoItemsRepository.numOfCompleted()
        Assertions.assertThat(count).isEqualTo(0)
    }

    @Test
    fun testNumOfCompletedEmptyList() {
        every { _todoItems.value } returns emptyList()
        val count = todoItemsRepository.numOfCompleted()

        Assertions.assertThat(count).isEqualTo(0)
    }

    @Test
    fun testUndoneTodoItems() {
        val count = todoItemsRepository.undoneTodoItems()

        Assertions.assertThat(count).isEqualTo(list)
    }

    @Test
    fun testUndoneTodoItemsZero() {
        every { todoItem.isDone } returns true
        val count = todoItemsRepository.undoneTodoItems()

        Assertions.assertThat(count).isEqualTo(emptyList())
    }

    @Test
    fun testRegisterNewUserSuccess() = runTest {
        val newUser = mockk<User>(relaxed = true) {
            every { email } returns "test@mail.ru"
            every { password } returns "Qwerty7"
        }
        val newUserResponse = mockk<UserResponse>(relaxed = true) {
            every { email } returns "test@mail.ru"
            every { id } returns "1"
        }
        val authResponse = mockk<AuthResponse>(relaxed = true) {
            every { accessToken } returns "someToken"
            every { user } returns newUserResponse
        }
        val successfulResponse = Response.success(authResponse)
        coEvery { apiService.registerNewUser(newUser) } returns successfulResponse
        val result = spyTodoItemsRepository.registerNewUser(newUser)
        advanceUntilIdle()
        coVerify {
            apiService.registerNewUser(newUser)
            spyTodoItemsRepository["patchListAfterRegistration"](successfulResponse)
        }
        Assertions.assertThat(result).isEqualTo(true)
    }

    @Test
    fun testRegisterNewUserFailure() = runTest {
        val newUser = mockk<User>(relaxed = true)
        val errorResponseBody =
            """{"error": "Not Found"}""".toResponseBody("application/json".toMediaType())
        val unsuccessfulResponse = Response.error<AuthResponse>(404, errorResponseBody)
        coEvery { apiService.registerNewUser(newUser) } returns unsuccessfulResponse
        val result = spyTodoItemsRepository.registerNewUser(newUser)
        advanceUntilIdle()
        coVerify {
            apiService.registerNewUser(newUser)
            spyTodoItemsRepository["patchListAfterRegistration"](unsuccessfulResponse)
        }
        Assertions.assertThat(result).isEqualTo(false)
    }


    @Test
    fun testRegisterNewUserError() = runTest {
        val newUser = mockk<User>(relaxed = true)
        coEvery { apiService.registerNewUser(any()) } throws Exception("Some error")
        val result = spyTodoItemsRepository.registerNewUser(newUser)
        advanceUntilIdle()
        coVerify {
            apiService.registerNewUser(newUser)
        }
        Assertions.assertThat(result).isEqualTo(false)
    }

    companion object {

        private const val MUTABLE_STATE_FLOW_TODO_ITEMS = "_todoItems"
        private const val ERROR_LIST_LIVE_DATA = "errorListLiveData"
        private const val ERROR_ITEM_LIVE_DATA = "errorItemLiveData"
    }
}
