package com.example.unitodoapp.data.workmanager

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomWorkManager @Inject constructor (
    private val workManager: WorkManager
) {

    fun setWorkers() {
        loadDataWork()
    }

    private fun loadDataWork() {
        loadDataFromDB()
    }


    private fun loadDataFromDB() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val request = OneTimeWorkRequest.Builder(NetworkUnavailableWorker::class.java)
            .setConstraints(constraints)
            .build()

        workManager
            .enqueueUniqueWork(
                "loadDBWorker",
                ExistingWorkPolicy.KEEP,
                request
            )
    }
}