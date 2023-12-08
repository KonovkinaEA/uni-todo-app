package com.example.unitodoapp.data.workmanager

import android.content.Context
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.unitodoapp.MainCoroutineRule
import com.example.unitodoapp.data.Repository
import com.example.unitodoapp.data.api.model.User
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.fest.assertions.api.Assertions
import org.junit.After
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkAvailableWorkerTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository = mockk<Repository>(relaxed = true)
    private val appContext = mockk<Context>(relaxed = true)
    private val inputDataForTest = Data.Builder()
        .putString("email", "hello@mail.ru")
        .putString("password", "12345678")
        .build()

    private val workerParams = mockk<WorkerParameters>(relaxed = true) {
        every { inputData } returns inputDataForTest
    }

    private val networkAvailableWorker =
        NetworkAvailableWorker(repository, appContext, workerParams)

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testDoWork() = runTest {
        val result = networkAvailableWorker.doWork()
        advanceUntilIdle()

        coVerify {
            repository.logInUser(User("hello@mail.ru", "12345678"))
            repository.loadDataFromServer()
        }
        Assertions.assertThat(result).isEqualTo(ListenableWorker.Result.success())
    }
}