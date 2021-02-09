package com.udacity.ui

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.udacity.R
import com.udacity.domain.ButtonState
import com.udacity.domain.DownloadState
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var selectedUrl: String? = null
    private var selectedFilename: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createChannel(
                getString(R.string.loadapp_channel_id),
                getString(R.string.download_channel_id)
        )
        download_options_group.setOnCheckedChangeListener { _, i -> onOptionSelected(i) }
        custom_button.setOnClickListener { onButtonClicked() }
    }

    private fun onButtonClicked() {
        if (hasSelectedRepo())
            download()
        else
            showUnselectedMessage()
    }

    private fun onOptionSelected(optionRedId: Int) {
        when (optionRedId) {
            R.id.glide_button -> {
                selectedUrl = GLIDE_URL
                selectedFilename = glide_button.text.toString()
            }
            R.id.retrofit_button -> {
                selectedUrl = RETROFIT_URL
                selectedFilename = retrofit_button.text.toString()
            }
            R.id.starter_project_button -> {
                selectedUrl = STARTER_PROJECT_URL
                selectedFilename = starter_project_button.text.toString()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun hasSelectedRepo() = selectedUrl != null

    private fun showUnselectedMessage() {
        Toast.makeText(this, R.string.unselected_repository, Toast.LENGTH_SHORT).show()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID)
                onDownloadCompleted()
        }
    }

    private fun onDownloadCompleted() {
        custom_button.state = ButtonState.Completed
        val query = DownloadManager.Query().setFilterById(downloadID)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val status = cursor.getInt(
                    cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            )
            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    val success = getString(R.string.download_status_success)
                    showNotification(status = success)
                }
                DownloadManager.STATUS_FAILED -> {
                    val fail = getString(R.string.download_status_fail)
                    showNotification(status = fail)
                }
            }
        }
    }

    private fun showNotification(status: String) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Create the content intent for the notification, which launches
        // this activity
        val contentIntent = DetailActivity.newIntent(
                context = applicationContext,
                state = DownloadState(filename = selectedFilename.orEmpty(), status = status)
        )
        val contentPendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        manager.sendNotification(applicationContext, contentPendingIntent)
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(selectedUrl))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        custom_button.state = ButtonState.Loading
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setShowBadge(true)
                enableLights(true)
                lightColor = Color.RED
                enableVibration(false)
                description = channelName
            }


            val notificationManager = getSystemService<NotificationManager>()
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val STARTER_PROJECT_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL = "https://github.com/bumptech/glide/archive/master.zip"
        private const val RETROFIT_URL = "https://github.com/square/retrofit/archive/master.zip"
    }

}
