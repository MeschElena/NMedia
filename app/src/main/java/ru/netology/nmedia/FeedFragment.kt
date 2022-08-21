package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentFeedBinding


class FeedFragment : Fragment() {
    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                action = Intent.ACTION_VIEW
                data = Uri.parse(post.video)
                //         putExtra(, )
            }

            val videoIntent = Intent.createChooser(intent, "Open with")
            startActivity(videoIntent)
        }

        setFragmentResultListener(
            requestKey = NewPostFragment.REQUEST_KEY
        ) {requestKey, bundle ->
            if (requestKey != NewPostFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(NewPostFragment.RESULT_KEY) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newPostContent)

        }

        viewModel.navigateToPostContentScreenEven.observe(this){initialContent ->
            val direction = FeedFragmentDirections.actionFeedFragmentToNewPostFragment(initialContent)
            findNavController().navigate(direction)
        }


        viewModel.navigateToPostContentScreenView.observe(this){initialIdPost ->
            val directionView = FeedFragmentDirections.actionFeedFragmentToPostViewFragment(initialIdPost)
            findNavController().navigate(directionView)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFeedBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = PostsAdapter(viewModel)

        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }

    }.root

    companion object{
        const val TAG = "FeedFragment"
    }


}


