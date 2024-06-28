package com.locnguyen.saleclothesapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.locnguyen.saleclothesapplication.application.DataLocal

class RoutingActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition{true}
        checkLogin()
        finish()
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
    }

    private fun goMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}