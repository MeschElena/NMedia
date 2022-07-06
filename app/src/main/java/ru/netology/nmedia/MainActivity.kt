package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likeText.text = countToString(post.countLike)
                shareText.text = countToString(post.countShare)
                visibilityText.text = countToString(post.countVisibility)
                likeImage.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_48 else R.drawable.ic_favorite_48
                )
            }
        }
        binding.likeImage.setOnClickListener {
            viewModel.like()
        }
        binding.shareImage.setOnClickListener {
            viewModel.share()
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


