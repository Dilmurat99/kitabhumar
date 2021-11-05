package com.uyghar.kitabhumar.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val id: Int?,
    val content: String?,
    val updated_at: String?,
    val user: User?,
    val book: Book?,
    val post_category: PostCategory?,
    val post_images: List<PostImage>?
):Parcelable