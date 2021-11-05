package com.uyghar.kitabhumar.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    val id: Int?,
    val title: String?,
    val image: String?,
    val favorite: String?,
    val cat: Int?,
    val ret: Int?,
    val author: Author?
):Parcelable