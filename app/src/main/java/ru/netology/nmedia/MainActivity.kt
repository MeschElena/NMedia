package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего.",
            content = "Привет, это новая Нетология!",
            published = "21 мая в 18:36",
            countLike = 999,
            countShare = 1495,
            countVisibility = 1200,
            likedByMe = false
        )

        with(binding){
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeText.text = countToString(post.countLike)
            shareText.text = countToString(post.countShare)
            visibilityText.text = countToString(post.countVisibility)

            likeImage?.setOnClickListener {
                if (!post.likedByMe) {
                    likeImage?.setImageResource(R.drawable.ic_liked_48)
                    post.countLike = post.countLike + 1
                    likeText.text = countToString(post.countLike)
                    post.likedByMe = true
                 } else {
                    post.likedByMe = !post.likedByMe
                    post.countLike = post.countLike - 1
                    likeImage.setImageResource(R.drawable.ic_favorite_48)
                    likeText.text = countToString(post.countLike)
                    post.likedByMe = false
                }
            }
            shareImage?.setOnClickListener {
                post.countShare = post.countShare + 1
                shareText.text = countToString(post.countShare)
            }
        }
    }

    fun countToString (count: Long): String {
        if (count / 100000 > 0) {
            if ((count - (count / 100000) * 100000) / 10000 >0) {
                return (count / 100000).toString() + "." + ((count - (count / 100000) * 100000) / 10000).toString() + " M"
            } else return (count / 1000).toString() + " M"
        } else if (count / 1000 >= 10) {
            return (count / 1000).toString() + " K"
        } else if (count / 1000 > 0) {
            if ((count - (count / 1000) * 1000) / 100 >0) {
                return (count / 1000).toString() + "." + ((count - (count / 1000) * 1000) / 100).toString() + " K"
            } else return (count / 1000).toString() + " K"
        } else return count.toString()
    }
}


