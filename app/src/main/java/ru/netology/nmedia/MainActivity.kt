package ru.netology.nmedia

import android.content.Intent
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


//        binding.saveButton.setOnClickListener{
//            with(binding.contentEditText) {
//                val content = text.toString()
//                viewModel.onSaveButtonClicked(content)
//                clearFocus()
//                hideKeyboard()
//                binding.group.visibility = View.GONE
//            }
//        }
//
//        binding.closeButton.setOnClickListener{
//            binding.group.visibility = View.GONE
//            with(binding.contentEditText) {
//                clearFocus()
//                hideKeyboard()
//                setText("")
//            }
//        }

        viewModel.currentPost.observe(this) {currentPost ->
            val content = currentPost?.content
            if (content != null) {
//                val editPostIntent = Intent(this, NewPostActivity::class.java)
//                editPostIntent.putExtra(Intent.EXTRA_TEXT, content)
    //            startActivity(editPostIntent)
                activityEditLauncher.launch(content)
            }


//            val intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                type = "text/plain"
//

    //        }


            //with(binding.contentEditText) {
//                val content = currentPost?.content
//                setText(content)
//                if (content != null) {
//                    val editText = "Edit Message: $content"
//                    binding.group.visibility = View.VISIBLE
//                    binding.contentEditTextClose.setText(editText)
//                    requestFocus()
//                    showKeyboard()
//                } else {
//                    clearFocus()
//                    hideKeyboard()
//                }
//            }
        }


    }
}


