package com.example.unitodoapp.data.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.unitodoapp.data.Repository
import io.mockk.mockk
import io.mockk.unmockkAll
import org.fest.assertions.api.Assertions
import org.junit.After
import org.junit.Test

class WorkerFactoryTest {
    private val repository = mockk<Repository>(relaxed = true)
    private val appContext = mockk<Context>(relaxed = true)
    private val workerParams = mockk<WorkerParameters>(relaxed = true)
    private val workerFactory = WorkerFactory(repository)

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testCreateWorker() {
        var worker = workerFactory.createWorker(
            appContext,
            NetworkUnavailableWorker::class.java.name,
            workerParams
        )
        var assertWorker: ListenableWorker = NetworkUnavailableWorker(repository, appContext, workerParams)
        Assertions.assertThat(worker).isEqualsToByComparingFields(assertWorker)
        worker = workerFactory.createWorker(
            appContext,
            NetworkAvailableWorker::class.java.name,
            workerParams
        )
        assertWorker = NetworkAvailableWorker(repository, appContext,  workerParams)
        Assertions.assertThat(worker).isEqualsToByComparingFields(assertWorker)
    }
}