package com.uyghar.kitabhumar.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PostImage(
    val id: Int,
    val images: String?,
    val post: Int?
): Parcelable