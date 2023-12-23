package com.example.unitodoapp.notifications

import android.content.Context
import android.content.Intent
import com.example.unitodoapp.R
import com.example.unitodoapp.data.model.Importance
import com.example.unitodoapp.utils.INTENT_ID_IMPORTANCE_KEY
import com.example.unitodoapp.utils.INTENT_ID_KEY
import com.example.unitodoapp.utils.INTENT_ID_TITLE_KEY
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
class AlarmReceiverTest {

    private val context: Context = RuntimeEnvironment.getApplication().applicationContext
    private val intent = mockk<Intent>(relaxed = true) {
        every { getIntExtra(INTENT_ID_KEY, 0) } returns IMPORTANCE_ID
        every { getStringExtra(INTENT_ID_TITLE_KEY) } returns IMPORTANCE_TITLE
        every { getIntExtra(INTENT_ID_IMPORTANCE_KEY, 0) } returns IMPORTANCE_TYPE
    }

    private val notificationService = mockk<DeadlineNotificationService>(relaxed = true) {
        every { showNotification(any(), any(), any()) } just runs
    }
    private val alarmReceiver = AlarmReceiver()


    @Before
    fun setUp() {
        ReflectionHelpers.setField(alarmReceiver, "notificationService", notificationService)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testOnReceive() {
        notificationService.showNotification(
            IMPORTANCE_ID,
            IMPORTANCE_TITLE,
            context.getString(
                IMPORTANCE_TYPE_TO_STR
            ) + context.getString(IMPORTANCE_TYPE)
        )
        alarmReceiver.onReceive(context, intent)
        verify {
            notificationService.showNotification(
                IMPORTANCE_ID,
                IMPORTANCE_TITLE,
                context.getString(
                    IMPORTANCE_TYPE_TO_STR
                ) + context.getString(IMPORTANCE_TYPE)
            )
        }
    }

    companion object {
        private const val IMPORTANCE_ID = 1
        private const val IMPORTANCE_TITLE = "testTitle"
        private val IMPORTANCE_TYPE = Importance.BASIC.toStringResource()
        private val IMPORTANCE_TYPE_TO_STR = R.string.importance_for_notif
    }
}