package ru.netology.nmedia

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {

    override fun getAll() = db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUNMS_NAMES,
            null,
            null,
            null,
            null,
            "${PostsTable.Column.ID.colunmName} DESC"
        ).use { cursor ->
            List(cursor.count){
                cursor.moveToNext()
                cursor.toPost()
            }
        }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostsTable.Column.AUTHOR.colunmName, post.author)
            put(PostsTable.Column.CONTENT.colunmName, post.content)
            put(PostsTable.Column.PUBLISHED.colunmName, post.published)
        }
        val id = if (post.id != 0L) {
            db.update(
                PostsTable.NAME,
                values,
                "${PostsTable.Column.ID.colunmName} = ?",
                arrayOf(post.id.toString()),
            )
            post.id
        } else {
            db.insert(PostsTable.NAME, null, values)
        }
        return db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUNMS_NAMES,
            "${PostsTable.Column.ID.colunmName} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use { cursor ->
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
           UPDATE ${PostsTable.NAME} SET
               countLike = countLike + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostsTable.NAME,
            "${PostsTable.Column.ID.colunmName} = ?",
            arrayOf(id.toString())
        )
    }

    override fun share(id: Long) {
        db.execSQL(
            """
           UPDATE ${PostsTable.NAME} SET
               countShare = countShare + 1
           WHERE id = ?;
        """.trimIndent(),
            arrayOf(id)
        )
    }
}