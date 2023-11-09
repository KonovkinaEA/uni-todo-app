package com.example.unitodoapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.unitodoapp.R
import com.example.unitodoapp.utils.INTENT_ID_IMPORTANCE_KEY
import com.example.unitodoapp.utils.INTENT_ID_KEY
import com.example.unitodoapp.utils.INTENT_ID_TITLE_KEY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationService: DeadlineNotificationService

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra(INTENT_ID_KEY, 0)
        val title = intent.getStringExtra(INTENT_ID_TITLE_KEY).toString()
        val importance = context.getString(R.string.importance_for_notif) +
                context.getString(intent.getIntExtra(INTENT_ID_IMPORTANCE_KEY, 0))
        notificationService.showNotification(id, title, importance)
    }
}