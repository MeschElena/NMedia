package ru.netology.nmedia

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.Appendable
import kotlin.properties.Delegates

class SharedPrefsPostRepository(application: Application) : PostRepository {

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )
    private var nextId: Long by Delegates.observable(
        prefs.getLong(NEXT_ID_PREFS_KEY, 0L)
    ) {_, _, newValue ->
        prefs.edit { putLong(NEXT_ID_PREFS_KEY, newValue) }
    }


    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            prefs.edit {
                val serializedPost = Json.encodeToString(value)
                putString(POSTS_PREFS_KEY, serializedPost)
            }
            data.value = value
        }
    override val data: MutableLiveData<List<Post>>


    init {
        val serializedPosts = prefs.getString(POSTS_PREFS_KEY,null)
        val posts: List<Post> = if (serializedPosts != null) {
             Json.decodeFromString(serializedPosts)
        } else emptyList()
        data = MutableLiveData(posts)
    }

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
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        posts = listOf(
            post.copy(id = ++nextId)
        ) + posts
    }

    private companion object {
        const val POSTS_PREFS_KEY = "posts"
        const val NEXT_ID_PREFS_KEY = "nextId"
    }
}
