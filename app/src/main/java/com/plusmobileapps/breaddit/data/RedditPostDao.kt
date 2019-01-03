package com.plusmobileapps.breaddit.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RedditPostDao {

    @Query("SELECT * FROM redditpost")
    fun getPosts(): LiveData<List<RedditPost>>

    @Query("SELECT * FROM redditpost WHERE id in (:id)")
    fun getById(id: Int): LiveData<RedditPost>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: RedditPost)

    @Delete
    fun deletePost(post: RedditPost)

}