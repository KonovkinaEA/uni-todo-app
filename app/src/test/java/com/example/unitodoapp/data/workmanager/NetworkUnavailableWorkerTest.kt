package com.example.unitodoapp.data.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.unitodoapp.MainCoroutineRule
import com.example.unitodoapp.data.Repository
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After
import kotlinx.coroutines.test.runTest
import org.fest.assertions.api.Assertions
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkUnavailableWorkerTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository = mockk<Repository>(relaxed = true)
    private val appContext = mockk<Context>(relaxed = true)
    private val workerParams = mockk<WorkerParameters>(relaxed = true)
    private val networkUnavailableWorker =
        NetworkUnavailableWorker(repository, appContext, workerParams)

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testDoWork() = runTest {
        val result = networkUnavailableWorker.doWork()
        advanceUntilIdle()

        coVerify {
            repository.loadDataFromDB()
            ListenableWorker.Result.success()
        }
        Assertions.assertThat(result).isEqualTo(ListenableWorker.Result.success())
    }
}