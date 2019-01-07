package com.plusmobileapps.breaddit.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.plusmobileapps.breaddit.logTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

inline fun <reified T : Any> Gson.fromGson(response: String): T {
    return this.fromJson(response, T::class.java)
}

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
                    convertToRedditPosts(body)

                }
            })
        }

        return dao.getPosts()
    }

    fun getRedditPost(id: String): LiveData<RedditPost> {
        return dao.getById(id)
    }

    private fun convertToRedditPosts(bodyResponse: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = gson.fromGson<RedditFeedResponse>(bodyResponse)
            val redditPosts = response.data.children.map { redditFeedChild ->
                val redditData = redditFeedChild.data
                return@map RedditPost(
                    id = redditData.id,
                    author = redditData.author,
                    title = redditData.title,
                    selfText = redditData.selftext,
                    subreddit_name_prefixed = redditData.subreddit_name_prefixed,
                    score = redditData.score,
                    created = redditData.created,
                    num_comments = redditData.num_comments,
                    permalink = redditData.permalink,
                    url = redditData.url,
                    subreddit_subscribers = redditData.subreddit_subscribers,
                    created_utc = redditData.created_utc,
//                            media = redditData.media,
                    is_video = redditData.is_video,
                    subreddit_id = redditData.subreddit_id
                )
            }
            dao.insertPosts(redditPosts)
        }
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
    val selftext: String,
    val subreddit_name_prefixed: String,
    val score: Int,
    val created: Int,
    val subreddit_id: String,
    val num_comments: Int,
    val permalink: String,
    val url: String,
    val subreddit_subscribers: Int,
    val created_utc: Int,
    val media: Media?,
    val is_video: Boolean
)

@Entity
data class Media(
    @PrimaryKey(autoGenerate = true)
    val id: Int = -1,
    val oembed: Embedded,
    val type: String
)


@Entity
data class Embedded(
    @PrimaryKey(autoGenerate = true)
    val id: Int = -1,
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