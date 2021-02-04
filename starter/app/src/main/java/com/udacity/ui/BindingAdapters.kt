package com.udacity.ui

import androidx.databinding.BindingAdapter
import com.udacity.domain.ButtonState
import com.udacity.widget.LoadingButton

@BindingAdapter("statusIcon")
fun bindButtonState(button: LoadingButton, state: ButtonState) {
    button.state = state
}