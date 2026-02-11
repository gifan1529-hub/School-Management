package com.example.schoolmanagement

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.NotificationCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.schoolmanagement.DI.appModule
import com.example.schoolmanagement.DI.platformModule
import com.example.schoolmanagement.Data.Local.createDataStore
import com.example.schoolmanagement.UI.Navigation.App
import com.google.firebase.messaging.RemoteMessage
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
            modules(appModule, platformModule,
                module {
                    single<DataStore<Preferences>> { createDataStore(this@MyApplication) }
                }
            )
        }
    }
}