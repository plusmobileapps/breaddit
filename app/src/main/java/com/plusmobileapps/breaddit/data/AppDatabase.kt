package com.plusmobileapps.breaddit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RedditPost::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun redditPostDao(): RedditPostDao

}