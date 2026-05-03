package com.diwan.myprofileapp

import android.app.Application
import com.diwan.myprofileapp.shared.di.androidModule
import com.diwan.myprofileapp.shared.di.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(commonModule, androidModule(BuildConfig.GEMINI_API_KEY))
        }
    }
}