package ru.netology.nmedia

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.Appendable
import kotlin.properties.Delegates

class FilePostRepository(
    private val application: Application) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

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
            application.openFileOutput(
                FILE_NAME, Context.MODE_PRIVATE
            ).bufferedWriter().use {
                it.write(gson.toJson(value))
            }
        }
    override val data: MutableLiveData<List<Post>>


    init {
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postsFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use {
                gson.fromJson(it, type)
            }
        } else emptyList()
        data = MutableLiveData(posts)
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun like(postId: Long) {
        data.value = posts.map {post ->
            if (post.id == postId) post.copy(likedByMe = !post.likedByMe, countLike = post.countLike + if (post.likedByMe) -1 else 1)
            else post
        }
        sync()
    }

    override fun share(postId: Long) {
        data.value = posts.map {post -> post.copy(countShare = post.countShare + 1)}
        sync()
    }

    override fun delete(postId: Long) {
        data.value = posts.filter {it.id != postId}
        sync()
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
        sync()
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
        sync()
    }

    private fun insert(post: Post) {
        data.value = listOf(
            post.copy(id = ++nextId)
        ) + posts
        sync()
    }

    private fun sync() {
        application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    private companion object {
        const val POSTS_PREFS_KEY = "posts"
        const val NEXT_ID_PREFS_KEY = "nextNewId"
        const val FILE_NAME = "posts.json"
    }
}
