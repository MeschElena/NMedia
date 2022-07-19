package ru.netology.nmedia

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostListItemBinding

internal class PostsAdapter(
    private val interactionListener: PostIteractionListener,
    ) : ListAdapter<Post, PostsAdapter.ViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class ViewHolder(
        private val binding: PostListItemBinding,
        listener: PostIteractionListener,
    ) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var post: Post

    private val popupMenu by lazy {
        PopupMenu(itemView.context, binding.menu).apply {
            inflate(R.menu.options_post)
            setOnMenuItemClickListener {menuItem ->
                when (menuItem.itemId) {
                    R.id.remove -> {
                        listener.onRemoveClicked(post)
                        true
                    }
                    R.id.edit -> {
                        listener.onEditClicked(post)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    init {
        binding.likeImage.setOnClickListener{
            listener.onLikeClicked(post)
        }
        binding.shareImage.setOnClickListener {
            listener.onShareClicked(post)
        }
    }
    fun bind(post: Post) {
        this.post = post
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
            menu.setOnClickListener { popupMenu.show() }
        }
    }

    private fun countToString (count: Long): String {
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

    private object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }
    }