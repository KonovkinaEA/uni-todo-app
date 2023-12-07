package com.example.unitodoapp.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.unitodoapp.utils.INTENT_ID_IMPORTANCE_KEY
import com.example.unitodoapp.utils.INTENT_ID_KEY
import com.example.unitodoapp.utils.INTENT_ID_TITLE_KEY
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.util.ReflectionHelpers


@RunWith(RobolectricTestRunner::class)
class TodoAlarmSchedulerTest {


    private val context: Context = RuntimeEnvironment.getApplication().applicationContext

    private val alarmManager = mockk<AlarmManager>(relaxed = true) {
        every { canScheduleExactAlarms() } returns true
    }
    private lateinit var alarmScheduler: TodoAlarmScheduler
    private lateinit var spyAlarmScheduler: TodoAlarmScheduler


    @Before
    fun setUp() {
        alarmScheduler = TodoAlarmScheduler(context)
        spyAlarmScheduler = spyk(alarmScheduler, recordPrivateCalls = true)
        ReflectionHelpers.setField(
            spyAlarmScheduler,
            "alarmManager",
            alarmManager
        )
    }

    @Test
    fun testSchedule() {
        spyAlarmScheduler.schedule(TIME, KEY, TITLE, IMPORTANCE)

        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent
                .putExtra(INTENT_ID_KEY, KEY)
                .putExtra(INTENT_ID_TITLE_KEY, TITLE)
                .putExtra(INTENT_ID_IMPORTANCE_KEY, IMPORTANCE)

            PendingIntent.getBroadcast(
                context,
                KEY,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        verify {
            spyAlarmScheduler["setAlarm"](TIME, KEY, TITLE, IMPORTANCE)
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                TIME,
                alarmIntent
            )
        }

    }

    companion object {
        private const val TIME = 10L
        private const val KEY = 1
        private const val TITLE = "testTitle"
        private const val IMPORTANCE = 1

    }

}


