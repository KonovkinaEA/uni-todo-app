package com.example.unitodoapp.data

import androidx.lifecycle.MutableLiveData
import com.example.unitodoapp.data.api.ApiService
import com.example.unitodoapp.data.api.model.AuthResponse
import com.example.unitodoapp.data.api.model.EmailServer
import com.example.unitodoapp.data.api.model.ItemResponse
import com.example.unitodoapp.data.api.model.RecoveryResponse
import com.example.unitodoapp.data.api.model.TodoItemServer
import com.example.unitodoapp.utils.toTodoItemServer
import com.example.unitodoapp.data.api.model.TodoListContainer
import com.example.unitodoapp.data.api.model.TodoListResponse
import com.example.unitodoapp.data.api.model.User
import com.example.unitodoapp.data.db.RevisionDao
import com.example.unitodoapp.data.db.TodoItemDao
import com.example.unitodoapp.data.model.TodoItem
import com.example.unitodoapp.utils.createTodo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class TodoItemsRepository @Inject constructor(
    private val todoItemDao: TodoItemDao,
    private val apiService: ApiService,
    private val revisionDao: RevisionDao
) : Repository {
    private val _todoItems: MutableStateFlow<List<TodoItem>> = MutableStateFlow(listOf())
    override val todoItems = _todoItems.asStateFlow()
    private val errorListLiveData = MutableLiveData<Boolean>()
    private val errorItemLiveData = MutableLiveData<Boolean>()

    private var currentId = ""
    private var currentToken = ""
    private var currentList: List<TodoItemServer>? = null
    private var currentRevision: Long = 0
    override suspend fun getItem(id: String) = _todoItems.value.firstOrNull { it.id == id }

    override suspend fun addItem(todoItem: TodoItem) =
        withContext(Dispatchers.IO) {
            _todoItems.update { currentList ->
                val updatedList = currentList.toMutableList()
                updatedList.add(todoItem)
                updatedList.toList()
            }
            val newTodo = createTodo(todoItem)
            todoItemDao.insertNewTodoItemData(newTodo.toTodoDbEntity())
            try {
                val todoItemServer = toTodoItemServer(todoItem)
                val response = apiService.addTodoItem(
                    revisionDao.getCurrentRevision().toString(),
                    "Bearer $currentToken",
                    currentId,
                    todoItemServer
                )
                updateRevisionNetworkAvailable(response)
            } catch (e: Exception) {
                updateRevisionNetworkUnavailable()
            }
        }

    override suspend fun updateItem(todoItem: TodoItem) =
        withContext(Dispatchers.IO) {
            val containsTodoItem = _todoItems.value.any { it.id == todoItem.id }
            if (containsTodoItem) {
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
                try {
                    val todoItemServer = toTodoItemServer(todoItem)
                    val response = apiService.updateTodoItem(
                        revisionDao.getCurrentRevision().toString(),
                        "Bearer $currentToken",
                        currentId, todoItem.id, todoItemServer
                    )
                    updateRevisionNetworkAvailable(response)
                } catch (e: Exception) {
                    updateRevisionNetworkUnavailable()
                }
            }
        }

    override suspend fun removeItem(id: String) =
        withContext(Dispatchers.IO) {
            val containsTodoItem = _todoItems.value.any { it.id == id }
            if (containsTodoItem) {
                _todoItems.update { currentList ->
                    currentList.filter { it.id != id }
                }
                todoItemDao.deleteTodoDataById(id)
                try {
                    val response = apiService.deleteTodoItem(
                        revisionDao.getCurrentRevision().toString(),
                        "Bearer $currentToken",
                        currentId, id
                    )
                    updateRevisionNetworkAvailable(response)
                } catch (e: Exception) {
                    updateRevisionNetworkUnavailable()
                }
            }
        }

    private suspend fun updateRevisionNetworkAvailable(response: Response<ItemResponse>) =
        withContext(Dispatchers.IO) {
            if (response.isSuccessful) {
                val dataFromServer = response.body() as ItemResponse
                revisionDao.updateRevision(dataFromServer.revision)
                errorItemLiveData.postValue(false)
            } else {
                errorItemLiveData.postValue(true)
            }
        }

    private fun updateRevisionNetworkUnavailable() =
        revisionDao.updateRevision(revisionDao.getCurrentRevision() + 1)

    override suspend fun loadDataFromDB() {
        withContext(Dispatchers.IO) {
            _todoItems.value = todoItemDao.getAllTodoData().map { it.toTodoItem() }
            errorListLiveData.postValue(true)
        }
    }

    override suspend fun loadDataFromServer() =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllTodoData(
                    "Bearer $currentToken",
                    currentId
                )

                if (response.isSuccessful) {
                    val dataFromServer = response.body() as TodoListResponse
                    if (dataFromServer.revision > revisionDao.getCurrentRevision()) {
                        updateDataDB(dataFromServer)
                    } else {
                        updateDataOnServer()
                    }
                    errorListLiveData.postValue(false)

                } else handleErrorWhenLoading()
            } catch (e: Exception) {
                handleErrorWhenLoading()
            }
        }

    private suspend fun handleErrorWhenLoading() {
        loadDataFromDB()
        errorListLiveData.postValue(true)
    }

    private suspend fun updateDataOnServer() =
        withContext(Dispatchers.IO) {
            _todoItems.value = todoItemDao.getAllTodoData().map { it.toTodoItem() }

            val todoListServer = TodoListContainer(todoItems.value.map { toTodoItemServer(it) })
            val response =
                apiService.patchList(
                    revisionDao.getCurrentRevision().toString(),
                    "Bearer $currentToken",
                    currentId,
                    todoListServer
                )

            if (response.isSuccessful) {
                val dataFromServer = response.body() as TodoListResponse
                revisionDao.updateRevision(dataFromServer.revision)
                errorItemLiveData.postValue(false)
            } else {
                errorItemLiveData.postValue(true)
            }
        }

    private suspend fun updateDataDB(dataFromServer: TodoListResponse) =
        withContext(Dispatchers.IO) {
            val todoItemsFromServer =
                dataFromServer.list?.map { it.toTodoItem() }?.toMutableList() ?: listOf()

            revisionDao.updateRevision(dataFromServer.revision)
            _todoItems.value = todoItemsFromServer

            val todoDbList = _todoItems.value.map { createTodo(it).toTodoDbEntity() }
            todoItemDao.replaceAllTodoItems(todoDbList)
        }

    override suspend fun reloadData() = loadDataFromServer()

    override fun errorListLiveData() = errorListLiveData

    override fun errorItemLiveData() = errorItemLiveData

    override fun numOfCompleted(): Int = _todoItems.value.count { it.isDone }

    override fun undoneTodoItems(): List<TodoItem> = _todoItems.value.filter { !it.isDone }

    override suspend fun registerNewUser(user: User): Boolean {
        return try {
            val response = apiService.registerNewUser(user)
            return patchListAfterRegistration(response)
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun patchListAfterRegistration(response: Response<AuthResponse>): Boolean {
        var flag = false
        withContext(Dispatchers.IO) {
            if (response.isSuccessful) {
                val dataFromServer = response.body() as AuthResponse
                apiService.patchList(
                    "1",
                    "Bearer ${dataFromServer.accessToken}",
                    dataFromServer.user.id,
                    TodoListContainer(emptyList())
                )
                flag = true
            }
        }
        return flag
    }

    override suspend fun logInUser(user: User): Boolean {
        return try {
            val response = apiService.logInUser(user)
            return updateLoginData(response)
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun updateLoginData(response: Response<AuthResponse>): Boolean {
        var flag = false
        withContext(Dispatchers.IO) {
            if (response.isSuccessful) {
                val dataFromServer = response.body() as AuthResponse
                currentId = dataFromServer.user.id
                currentToken = dataFromServer.accessToken
                flag = true
            }
        }
        return flag
    }

    override suspend fun checkUserExist(user: User): Boolean {
        return try {
            val response = apiService.checkUserExist(EmailServer(user.email))
            return updateRecoveryData(response)
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun updateRecoveryData(response: Response<RecoveryResponse>): Boolean {
        withContext(Dispatchers.IO) {
            if (response.isSuccessful) {
                val dataFromServer = response.body() as RecoveryResponse
                currentList = dataFromServer.list
                currentRevision = dataFromServer.revision
                currentId = dataFromServer.userId.toString()
            }
        }
        return currentList != null
    }

    override suspend fun recoveryPassword(user: User) {
        withContext(Dispatchers.IO) {
            apiService.resetUser(currentId, user)
            val response = apiService.logInUser(user)
            val dataFromServer = response.body() as AuthResponse
            apiService.patchList(
                currentRevision.toString(),
                "Bearer ${dataFromServer.accessToken}",
                dataFromServer.user.id,
                TodoListContainer(currentList!!)
            )
        }
    }

    override suspend fun clearDatabase() {
        withContext(Dispatchers.IO) {
            todoItemDao.deleteAllTodoItems()
            revisionDao.setRevisionToOne()
        }
    }
}