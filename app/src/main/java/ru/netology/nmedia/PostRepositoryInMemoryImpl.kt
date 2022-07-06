package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего.",
        content = "Привет, это новая Нетология!",
        published = "21 мая в 18:36",
        countLike = 999,
        countShare = 1495,
        countVisibility = 1200,
        likedByMe = false
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data
    override fun like() {
        val typeLike = if (post.likedByMe) -1 else 1
        post = post.copy(likedByMe = !post.likedByMe, countLike = post.countLike + typeLike)
        data.value = post
    }

    override fun share() {
        post = post.copy(countShare = post.countShare + 1)
        data.value = post
    }
}