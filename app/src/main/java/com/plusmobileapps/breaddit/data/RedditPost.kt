package com.plusmobileapps.breaddit.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class RedditPost(

    @PrimaryKey(autoGenerate = false)
    val id: String,

    val author: String,
    val title: String,
    val selfText: String,
    val subreddit_name_prefixed: String,
    val score: Int,
    val created: Int,
    val subreddit_id: String,
    val num_comments: Int,
    val permalink: String,
    val url: String,
    val subreddit_subscribers: Int,
    val created_utc: Int,
//    val media: Media?,
    val is_video: Boolean

)