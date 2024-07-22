package com.locnguyen.saleclothesapplication.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.application.DataLocal
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class RoutingActivity: AppCompatActivity() {
    private val loadSplashScreen: Executor by lazy { ContextCompat.getMainExecutor(this) }
    private val loadSplashScreenTimer: Timer by lazy {Timer()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition{true}
        }
        else{
            setTheme(R.style.Theme_SaleClothesApplication)
        }
        checkLogin()
    }

    private fun checkLogin() {
        if (DataLocal.getInstance().getIsLoggedIn()){
            goMainScreen()
        }
        else{
            goLoginScreen()
        }
    }

    private fun goLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun goMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}