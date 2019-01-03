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
                    val jsonObject = JSONObject(body)
                    val data = jsonObject.optJSONObject("data")
                    val children = data.optJSONArray("children")
                    for (i in 0..(children.length() - 1)) {
                        val child = children.getJSONObject(i)
                        val data = child.getJSONObject("data")
                        val redditPost = RedditPost(
                            subreddit = data.getString("subreddit"),
                            author_fullname = data.getString("author_fullname"),
                            title = data.getString("title"),
                            ups = data.getInt("ups"),
                            url = data.getString("url")
                        )
                        println("$redditPost")
                        GlobalScope.launch {
                            dao.insertPost(redditPost)
                        }

                    }
                }
            })
        }

        return dao.getPosts()
    }

}