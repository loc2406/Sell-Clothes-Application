package com.locnguyen.saleclothesapplication.application

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize

class Application: Application() {
    override fun onCreate() {
        super.onCreate()

        DataLocal.init(applicationContext)
        Firebase.initialize(applicationContext)
    }
}