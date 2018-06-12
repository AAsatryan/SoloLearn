package com.example.arsen.sololearntask.activity

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration


class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        val config: RealmConfiguration = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)
    }
}