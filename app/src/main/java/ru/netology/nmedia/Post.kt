package ru.netology.nmedia

import kotlinx.serialization.Serializable

@Serializable
data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val countLike: Long = 0L,
    val countShare:Long = 0L,
    val countVisibility: Long = 0L,
    val video: String? = null,
    val likedByMe: Boolean = false
)