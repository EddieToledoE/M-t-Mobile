package com.teddy.mirandaytoledo

import android.app.Application
import com.teddy.mirandaytoledo.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MirandaytoledoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MirandaytoledoApp)
            androidLogger()

            modules(appModule)
        }
    }
}