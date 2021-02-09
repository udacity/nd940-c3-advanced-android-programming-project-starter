package com.udacity.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.udacity.R
import com.udacity.domain.ButtonState
import com.udacity.domain.DownloadState
import com.udacity.widget.LoadingButton

@BindingAdapter("statusIcon")
fun bindButtonState(button: LoadingButton, state: ButtonState) {
    button.state = state
}

@BindingAdapter("downloadFilename")
fun bindDownloadFilename(view: TextView, state: DownloadState) {
    view.text = state.filename
}

@BindingAdapter("downloadStatus")
fun bindDownloadStatus(view: TextView, state: DownloadState) {
    val success = view.resources.getString(R.string.download_status_success)
    val failure = view.resources.getString(R.string.download_status_fail)
    when (state.status) {
        success -> {
            view.text = success
            view.contentDescription = success
            view.setTextAppearance(R.style.TextAppearance_LoadApp_Content)
        }
        failure -> {
            view.text = failure
            view.contentDescription = failure
            view.setTextAppearance(R.style.TextAppearance_LoadApp_Failure)
        }
    }
}