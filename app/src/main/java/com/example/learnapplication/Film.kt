package com.example.learnapplication

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Film(
    val title: String,
    val poster: Int,
    val description: String,
    var favorite: Boolean = true,
    var watchLater: Boolean = true
) : Parcelable
