package com.plusmobileapps.breaddit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.plusmobileapps.breaddit.data.RedditPost
import com.plusmobileapps.breaddit.data.RedditPostRepository

private const val AUTH_URL = "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" +
        "&response_type=code&state=%s&redirect_uri=%s&" +
        "duration=permanent&scope=identity"

private const val CLIENT_ID = "kV9k939bhxpvyg"

private const val REDIRECT_URI = "https://plusmobileapps.com/breaddit"

private const val STATE = "MY_RANDOM_STRING_1"

private const val ACCESS_TOKEN_URL = "https://www.reddit.com/api/v1/access_token"


class MainViewModel(private val repository: RedditPostRepository) : ViewModel() {

    val redditPosts: LiveData<List<RedditPost>> = repository.load()

    fun onLoginButtonClicked() {

    }

}