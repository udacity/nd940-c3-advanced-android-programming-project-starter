package com.udacity

import androidx.annotation.StringRes

enum class RadioOption(val url: String, @StringRes val description: Int) {
    IMAGE("https://github.com/bumptech/glide", R.string.label_radio_1),
    LOAD_APP(
        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter",
        R.string.label_radio_2
    ),
    RETROFIT("https://github.com/square/retrofit", R.string.label_radio_3)
}