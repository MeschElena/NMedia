package ru.netology.nmedia

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var countLike: Long,
    var countShare:Long,
    var countVisibility: Long,
    var likedByMe: Boolean = false
)