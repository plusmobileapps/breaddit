package com.plusmobileapps.breaddit

import android.app.Application
import com.plusmobileapps.breaddit.di.appModule
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}