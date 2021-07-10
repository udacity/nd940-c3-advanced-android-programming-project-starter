package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.MainActivity.Companion.EXTRA_KEY_DESCRIPTION
import com.udacity.MainActivity.Companion.EXTRA_KEY_DOWNLOAD_STATUS
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        setupExtra()
        setupListeners()
    }

    private fun setupListeners() {
        btnOk.setOnClickListener { finish() }
    }

    private fun setupExtra() {
        intent.extras?.getString(EXTRA_KEY_DESCRIPTION)?.let { tvFileValue.text = it }
        intent.extras?.get(EXTRA_KEY_DOWNLOAD_STATUS)?.let { status ->
            when (status as DownloadStatus) {
                DownloadStatus.SUCCESS -> {
                    tvStatusValue.text = getString(R.string.label_success)
                    tvStatusValue.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorPrimaryDark
                        )
                    )
                }
                DownloadStatus.FAILED -> {
                    tvStatusValue.text = getString(R.string.label_fail)
                    tvStatusValue.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorAccent
                        )
                    )
                }
            }
        }

    }

}
