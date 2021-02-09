package com.udacity.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DownloadState(
    val filename: String,
    val status: String
): Parcelable
