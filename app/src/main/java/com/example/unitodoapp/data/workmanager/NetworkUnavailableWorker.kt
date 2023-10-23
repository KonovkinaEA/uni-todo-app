package com.example.unitodoapp.data.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.hilt.work.HiltWorker
import com.example.unitodoapp.data.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class NetworkUnavailableWorker @AssistedInject constructor(
    @Assisted private val repository: Repository,
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        repository.loadDataFromDB()
        return Result.success()
    }
}