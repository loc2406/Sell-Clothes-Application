package com.locnguyen.saleclothesapplication.application

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.locnguyen.saleclothesapplication.R

class Application: Application() {
    override fun onCreate() {
        super.onCreate()

        DataLocal.init(applicationContext)
        Firebase.initialize(applicationContext)
    }
}