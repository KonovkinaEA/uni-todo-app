package com.example.unitodoapp.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.unitodoapp.R
import javax.inject.Inject

class DeadlineNotificationService @Inject constructor(
    private val context: Context
) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(id: Int, title: String, importance: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.deadline_notification)
            .setContentTitle(title)
            .setContentText(importance)
            .build()

        notificationManager.notify(id, notification)
    }

    companion object {
        const val CHANNEL_ID = "channel_id"
        const val CHANNEL_NAME = "Deadline"
    }
}