package ru.netology.nmedia

object PostsTable {
    const val NAME = "posts"

    val DDL = """
        CREATE TABLE $NAME (
            ${Column.ID.colunmName} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Column.AUTHOR.colunmName} TEXT NOT NULL,
            ${Column.CONTENT.colunmName} TEXT NOT NULL,
            ${Column.PUBLISHED.colunmName} TEXT NOT NULL,
            ${Column.LIKED_BY_ME.colunmName} BOOLEAN NOT NULL DEFAULT 0,
            ${Column.COUNT_LIKE.colunmName} INTEGER NOT NULL DEFAULT 0,
            ${Column.COUNT_SHARE.colunmName} INTEGER NOT NULL DEFAULT 0,
            ${Column.COUNT_VISIBILITY.colunmName} INTEGER NOT NULL DEFAULT 0,
            ${Column.VIDEO.colunmName} TEXT NOT NULL DEFAULT ""
        );
        """.trimIndent()

    val ALL_COLUNMS_NAMES = Column.values().map {it.colunmName}.toTypedArray()

    enum class Column(val colunmName: String){
        ID("id"),
        AUTHOR("author"),
        CONTENT("content"),
        PUBLISHED("published"),
        COUNT_LIKE("countLike"),
        COUNT_SHARE("countShare"),
        COUNT_VISIBILITY("countVisibility"),
        VIDEO("video"),
        LIKED_BY_ME("likedByMe")
    }

}