package com.example.unitodoapp.data.workmanager

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.unitodoapp.utils.REPEAT_LOAD_INTERVAL
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomWorkManager @Inject constructor (
    private val connectivityManager: ConnectivityManager,
    private val workManager: WorkManager
) {

    fun setWorkers() {
        refreshPeriodicWork()
        loadDataWork()
    }

    fun reloadData() {
        loadDataWork()
    }

    private fun loadDataWork() {
        loadDataFromServer()
        if (!isNetworkAvailable()) {
            loadDataFromDB()
        }
    }

    fun isNetworkAvailable(): Boolean {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun refreshPeriodicWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequest.Builder(DataUpdatesWorker::class.java, REPEAT_LOAD_INTERVAL, TimeUnit.HOURS) //wtf
            .setConstraints(constraints)
            .build()

        workManager
            .enqueueUniquePeriodicWork(
                "refreshWorker",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }

    private fun loadDataFromServer() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequest.Builder(NetworkAvailableWorker::class.java)
            .setConstraints(constraints)
            .build()

        workManager
            .enqueueUniqueWork(
                "loadServerWorker",
                ExistingWorkPolicy.KEEP,
                request
            )
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