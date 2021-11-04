package com.uyghar.kitabhumar.models

data class Post(
    val id: Int?,
    val content: String?,
    val updated_at: String?,
    val user: User?,
    val book: Book?,
    val post_category: PostCategory?,
    val post_images: List<PostImage>?
)