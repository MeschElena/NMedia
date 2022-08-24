package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.FragmentPostBinding

class PostViewFragment: Fragment() {
   // application: Application
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private val args by navArgs<PostViewFragmentArgs>()
  //  private val repository: PostRepository = FilePostRepository(application)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPostBinding.inflate(inflater, container, false).also { binding ->

        var removePost = false
        val viewHolder = PostsAdapter.ViewHolder(binding.PostLayoult, object : PostIteractionListener{
            override fun onLikeClicked(post: Post) = viewModel.onLikeClicked(post)

            override fun onRemoveClicked(post: Post) {
                removePost = true
                viewModel.onRemoveClicked(post)
                findNavController().popBackStack()
            }

            override fun onShareClicked(post: Post) = viewModel.onShareClicked(post)

            override fun onEditClicked(post: Post) {
                viewModel.navigateToPostContentViewEven.value = post.content
                viewModel.currentPost.value = post
            }
            override fun onPlayVideo(post: Post) = viewModel.onPlayVideo(post)
            override fun onPostClicked(post: Post) = viewModel.onPostClicked(post)
        }
        )

        viewModel.data.observe(viewLifecycleOwner) {
            if (!removePost){
                viewHolder.bind(viewModel.getPost(args.initialIdPost))
            }
        }
    }.root

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

        viewModel.navigateToPostContentViewEven.observe(this){initialContent ->
            val direction = PostViewFragmentDirections.actionPostViewFragmentToNewPostFragment(initialContent)
            findNavController().navigate(direction)
        }
    }

}