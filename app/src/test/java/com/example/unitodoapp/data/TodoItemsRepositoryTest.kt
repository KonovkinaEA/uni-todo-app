package com.example.unitodoapp.data

import androidx.lifecycle.MutableLiveData
import com.example.unitodoapp.MainCoroutineRule
import com.example.unitodoapp.data.api.ApiService
import com.example.unitodoapp.data.api.model.AuthResponse
import com.example.unitodoapp.data.api.model.EmailServer
import com.example.unitodoapp.data.api.model.ItemResponse
import com.example.unitodoapp.data.api.model.RecoveryResponse
import com.example.unitodoapp.data.api.model.TodoItemServer
import com.example.unitodoapp.data.api.model.TodoListContainer
import com.example.unitodoapp.data.api.model.TodoListResponse
import com.example.unitodoapp.data.api.model.User
import com.example.unitodoapp.data.api.model.UserResponse
import com.example.unitodoapp.data.db.RevisionDao
import com.example.unitodoapp.data.db.TodoItemDao
import com.example.unitodoapp.data.model.TodoItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
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
    private val user = mockk<User>(relaxed = true) {
        every { email } returns "test@mail.ru"
        every { password } returns "Qwerty7"
    }
    private val userResponse = mockk<UserResponse>(relaxed = true) {
        every { email } returns "test@mail.ru"
        every { id } returns "1"
    }
    private val authResponse = mockk<AuthResponse>(relaxed = true) {
        every { accessToken } returns "someToken"
        every { user } returns userResponse
    }
    private val successfulAuthResponse = Response.success(authResponse)
    private val errorResponseBody =
        """{"error": "Not Found"}""".toResponseBody("application/json".toMediaType())
    private val unsuccessfulAuthResponse = Response.error<AuthResponse>(404, errorResponseBody)
    private val todoItemsServer = mutableListOf<TodoItemServer>()
    private val todoItemServer = mockk<TodoItemServer>(relaxed = true) {
        every { id } returns "1"
    }
    private val emailServer = mockk<EmailServer>(relaxed = true) {
        every { email } returns user.email
    }
    private val recoveryResponse = mockk<RecoveryResponse>(relaxed = true) {
        every { list } returns todoItemsServer
        every { revision } returns 1
        every { userId } returns 1
    }
    private val successfulRecoveryResponse = Response.success(recoveryResponse)
    private val unsuccessfulRecoveryResponse =
        Response.error<RecoveryResponse>(404, errorResponseBody)
    private val todoListResponse = mockk<TodoListResponse>(relaxed = true) {
        every { id } returns 1
        every { list } returns todoItemsServer
        every { revision } returns 5
    }
    private val successfulTodoListResponse = Response.success(todoListResponse)
    private val unSuccessfulTodoListResponse =
        Response.error<TodoListResponse>(404, errorResponseBody)
    private val itemResponse = mockk<ItemResponse>(relaxed = true) {
        every { element } returns todoItemServer
        every { revision } returns 1
    }
    private val successfulItemResponse = Response.success(itemResponse)
    private val todoListContainer = mockk<TodoListContainer>(relaxed = true) {
        every { list } returns todoItemsServer
    }
    private var todoItemList = mutableListOf<TodoItem>()
    private val _todoItems = mockk<MutableStateFlow<List<TodoItem>>> {
        every { value } returns todoItemList
    }
    private val errorListLiveData = mockk<MutableLiveData<Boolean>>()
    private val errorItemLiveData = mockk<MutableLiveData<Boolean>>()

    private lateinit var todoItemsRepository: TodoItemsRepository
    private lateinit var spyTodoItemsRepository: TodoItemsRepository

    @Before
    fun setUp() {
        repeat(15) { todoItemList.add(todoItem) }
        repeat(15) { todoItemsServer.add(todoItemServer) }
        todoItemsRepository = TodoItemsRepository(todoItemDao, apiService, revisionDao)
        spyTodoItemsRepository = spyk(todoItemsRepository, recordPrivateCalls = true)
        ReflectionHelpers.setField(todoItemsRepository, MUTABLE_STATE_FLOW_TODO_ITEMS, _todoItems)
        ReflectionHelpers.setField(todoItemsRepository, ERROR_LIST_LIVE_DATA, errorListLiveData)
        ReflectionHelpers.setField(todoItemsRepository, ERROR_ITEM_LIVE_DATA, errorItemLiveData)
        ReflectionHelpers.setField(todoItemsRepository, "currentList", todoItemsServer)
        ReflectionHelpers.setField(todoItemsRepository, "currentId", userResponse.id)
        ReflectionHelpers.setField(
            todoItemsRepository,
            "currentRevision",
            todoListResponse.revision
        )
        mockkConstructor(TodoListContainer::class)
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
        todoItemList.add(newItem)
        todoItemList.shuffle()
        val item = todoItemsRepository.getItem(id = "1")
        advanceUntilIdle()

        Assertions.assertThat(item).isEqualTo(newItem)
    }

    @Test
    fun testGetItemNull() = runTest {
        todoItemList.removeIf { it.id == "1" }
        val item = todoItemsRepository.getItem("1")
        advanceUntilIdle()

        Assertions.assertThat(item).isEqualTo(null)
    }

    @Test
    fun testAddItem() = runTest {
        coEvery { revisionDao.getCurrentRevision() } returns 1
        coEvery { todoItemDao.insertNewTodoItemData(any()) } returns Unit
        coEvery {
            apiService.addTodoItem(
                any(),
                any(),
                any(),
                any()
            )
        } returns successfulItemResponse
        coEvery { spyTodoItemsRepository["updateRevisionNetworkAvailable"](successfulItemResponse) } returns Unit
        spyTodoItemsRepository.addItem(todoItem)
        advanceUntilIdle()
        coVerify {
            todoItemDao.insertNewTodoItemData(any())
            apiService.addTodoItem(any(), any(), any(), any())
            spyTodoItemsRepository["updateRevisionNetworkAvailable"](successfulItemResponse)
        }

    }

//    @Test
//    fun testUpdateItem() = runTest {
//        val newTodoItemStart = mockk<TodoItem>(relaxed = true) {
//            every { id } returns "1"
//            every { isDone } returns false
//        }
//        val newTodoItem = mockk<TodoItem>(relaxed = true) {
//            every { id } returns "1"
//            every { isDone } returns true
//        }
//        coEvery { _todoItems.value } returns todoItemList
//        todoItemList.add(newTodoItemStart)
//        coEvery { revisionDao.getCurrentRevision() } returns 1
//        coEvery { todoItemDao.updateTodoData(any()) } returns Unit
//        coEvery {
//            apiService.updateTodoItem(
//                any(),
//                any(),
//                any(),
//                any(),
//                any()
//            )
//        } returns successfulItemResponse
//        coEvery { spyTodoItemsRepository["updateRevisionNetworkAvailable"](successfulItemResponse) } returns Unit
//        spyTodoItemsRepository.updateItem(newTodoItem)
//        advanceUntilIdle()
//        coVerify {
//            todoItemDao.updateTodoData(any())
//            apiService.updateTodoItem(any(), any(), any(), any(), any())
//            spyTodoItemsRepository["updateRevisionNetworkAvailable"](successfulItemResponse)
//        }
//    }
//
//    @Test
//    fun testRemoveItem() = runTest {
//    }
//        @Test
//    fun testLoadDataFromDB() = runTest {
//        val todoItemInfoTuple = mockk<TodoItemInfoTuple>(relaxed = true)
//        val list = mutableListOf<TodoItemInfoTuple>()
//        repeat(10) { list.add(todoItemInfoTuple) }
//        coEvery { todoItemDao.getAllTodoData() } returns list
//        coEvery { errorItemLiveData.postValue(true) } returns Unit
//        todoItemsRepository.loadDataFromDB()
//        advanceUntilIdle()
//        coVerify {
//            todoItemDao.getAllTodoData()
//        }
//    }

    @Test
    fun testLoadDataFromServerUpdateDB() = runTest {
        coEvery {
            apiService.getAllTodoData(
                "Bearer ${authResponse.accessToken}",
                userResponse.id
            )
        } returns successfulTodoListResponse
        revisionDao.setRevisionToOne()
        coEvery { spyTodoItemsRepository["updateDataDB"](successfulTodoListResponse.body()) } returns Unit
        coEvery { errorItemLiveData.postValue(false) } returns Unit
        withContext(Dispatchers.IO) {
            spyTodoItemsRepository.loadDataFromServer()
        }
        advanceUntilIdle()
        coVerify {
            apiService.getAllTodoData(any(), any())
            spyTodoItemsRepository["updateDataDB"](successfulTodoListResponse.body())
        }
    }

    @Test
    fun testLoadDataFromServerUpdateDataOnServer() = runTest {
        val newTodoListResponse = mockk<TodoListResponse>(relaxed = true) {
            every { id } returns 1
            every { list } returns todoItemsServer
            every { revision } returns 0
        }
        val newSuccessfulTodoListResponse = Response.success(newTodoListResponse)
        coEvery {
            apiService.getAllTodoData(
                "Bearer ${authResponse.accessToken}",
                userResponse.id
            )
        } returns newSuccessfulTodoListResponse
        revisionDao.setRevisionToOne()
        coEvery { spyTodoItemsRepository["updateDataOnServer"]() } returns Unit
        coEvery { errorItemLiveData.postValue(false) } returns Unit
        withContext(Dispatchers.IO) {
            spyTodoItemsRepository.loadDataFromServer()
        }
        advanceUntilIdle()
        coVerify {
            apiService.getAllTodoData(any(), any())
            spyTodoItemsRepository["updateDataOnServer"]()
        }
    }

    @Test
    fun testLoadDataFromServerError() = runTest {
        coEvery {
            apiService.getAllTodoData(
                "Bearer ${authResponse.accessToken}",
                userResponse.id
            )
        } returns unSuccessfulTodoListResponse
        coEvery {
            spyTodoItemsRepository["handleErrorWhenLoading"]()
        } returns Unit
        withContext(Dispatchers.IO) {
            spyTodoItemsRepository.loadDataFromServer()
        }
        advanceUntilIdle()
        coVerify {
            apiService.getAllTodoData(any(), any())
            spyTodoItemsRepository["handleErrorWhenLoading"]()
        }
    }

    @Test
    fun testReloadData() = runTest {
        coEvery { spyTodoItemsRepository.loadDataFromServer() } returns Unit
        spyTodoItemsRepository.reloadData()
        advanceUntilIdle()
        coVerify { spyTodoItemsRepository.loadDataFromServer() }
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
        repeat(3) { todoItemList.add(newItem) }
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

        Assertions.assertThat(count).isEqualTo(todoItemList)
    }

    @Test
    fun testUndoneTodoItemsZero() {
        every { todoItem.isDone } returns true
        val count = todoItemsRepository.undoneTodoItems()

        Assertions.assertThat(count).isEqualTo(emptyList())
    }

    @Test
    fun testRegisterNewUserSuccess() = runTest {
        coEvery { apiService.registerNewUser(user) } returns successfulAuthResponse
        val result = spyTodoItemsRepository.registerNewUser(user)
        advanceUntilIdle()
        coVerify {
            apiService.registerNewUser(user)
            spyTodoItemsRepository["patchListAfterRegistration"](successfulAuthResponse)
        }

        Assertions.assertThat(result).isEqualTo(true)
    }

    @Test
    fun testRegisterNewUserFailure() = runTest {
        coEvery { apiService.registerNewUser(user) } returns unsuccessfulAuthResponse
        val result = spyTodoItemsRepository.registerNewUser(user)
        advanceUntilIdle()
        coVerify {
            apiService.registerNewUser(user)
            spyTodoItemsRepository["patchListAfterRegistration"](unsuccessfulAuthResponse)
        }

        Assertions.assertThat(result).isEqualTo(false)
    }


    @Test
    fun testRegisterNewUserError() = runTest {
        coEvery { apiService.registerNewUser(any()) } throws Exception("Some error")
        val result = spyTodoItemsRepository.registerNewUser(user)
        advanceUntilIdle()
        coVerify {
            apiService.registerNewUser(user)
        }

        Assertions.assertThat(result).isEqualTo(false)
    }

    @Test
    fun testLogInUserSuccess() = runTest {
        coEvery { apiService.logInUser(user) } returns successfulAuthResponse
        val result = spyTodoItemsRepository.logInUser(user)
        advanceUntilIdle()
        coVerify {
            apiService.logInUser(user)
            spyTodoItemsRepository["updateLoginData"](successfulAuthResponse)
        }

        Assertions.assertThat(result).isEqualTo(true)
    }

    @Test
    fun testLogInUserFailure() = runTest {
        coEvery { apiService.logInUser(user) } returns unsuccessfulAuthResponse
        val result = spyTodoItemsRepository.logInUser(user)
        advanceUntilIdle()
        coVerify {
            apiService.logInUser(user)
            spyTodoItemsRepository["updateLoginData"](unsuccessfulAuthResponse)
        }

        Assertions.assertThat(result).isEqualTo(false)
    }

    @Test
    fun testLogInUserError() = runTest {
        coEvery { apiService.logInUser(user) } throws Exception("Some error")
        val result = spyTodoItemsRepository.logInUser(user)
        advanceUntilIdle()
        coVerify {
            apiService.logInUser(user)
        }

        Assertions.assertThat(result).isEqualTo(false)
    }

    @Test
    fun testCheckUserExistSuccess() = runTest {
        coEvery { apiService.checkUserExist(emailServer) } returns successfulRecoveryResponse
        val result = spyTodoItemsRepository.checkUserExist(user)
        advanceUntilIdle()
        coVerify {
            apiService.checkUserExist(emailServer)
            spyTodoItemsRepository["updateRecoveryData"](successfulRecoveryResponse)
        }

        Assertions.assertThat(result).isEqualTo(true)
    }

    @Test
    fun testCheckUserExistFailure() = runTest {
        coEvery { apiService.checkUserExist(emailServer) } returns unsuccessfulRecoveryResponse
        val result = spyTodoItemsRepository.checkUserExist(user)
        advanceUntilIdle()
        coVerify {
            apiService.checkUserExist(emailServer)
            spyTodoItemsRepository["updateRecoveryData"](unsuccessfulRecoveryResponse)
        }

        Assertions.assertThat(result).isEqualTo(false)
    }

    @Test
    fun testCheckUserExistError() = runTest {
        coEvery { apiService.checkUserExist(emailServer) } throws Exception("Some error")
        val result = spyTodoItemsRepository.checkUserExist(user)
        advanceUntilIdle()
        coVerify {
            apiService.checkUserExist(emailServer)
        }

        Assertions.assertThat(result).isEqualTo(false)
    }

    @Test
    fun testRecoveryPassword() = runTest {
        coEvery { apiService.resetUser(userResponse.id, user) } returns Unit
        coEvery { apiService.logInUser(user) } returns successfulAuthResponse
        coEvery {
            apiService.patchList(
                recoveryResponse.revision.toString(),
                "Bearer ${authResponse.accessToken}",
                userResponse.id,
                todoListContainer
            )
        } returns successfulTodoListResponse
        todoItemsRepository.recoveryPassword(user)
        advanceUntilIdle()
        coVerify {
            apiService.resetUser(any(), user)
            apiService.logInUser(user)
            apiService.patchList(
                any(),
                any(),
                any(),
                any()
            )
        }
    }

    @Test
    fun testClearDatabase() = runTest {
        todoItemsRepository.clearDatabase()
        advanceUntilIdle()
        coVerify {
            todoItemDao.deleteAllTodoItems()
            revisionDao.setRevisionToOne()
        }

    }

    companion object {

        private const val MUTABLE_STATE_FLOW_TODO_ITEMS = "_todoItems"
        private const val ERROR_LIST_LIVE_DATA = "errorListLiveData"
        private const val ERROR_ITEM_LIVE_DATA = "errorItemLiveData"
    }
}
