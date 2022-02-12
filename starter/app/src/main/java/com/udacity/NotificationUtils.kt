package com.udacity.utils


import android.content.Context
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.udacity.R


private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0



fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context){



    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )

        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

    notify(NOTIFICATION_ID, builder.build())
}