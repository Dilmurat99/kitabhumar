package com.uyghar.kitabhumar.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PostCategory(
    val id: Int,
    val title: String?
): Parcelable