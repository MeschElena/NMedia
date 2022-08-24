package ru.netology.nmedia

import android.database.Cursor

fun Cursor.toPost() = Post(
    id = getLong(getColumnIndexOrThrow(PostsTable.Column.ID.colunmName)),
    author = getString(getColumnIndexOrThrow(PostsTable.Column.AUTHOR.colunmName)),
    content = getString(getColumnIndexOrThrow(PostsTable.Column.CONTENT.colunmName)),
    published = getString(getColumnIndexOrThrow(PostsTable.Column.PUBLISHED.colunmName)),
    video = getString(getColumnIndexOrThrow(PostsTable.Column.VIDEO.colunmName)),
    countLike = getLong(getColumnIndexOrThrow(PostsTable.Column.COUNT_LIKE.colunmName)),
    countShare = getLong(getColumnIndexOrThrow(PostsTable.Column.COUNT_SHARE.colunmName)),
    countVisibility = getLong(getColumnIndexOrThrow(PostsTable.Column.COUNT_VISIBILITY.colunmName)),
    likedByMe = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKED_BY_ME.colunmName)) != 0
)