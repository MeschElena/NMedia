package ru.netology.nmedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostIteractionListener {

    private val repository: PostRepository = SQLiteRepository(
        dao = AppDb.getInstance(
            context = application
        ).postDao
    )
    val data get() = repository.data

    val currentPost = MutableLiveData<Post?>(null)
    val shareEvent = SingleLiveEvent<Post>()
    val playEvent = SingleLiveEvent<Post>()
    val navigateToPostContentScreenEven = SingleLiveEvent<String>()
    val navigateToPostContentViewEven = SingleLiveEvent<String>()
    val navigateToPostContentScreenView = SingleLiveEvent<Long>()

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
        navigateToPostContentScreenEven.value = post.content
        currentPost.value = post
    }

    override fun onPlayVideo(post: Post) {
        playEvent.value = post
    }

    fun onAddClicked(){
        navigateToPostContentScreenEven.call()
    }

    override fun onPostClicked(post: Post) {
        navigateToPostContentScreenView.value = post.id
    }

    fun getPost(postId:Long) = checkNotNull(repository.getAll().value).filter {it.id == postId}[0]
}