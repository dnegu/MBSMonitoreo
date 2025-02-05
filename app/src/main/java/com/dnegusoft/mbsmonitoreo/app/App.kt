package com.dnegusoft.mbsmonitoreo.app

import android.app.Application
import com.dnegusoft.mbsmonitoreo.di.module.apiModule
import com.dnegusoft.mbsmonitoreo.di.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(apiModule,appModule)
        }
    }
}