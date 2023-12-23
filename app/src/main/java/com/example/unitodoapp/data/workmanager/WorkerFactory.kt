package com.example.unitodoapp.data.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.unitodoapp.data.Repository
import javax.inject.Inject

class WorkerFactory @Inject constructor(
    private val repository: Repository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return when (workerClassName) {
            NetworkUnavailableWorker::class.java.name -> {
                NetworkUnavailableWorker(repository, appContext, workerParameters)
            }
            else -> {
                NetworkAvailableWorker(repository, appContext, workerParameters)
            }
        }
    }
}