package ru.netology.nmedia

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel(), PostIteractionListener {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data get() = repository.data

    val currentPost = MutableLiveData<Post?>(null)

    val shareEvent = SingleLiveEvent<Post>()
    val playEvent = SingleLiveEvent<Post>()

    fun onSaveButtonClicked(content: String) {
        if (content.isBlank()) return
        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published =  "Today"
        )
        repository.save(post)
        currentPost.value = null
    }

    override fun onLikeClicked(post: Post) = repository.like(post.id)

    override fun onRemoveClicked(post: Post) = repository.delete(post.id)

    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        shareEvent.value = post
    }
    override fun onEditClicked(post: Post) {
        currentPost.value = post
    }

    override fun onPlayVideo(post: Post) {
        playEvent.value = post
    }
    fun onCreatNewPost(newPostContent: String) {
        if (newPostContent.isBlank()) return
        val post = currentPost.value?.copy(
            content = newPostContent
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = newPostContent,
            published =  "Today"
        )
        repository.save(post)
        currentPost.value = null
    }

}