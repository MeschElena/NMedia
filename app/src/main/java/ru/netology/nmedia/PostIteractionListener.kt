package ru.netology.nmedia

interface PostIteractionListener {
    fun onLikeClicked(post: Post)
    fun onRemoveClicked(post: Post)
    fun onShareClicked(post: Post)
    fun onEditClicked(post: Post)
    fun onPlayVideo(post: Post)
}