package com.example.unitodoapp.ui

import com.example.unitodoapp.MainCoroutineRule
import com.example.unitodoapp.data.Repository
import com.example.unitodoapp.data.datastore.DataStoreManager
import com.example.unitodoapp.data.model.TodoItem
import com.example.unitodoapp.ui.screens.list.ListViewModel
import com.example.unitodoapp.ui.screens.list.actions.ListUiAction
import com.example.unitodoapp.ui.screens.list.actions.ListUiEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.util.ReflectionHelpers

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository = mockk<Repository>(relaxed = true)
    private val dataStoreManager = mockk<DataStoreManager>(relaxed = true)
    private val _uiEvent = mockk<Channel<ListUiEvent>>(relaxed = true) {
        coEvery { send(ListUiEvent.NavigateToNewTodoItem) } just runs
        coEvery { send(ListUiEvent.NavigateToSettings) } just runs
    }
    private val todoItem = mockk<TodoItem>(relaxed = true) {
        every { id } returns "1"
    }
    private lateinit var listViewModel: ListViewModel
    private lateinit var spyListViewModel: ListViewModel
    @Before
    fun setUp() = runTest {
        listViewModel = ListViewModel(repository, dataStoreManager)
        ReflectionHelpers.setField(listViewModel, "_uiEvent", _uiEvent)
        spyListViewModel = spyk(listViewModel, recordPrivateCalls = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testOnUiActionAddNewItem() = runTest {
        listViewModel.onUiAction(ListUiAction.AddNewItem)
        advanceUntilIdle()
        coVerify { _uiEvent.send(ListUiEvent.NavigateToNewTodoItem) }
    }

    @Test
    fun testOnUiActionChangeFilter() = runTest {
        spyListViewModel.onUiAction(ListUiAction.ChangeFilter(true))
        advanceUntilIdle()
        coVerify { spyListViewModel["changeFilterState"](true) }
    }

    @Test
    fun testOnUiActionEditTodoItem() = runTest {
        listViewModel.onUiAction(ListUiAction.EditTodoItem(todoItem))
        advanceUntilIdle()
        coVerify {
            _uiEvent.send(ListUiEvent.NavigateToEditTodoItem("1"))
        }
    }

    @Test
    fun testOnUiActionUpdateTodoItem() = runTest {
        spyListViewModel.onUiAction(ListUiAction.UpdateTodoItem(todoItem))
        advanceUntilIdle()
        coVerify {
            spyListViewModel["updateTodoItem"](todoItem)
        }
    }

    @Test
    fun testOnUiActionRemoveTodoItem() = runTest {
        spyListViewModel.onUiAction(ListUiAction.RemoveTodoItem(todoItem))
        advanceUntilIdle()
        coVerify {
            spyListViewModel["removeTodoItem"](todoItem)
        }
    }

}
