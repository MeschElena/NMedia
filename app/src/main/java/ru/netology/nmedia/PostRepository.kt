package ru.netology.nmedia

import androidx.lifecycle.LiveData

interface PostRepository {
    val data: LiveData<List<Post>>
    fun getAll(): LiveData<List<Post>>
    fun like(id: Long)
    fun share(id: Long)
}