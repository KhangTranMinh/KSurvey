package com.ktm.ksurvey.presentation.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ktm.ksurvey.R
import kotlin.random.Random

class NotificationHandler(private val context: Context) {

    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID_DEFAULT,
                CHANNEL_NAME_DEFAULT,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun showNotification(title: String, content: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID_DEFAULT)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setPriority(NotificationManager.IMPORTANCE_DEFAULT)
        }

        notificationManager.notify(Random.nextInt(), builder.build())
    }

    companion object {
        private const val CHANNEL_ID_DEFAULT = "KSurvey_channel_id_default"
        private const val CHANNEL_NAME_DEFAULT = "KSurvey_channel_name_default"
    }
}