package com.uyghar.kitabhumar.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    val id: Int,
    val name: String?,
    val surname: String?,
    val nickname: String?,
    val email: String?,
    val image: String?,
): Parcelable