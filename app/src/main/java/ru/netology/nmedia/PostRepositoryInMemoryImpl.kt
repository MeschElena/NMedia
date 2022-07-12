package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemoryImpl : PostRepository {
    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            data.value = value
        }
    override val data = MutableLiveData(
        List(100) { index ->
            Post(
                id = index + 1L,
                author = "Нетология. Университет интернет-профессий будущего.",
                content = "Привет, это новая Нетология! $index + 1",
                published = "21 мая в 18:36",
                countLike = 99L + 10 * index,
                countShare = 14L + 10 * index,
                countVisibility = 1200,
                likedByMe = false
            )
        }
    )

    override fun getAll(): LiveData<List<Post>> = data

    override fun like(postId: Long) {
        posts = posts.map {post ->
            if (post.id == postId) post.copy(likedByMe = !post.likedByMe, countLike = post.countLike + if (post.likedByMe) -1 else 1)
            else post
        }
    }

    override fun share(postId: Long) {
        posts = posts.map {post -> post.copy(countShare = post.countShare + 1)}
    }
}