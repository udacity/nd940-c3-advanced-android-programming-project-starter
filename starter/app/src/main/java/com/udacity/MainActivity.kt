package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var radioOption: RadioOption? = null

    private val notificationManager: NotificationManager? by lazy {
        ContextCompat.getSystemService(
            applicationContext, NotificationManager::class.java
        )
    }
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio1 -> {
                    radioOption = RadioOption.IMAGE
                }
                R.id.radio2 -> {
                    radioOption = RadioOption.LOAD_APP
                }
                R.id.radio3 -> {
                    radioOption = RadioOption.RETROFIT
                }
            }
        }
        custom_button.setOnClickListener {
            radioOption?.let {
                download(it.url)
                notificationManager?.cancelAll()
            } ?: showToast()

        }
    }

    private fun showToast() {
        Toast.makeText(
            this,
            getString(R.string.label_please_select_the_file_to_download),
            Toast.LENGTH_SHORT
        ).show()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val downloadStatus = if (id == downloadID) {
                DownloadStatus.SUCCESS
            } else {
                DownloadStatus.FAILED
            }
            radioOption?.let { selectedOption ->

                notificationManager?.sendNotification(
                    context,
                    CHANNEL_ID,
                    context.getString(R.string.notification_description),
                    createPendingIntent(selectedOption, downloadStatus)
                )
            }
        }
    }

    private fun createPendingIntent(
        selectedRadioOption: RadioOption,
        downloadStatus: DownloadStatus
    ): PendingIntent {
        val contentIntent = Intent(applicationContext, DetailActivity::class.java).apply {
            putExtra(
                EXTRA_KEY_DESCRIPTION,
                applicationContext.getString(selectedRadioOption.description)
            )
            putExtra(EXTRA_KEY_DOWNLOAD_STATUS, downloadStatus)
        }
        return PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"

        private const val CHANNEL_ID = "channelId"
        const val EXTRA_KEY_DESCRIPTION = "EXTRA_KEY_DESCRIPTION"
        const val EXTRA_KEY_DOWNLOAD_STATUS = "EXTRA_KEY_DOWNLOAD_STATUS"
    }

}
