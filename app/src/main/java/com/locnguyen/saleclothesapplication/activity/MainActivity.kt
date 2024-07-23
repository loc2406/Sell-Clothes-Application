package com.locnguyen.saleclothesapplication.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.application.DataLocal
import com.locnguyen.saleclothesapplication.databinding.MainActivityBinding
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController
    private var isPressedBackBefore: Boolean = false
    private val timer: Timer by lazy {Timer()}
    private val executor: Executor by lazy {ContextCompat.getMainExecutor(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_SaleClothesApplication)
        setContentView(binding.root)

        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        onBackPressedDispatcher.addCallback {
            if (::navController.isInitialized) {
                if (!navController.popBackStack()) {
                    if (isPressedBackBefore) {
                        finish()
                    } else {
                        DataLocal.getInstance().showToast(this@MainActivity, "Nhấn 1 lần nữa để thoát ứng dụng")
                        isPressedBackBefore = true
                    }
                } else {
                    navController.navigateUp()
                }
            }

            timer.schedule(object: TimerTask(){
                override fun run() {
                   executor.execute {
                       isPressedBackBefore = false
                   }
                }
            }, 3000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}
