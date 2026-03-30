package com.teddy.mirandaytoledo

import android.app.Application
import com.teddy.mirandaytoledo.catalog.di.colorModule
import com.teddy.mirandaytoledo.catalog.di.educationalLevelModule
import com.teddy.mirandaytoledo.catalog.di.frameModelModule
import com.teddy.mirandaytoledo.catalog.di.frameRestrictionModule
import com.teddy.mirandaytoledo.catalog.di.finishModule
import com.teddy.mirandaytoledo.catalog.di.productTypeModule
import com.teddy.mirandaytoledo.catalog.di.schoolGroupModule
import com.teddy.mirandaytoledo.catalog.di.schoolModule
import com.teddy.mirandaytoledo.catalog.di.sizeModule
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

            modules(
                appModule,
                educationalLevelModule,
                schoolModule,
                schoolGroupModule,
                finishModule,
                colorModule,
                frameModelModule,
                frameRestrictionModule,
                sizeModule,
                productTypeModule
            )
        }
    }
}
