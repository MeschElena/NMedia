package ru.netology.nmedia

import androidx.lifecycle.LiveData

interface PostRepository {
    val data: LiveData<List<Post>>
    fun getAll(): LiveData<List<Post>>
    fun like(id: Long)
    fun share(id: Long)
    fun delete(id: Long)
    fun save(post: Post)

    companion object {
        const val NEW_POST_ID = 0L
    }
}