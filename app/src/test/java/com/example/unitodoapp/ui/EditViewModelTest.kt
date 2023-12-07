package com.example.unitodoapp.ui

import androidx.lifecycle.SavedStateHandle
import com.example.unitodoapp.MainCoroutineRule
import com.example.unitodoapp.data.Repository
import com.example.unitodoapp.data.model.Importance
import com.example.unitodoapp.data.model.TodoItem
import com.example.unitodoapp.notifications.TodoAlarmScheduler
import com.example.unitodoapp.ui.screens.edit.EditViewModel
import com.example.unitodoapp.ui.screens.edit.actions.EditUiAction
import com.example.unitodoapp.ui.screens.edit.actions.EditUiEvent
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
import org.fest.assertions.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.util.ReflectionHelpers

@OptIn(ExperimentalCoroutinesApi::class)
class EditViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val alarmScheduler = mockk<TodoAlarmScheduler>(relaxed = true)
    private val todoItem = mockk<TodoItem>(relaxed = true) {
        every { isDone } returns false
        every { id } returns "1"
        every { deadline } returns 1354135135L
        every { creationDate } returns 1354135035L
        every { modificationDate } returns 1354135035L
        every { text } returns "test"
        every { importance } returns Importance.BASIC
    }
    private val repository = mockk<Repository>(relaxed = true) {
        coEvery { getItem(any()) } returns todoItem
    }
    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true) {
        coEvery { get<String>(any()) } returns "1"
    }
    private val _uiEvent = mockk<Channel<EditUiEvent>>(relaxed = true) {
        coEvery { send(EditUiEvent.NavigateUp) } just runs
    }

    private lateinit var editViewModel: EditViewModel
    private lateinit var spyEditViewModel: EditViewModel

    @Before
    fun setUp() = runTest {
        editViewModel = EditViewModel(alarmScheduler, repository, savedStateHandle)
        ReflectionHelpers.setField(editViewModel, "_uiEvent", _uiEvent)
        spyEditViewModel = spyk(editViewModel, recordPrivateCalls = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testInitialization() = runTest {
        advanceUntilIdle()
        val value = editViewModel.uiState.value
        Assertions.assertThat(value.text).isEqualTo(todoItem.text)
        Assertions.assertThat(value.importance).isEqualTo(todoItem.importance)
        Assertions.assertThat(value.deadline).isEqualTo(todoItem.deadline)
        Assertions.assertThat(value.isDeadlineSet).isEqualTo(todoItem.deadline != null)
        Assertions.assertThat(value.isNewItem).isEqualTo(false)
    }

    @Test
    fun testOnUiActionSaveTask() = runTest {
        spyEditViewModel.onUiAction(EditUiAction.SaveTask)
        advanceUntilIdle()
        coVerify {
            spyEditViewModel["saveTodoItem"]()
        }
    }

    @Test
    fun testOnUiActionDeleteTask() = runTest {
        spyEditViewModel.onUiAction(EditUiAction.DeleteTask)
        advanceUntilIdle()
        coVerify {
            spyEditViewModel["removeTodoItem"]()
        }
    }

    @Test
    fun testOnUiActionNavigateUp() = runTest {
        editViewModel.onUiAction(EditUiAction.NavigateUp)
        advanceUntilIdle()
        coVerify {
            _uiEvent.send(EditUiEvent.NavigateUp)
        }
    }

    @Test
    fun testOnUiActionUpdateText() = runTest {
        editViewModel.onUiAction(EditUiAction.UpdateText(todoItem.text))
        advanceUntilIdle()

        Assertions.assertThat(editViewModel.uiState.value.text).isEqualTo(todoItem.text)
    }

    @Test
    fun testOnUiActionUpdateDeadlineSet() = runTest {
        editViewModel.onUiAction(EditUiAction.UpdateDeadlineSet(editViewModel.uiState.value.isDeadlineSet))
        advanceUntilIdle()

        Assertions.assertThat(editViewModel.uiState.value.isDeadlineSet).isEqualTo(true)
    }

    @Test
    fun testOnUiActionUpdateImportance() = runTest {
        editViewModel.onUiAction(EditUiAction.UpdateImportance(todoItem.importance))
        advanceUntilIdle()

        Assertions.assertThat(editViewModel.uiState.value.importance).isEqualTo(todoItem.importance)
    }

    @Test
    fun testOnUiActionUpdateDeadline() = runTest {
        editViewModel.onUiAction(EditUiAction.UpdateDeadline(todoItem.deadline!!))
        advanceUntilIdle()

        Assertions.assertThat(editViewModel.uiState.value.deadline).isEqualTo(todoItem.deadline)
    }

}