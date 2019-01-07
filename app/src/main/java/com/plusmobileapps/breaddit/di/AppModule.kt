package com.plusmobileapps.breaddit.di

import androidx.room.Room
import com.google.gson.Gson
import com.plusmobileapps.breaddit.viewmodels.MainViewModel
import com.plusmobileapps.breaddit.data.AppDatabase
import com.plusmobileapps.breaddit.data.RedditPostRepository
import com.plusmobileapps.breaddit.viewmodels.RedditPostDetailViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    single { Gson() }
    single { OkHttpClient() }
    single<AppDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "appDb.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().redditPostDao() }
    single { RedditPostRepository(get(), get(), get()) }

    viewModel { MainViewModel(get()) }
    viewModel { RedditPostDetailViewModel(get()) }

}