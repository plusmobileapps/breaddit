package com.plusmobileapps.breaddit.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.plusmobileapps.breaddit.logTag
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class RedditPostRepository(private val dao: RedditPostDao, private val client: OkHttpClient, private val gson: Gson) {

    private val breadUrls = listOf(
        "https://www.reddit.com/r/breaddit/.json",
        "https://www.reddit.com/r/Breadit/.json",
        "https://reddit.com/r/BreadStapledToTrees/.json",
        "https://www.reddit.com/r/GarlicBreadMemes/.json"
    )

    fun load(): LiveData<List<RedditPost>> {
        breadUrls.forEach { breadUrl ->
            val request = Request.Builder().url(breadUrl).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e(logTag, "$request $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body()?.string() ?: return
                    val response = gson.fromJson(body, RedditFeedResponse::class.java)
                }
            })
        }

        return dao.getPosts()
    }

}


data class RedditFeedResponse(
    val kind: String,
    val data: RedditFeedData
)

data class RedditFeedData(
    val modhash: String,
    val dist: Int,
    val children: List<RedditFeedChild>,
    val after: String
)

data class RedditFeedChild(
    val kind: String,
    val data: ApiRedditPost
)

data class ApiRedditPost(
    val id: String,
    val author: String,
    val title: String,
    val subreddit_name_prefixed: String,
    val score: Int,
    val created: Int,
    val subreddit_id: String,
    val num_comments: Int,
    val permalink: String,
    val url: String,
    val subreddit_subscribers: Int,
    val created_utc: Int,
    val media: Media,
    val is_video: Boolean
)

data class Media(
    val oembed: Embedded,
    val type: String
)

data class Embedded(
    val provider_url: String,
    val description: String,
    val title: String,
    val url: String,
    val type: String,
    val thumbnail_width: Int,
    val height: Int,
    val width: Int,
    val html: String,
    val version: String,
    val provider_name: String,
    val thumbnail_url: String,
    val thumbnail_height: Int
)