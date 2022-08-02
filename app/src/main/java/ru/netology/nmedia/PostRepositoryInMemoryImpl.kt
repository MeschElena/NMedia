package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = GENERATION_POST_AMOUNT.toLong()
    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            data.value = value
        }
    override val data = MutableLiveData(
        List(GENERATION_POST_AMOUNT) { index ->
            Post(
                id = index + 1L,
                author = "Нетология. Университет интернет-профессий будущего.",
                content = "Привет, это новая Нетология! $index",
                published = "21 мая в 18:36",
                countLike = 99L + 10 * index,
                countShare = 14L + 10 * index,
                countVisibility = 1200,
                video = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
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

    override fun delete(postId: Long) {
        posts = posts.filter {it.id != postId}
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        data.value = listOf(
            post.copy(id = ++nextId)
        ) + posts
    }

    private companion object {
        const val GENERATION_POST_AMOUNT = 100
    }
}