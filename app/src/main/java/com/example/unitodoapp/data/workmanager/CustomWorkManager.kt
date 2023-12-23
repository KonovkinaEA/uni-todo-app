package com.example.unitodoapp.data.workmanager

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.unitodoapp.data.api.model.User
import com.example.unitodoapp.utils.REPEAT_TOKEN_INTERVAL
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomWorkManager @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val workManager: WorkManager
) {

    fun setWorkers(wasLogged: Boolean, user: User) {
        monitorNetworkConnection(user)
        updateDataAndToken(user)
        if (!wasLogged) loadDataFromServer(user)
        else loadDataWork(user)
    }

    private fun loadDataWork(user: User) {
        if (!isNetworkAvailable()) {
            loadDataFromDB()
        } else {
            loadDataFromServer(user)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun updateDataAndToken(user: User) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putString("email", user.email)
            .putString("password", user.password)
            .build()

        val request = PeriodicWorkRequest.Builder(
            NetworkAvailableWorker::class.java,
            REPEAT_TOKEN_INTERVAL,
            TimeUnit.MINUTES
        )
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        workManager
            .enqueueUniquePeriodicWork(
                "updateToken",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }

    private fun loadDataFromServer(user: User) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putString("email", user.email)
            .putString("password", user.password)
            .build()

        val request = OneTimeWorkRequest.Builder(NetworkAvailableWorker::class.java)
            .setInputData(inputData)
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

    private fun monitorNetworkConnection(user: User) {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(request, getNetworkCallback(user))
    }

    private fun getNetworkCallback(user: User) =
        object : ConnectivityManager.NetworkCallback() {

            private val availableNetworks: MutableSet<Network> = HashSet()

            override fun onAvailable(network: Network) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                val hasInternetCapability =
                    networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

                if (hasInternetCapability == true) {
                    availableNetworks.add(network)
                    sendNetworkState()
                }
            }

            override fun onLost(network: Network) {
                availableNetworks.remove(network)
            }

            private fun sendNetworkState() {
                if (availableNetworks.isNotEmpty()) {
                    loadDataFromServer(user)
                }
            }
        }
}
