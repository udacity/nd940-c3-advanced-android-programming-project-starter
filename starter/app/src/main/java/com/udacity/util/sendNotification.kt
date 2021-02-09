package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.udacity.R

private const val NOTIFICATION_ID = 0

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(
        applicationContext: Context,
        pendingIntent: PendingIntent
) {
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.loadapp_channel_id)
    )
    builder.setSmallIcon(R.drawable.ic_cloud_download_white_18dp)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(applicationContext.getString(R.string.notification_description))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(
                    R.drawable.ic_cloud_download_white_18dp,
                    applicationContext.getString(R.string.notification_button),
                    pendingIntent
            )
            .priority = NotificationCompat.PRIORITY_DEFAULT

    notify(NOTIFICATION_ID, builder.build())
}