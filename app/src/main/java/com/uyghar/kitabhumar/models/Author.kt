package com.uyghar.kitabhumar.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Author(
    val id: Int?,
    val name: String?,
    val image: String?,
    val surname: String?,
    val gender: String?,
): Parcelable