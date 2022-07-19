package ru.netology.nmedia

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val countLike: Long = 0L,
    val countShare:Long = 0L,
    val countVisibility: Long = 0L,
    val likedByMe: Boolean = false
)