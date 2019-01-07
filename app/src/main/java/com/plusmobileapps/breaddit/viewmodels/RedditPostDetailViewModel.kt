package com.plusmobileapps.breaddit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.plusmobileapps.breaddit.data.RedditPost
import com.plusmobileapps.breaddit.data.RedditPostRepository

class RedditPostDetailViewModel(private val redditPostRepository: RedditPostRepository) : ViewModel() {

    fun loadRedditPost(id: String): LiveData<RedditPost> {
        return redditPostRepository.getRedditPost(id)
    }

}