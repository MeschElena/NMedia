package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel)

        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.shareEvent.observe(this) { post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, post.content)
            }

            val shareIntent = Intent.createChooser(intent, "Поделится")
            startActivity(shareIntent)
        }

        viewModel.playEvent.observe(this) { post ->
            val intent = Intent().apply {
                putExtra(Intent.ACTION_VIEW, Uri.parse(post.video))
            }

            val videoIntent = Intent.createChooser(intent, "Open with")
            startActivity(videoIntent)
        }

        val activityLauncher = registerForActivityResult(NewPostActivity.ResultContract) {
            postContent: String? ->
            postContent?.let(viewModel::onCreatNewPost)
        }

        val activityEditLauncher = registerForActivityResult(NewPostActivity.ResultContractEdit) {
                postContent: String? ->
            postContent?.let(viewModel::onSaveButtonClicked)
        }

        binding.fab.setOnClickListener {
            activityLauncher.launch(Unit)
        }

        viewModel.currentPost.observe(this) {currentPost ->
            val content = currentPost?.content
            if (content != null) {
                activityEditLauncher.launch(content)
            }
        }
    }
}


