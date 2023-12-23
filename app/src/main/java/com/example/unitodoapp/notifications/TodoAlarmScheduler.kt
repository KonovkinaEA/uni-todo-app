package com.example.unitodoapp.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.example.unitodoapp.utils.INTENT_ID_IMPORTANCE_KEY
import com.example.unitodoapp.utils.INTENT_ID_KEY
import com.example.unitodoapp.utils.INTENT_ID_TITLE_KEY
import javax.inject.Inject

class TodoAlarmScheduler @Inject constructor(
    private val context: Context
) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(time: Long, key: Int, title: String, importance: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            when {
                alarmManager.canScheduleExactAlarms() -> {
                    setAlarm(time, key, title, importance)
                }

                else -> {
                    // Ask users to go to exact alarm page in system settings.
                    ContextCompat.startActivity(
                        context,
                        Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM),
                        null
                    )
                }
            }
        } else {
            //setAlarm(time, key, title, importance)
        }

    }

    private fun setAlarm(time: Long, key: Int, title: String, importance: Int) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent
                .putExtra(INTENT_ID_KEY, key)
                .putExtra(INTENT_ID_TITLE_KEY, title)
                .putExtra(INTENT_ID_IMPORTANCE_KEY, importance)

            PendingIntent.getBroadcast(
                context,
                key,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            time,
            alarmIntent
        )
    }
}