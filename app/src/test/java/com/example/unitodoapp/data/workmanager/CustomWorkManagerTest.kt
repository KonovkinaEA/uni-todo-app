package com.example.unitodoapp.data.workmanager

import android.net.ConnectivityManager
import androidx.work.WorkManager
import com.example.unitodoapp.data.api.model.User
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Test


class CustomWorkManagerTest {

    private val connectivityManager = mockk<ConnectivityManager>(relaxed = true)
    private val workManager = mockk<WorkManager>(relaxed = true)

    private val customWorkManager = CustomWorkManager(connectivityManager, workManager)
    private val spyCustomWorkManager = spyk(customWorkManager, recordPrivateCalls = true)

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testSetWorkers() {
        val testUser = mockk<User>(relaxed = true) {
            every { email } returns "email@mail.ru"
            every { password } returns "12345678"
        }
        spyCustomWorkManager.setWorkers(false, testUser)

        coVerify {
            spyCustomWorkManager["monitorNetworkConnection"](testUser)
            spyCustomWorkManager["updateDataAndToken"](testUser)
            spyCustomWorkManager["loadDataFromServer"](testUser)
        }

        spyCustomWorkManager.setWorkers(true, testUser)
        coVerify {
            spyCustomWorkManager["monitorNetworkConnection"](testUser)
            spyCustomWorkManager["updateDataAndToken"](testUser)
            spyCustomWorkManager["loadDataWork"](testUser)
        }
    }
}