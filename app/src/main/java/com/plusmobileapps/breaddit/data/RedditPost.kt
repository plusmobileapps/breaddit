package com.plusmobileapps.breaddit.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class RedditPost(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val subreddit: String,

    val author_fullname: String,

    val title: String,

    val ups: Int,

    val url: String

)