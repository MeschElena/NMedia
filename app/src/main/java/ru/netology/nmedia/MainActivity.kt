package ru.netology.nmedia

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
        binding.saveButton.setOnClickListener{
            with(binding.contentEditText) {
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)
                clearFocus()
                hideKeyboard()
                binding.group.visibility = View.GONE
            }
        }

        binding.closeButton.setOnClickListener{
            binding.group.visibility = View.GONE
            with(binding.contentEditText) {
                clearFocus()
                hideKeyboard()
                setText("")
            }
        }

        viewModel.currentPost.observe(this) {currentPost ->
            with(binding.contentEditText) {
                val content = currentPost?.content
                setText(content)
                if (content != null) {
                    val editText = "Edit Message: $content"
                    binding.group.visibility = View.VISIBLE
                    binding.contentEditTextClose.setText(editText)
                    requestFocus()
                    showKeyboard()
                } else {
                    clearFocus()
                    hideKeyboard()
                }
            }
        }
    }
}


