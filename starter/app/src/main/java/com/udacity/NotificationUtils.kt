package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
private const val FLAGS = 0

fun NotificationManager.cancelNotifications() {
    cancelAll()
}

fun NotificationManager.sendNotification(
    applicationContext: Context,
    channelId: String,
    messageBody: String,
    pendingIntent: PendingIntent
) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            channelId,
            "load app",
            IMPORTANCE_DEFAULT
        )
        createNotificationChannel(notificationChannel)
    }

    val builder = NotificationCompat.Builder(
        applicationContext,
        channelId
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setAutoCancel(true)
        .addAction(R.drawable.ic_assistant_black_24dp, "Check the status", pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())

}