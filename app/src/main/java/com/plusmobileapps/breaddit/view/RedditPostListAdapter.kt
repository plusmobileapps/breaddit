package com.plusmobileapps.breaddit.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.plusmobileapps.breaddit.R
import com.plusmobileapps.breaddit.data.RedditPost

class RedditPostDiffUtil : DiffUtil.ItemCallback<RedditPost>() {
    override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
        return oldItem == newItem
    }
}

class RedditPostListAdapter(private val itemClickListener: RedditPostClickListener) : ListAdapter<RedditPost, RedditPostVH>(RedditPostDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditPostVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reddit_post_list_item, parent, false)
        return RedditPostVH(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: RedditPostVH, position: Int) = holder.bind(getItem(position))

}


class RedditPostVH(itemView: View, private val itemClickListener: RedditPostClickListener): RecyclerView.ViewHolder(itemView) {

    val title = itemView.findViewById<TextView>(R.id.reddit_post_title)
    val subredditName = itemView.findViewById<TextView>(R.id.reddit_post_subbreddit_name)
    val ups = itemView.findViewById<TextView>(R.id.reddit_post_ups)
    val upButton = itemView.findViewById<ImageView>(R.id.reddit_post_upvote)
    val downButton = itemView.findViewById<ImageView>(R.id.reddit_post_downvote)

    fun bind(redditPost: RedditPost) {
        title.text = redditPost.title
        subredditName.text = redditPost.subreddit_name_prefixed
        ups.text = redditPost.score.toString()

        itemView.setOnClickListener { itemClickListener.onPostClicked(redditPost) }
        upButton.setOnClickListener { itemClickListener.onUpVoteClicked(redditPost) }
        downButton.setOnClickListener { itemClickListener.onDownVoteClicked(redditPost) }
    }

}

interface RedditPostClickListener {
    fun onPostClicked(redditPost: RedditPost)
    fun onUpVoteClicked(redditPost: RedditPost)
    fun onDownVoteClicked(redditPost: RedditPost)
}