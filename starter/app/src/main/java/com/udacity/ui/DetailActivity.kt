package com.udacity.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udacity.R
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.domain.DownloadState

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val state = intent.getParcelableExtra<DownloadState>(STATE)
        state?.let { binding.detailContent.state = state }
        binding.detailContent.okButton.setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private const val STATE = "state"
        fun newIntent(context: Context, state: DownloadState): Intent {
            return Intent(context, DetailActivity::class.java)
                    .putExtra(STATE, state)
        }
    }

}
