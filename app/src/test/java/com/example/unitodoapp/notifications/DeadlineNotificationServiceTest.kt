package com.example.unitodoapp.notifications

import android.app.NotificationManager
import android.content.Context
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.util.ReflectionHelpers


@RunWith(RobolectricTestRunner::class)
class DeadlineNotificationServiceTest {


    private val notificationManager = mockk<NotificationManager>(relaxed = true) {
        every { notify(any(), any()) } just runs
    }
    private val context: Context = RuntimeEnvironment.getApplication().applicationContext

    private lateinit var deadlineNotificationService: DeadlineNotificationService


    @Before
    fun setUp() {
        deadlineNotificationService = DeadlineNotificationService(context)
        ReflectionHelpers.setField(
            deadlineNotificationService,
            "notificationManager",
            notificationManager
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testShowNotification() {
        deadlineNotificationService.showNotification(
            IMPORTANCE_ID,
            IMPORTANCE_TITLE,
            IMPORTANCE_TYPE_TO_STR
        )

        verify {
            notificationManager.notify(
                IMPORTANCE_ID,
                any()
            )
        }
    }


    companion object {
        private const val IMPORTANCE_ID = 1
        private const val IMPORTANCE_TITLE = "testTitle"
        private const val IMPORTANCE_TYPE_TO_STR = "testImportance"
    }
}