package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SQLiteRepository (
    private val dao: PostDao
) : PostRepository {

    private var posts
        get() = checkNotNull(data.value)
        set(value) {
            data.value = value
        }
    override val data = MutableLiveData(dao.getAll())
    override fun getAll(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }

    override fun like(id: Long) {
        dao.likeById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                countLike = if (it.likedByMe) it.countLike - 1 else it.countLike + 1
            )
        }
        data.value = posts
    }

    override fun share(id: Long) {
        dao.share(id)
        posts = posts.map {
            if (it.id == id)  it.copy(countShare = it.countShare + 1)
            else it}
        data.value = posts
    }

    override fun delete(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }
}