package com.example.schoolmanagement

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.schoolmanagement.DI.appModule
import com.example.schoolmanagement.Data.Local.createDataStore
import com.example.schoolmanagement.UI.Navigation.App
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication : Application()  {
    companion object {
        lateinit var appContext: Context
            private set
    }
    override fun onCreate() {
        super.onCreate()
        appContext = this
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule,
                module {
                    single<DataStore<Preferences>> { createDataStore(this@MyApplication) }
                }
                )
        }
    }
}