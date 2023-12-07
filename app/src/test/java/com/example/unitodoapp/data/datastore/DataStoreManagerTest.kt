package com.example.unitodoapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.unitodoapp.MainCoroutineRule
import com.example.unitodoapp.data.api.model.User
import com.example.unitodoapp.ui.screens.settings.model.ThemeMode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.util.ReflectionHelpers

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DataStoreManagerTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val context: Context = RuntimeEnvironment.getApplication().applicationContext

    private val userPreferences = UserPreferences()

    private val preferencesDataStore = mockk<DataStore<UserPreferences>>(relaxed = true) {
        coEvery { updateData(allAny()) } returns userPreferences
    }

    private lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setUp() = runTest {
        dataStoreManager = DataStoreManager(context)
        ReflectionHelpers.setField(
            dataStoreManager,
            "preferencesDataStore",
            preferencesDataStore
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testSaveThemeMode() = runTest {
        val theme = ThemeMode.DARK
        dataStoreManager.saveThemeMode(theme)
        advanceUntilIdle()

        coVerify {
            preferencesDataStore.updateData(allAny())
        }
    }

    @Test
    fun testSaveNotificationsPermission() = runTest {
        dataStoreManager.saveNotificationsPermission(true)
        advanceUntilIdle()

        coVerify {
            preferencesDataStore.updateData(allAny())
        }
    }

    @Test
    fun testSaveFilterState() = runTest {
        dataStoreManager.saveFilterState(true)
        advanceUntilIdle()

        coVerify {
            preferencesDataStore.updateData(allAny())
        }
    }

    @Test
    fun testSaveUser() = runTest {
        dataStoreManager.saveUser(User("testUser", "123456"))
        advanceUntilIdle()

        coVerify {
            preferencesDataStore.updateData(allAny())
        }
    }


    @Test
    fun testLogOutUser() = runTest {
        dataStoreManager.logOutUser()
        advanceUntilIdle()

        coVerify {
            preferencesDataStore.updateData(allAny())
        }
    }

    @Test
    fun testSetUserStayLoggedTo() = runTest {
        dataStoreManager.setUserStayLoggedTo(true)
        advanceUntilIdle()

        coVerify {
            preferencesDataStore.updateData(allAny())
        }
    }
}