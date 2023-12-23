package com.example.unitodoapp.data.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.unitodoapp.data.Repository
import com.example.unitodoapp.data.api.model.User
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NetworkAvailableWorker @AssistedInject constructor(
    @Assisted private val repository: Repository,
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val email = inputData.getString("email")
        val password = inputData.getString("password")
        repository.logInUser(User(email = email!!, password = password!!))
        repository.loadDataFromServer()
        return Result.success()
    }
}