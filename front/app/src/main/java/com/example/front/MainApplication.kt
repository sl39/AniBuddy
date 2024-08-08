package com.example.front

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("chat.db")
            .schemaVersion(1)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}