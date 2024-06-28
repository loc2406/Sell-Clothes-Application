package com.locnguyen.saleclothesapplication.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.application.DataLocal
import com.locnguyen.saleclothesapplication.application.LoadingDialog
import com.locnguyen.saleclothesapplication.databinding.LogupActivityBinding
import com.locnguyen.saleclothesapplication.viewmodel.LogupVM

class LogupActivity: AppCompatActivity() {

    private lateinit var binding: LogupActivityBinding
    private lateinit var logupVM: LogupVM
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.logup_activity)
        logupVM = ViewModelProvider(this)[LogupVM::class.java]

        binding.lifecycleOwner = this
        binding.logupVM = logupVM

        initObserves()

    }

    private fun initObserves() {
        logupVM.logup.observe(this){ isClicked ->
            if (isClicked){
                loadingDialog.show()
            }
        }

        logupVM.messageInvalid.observe(this){ message ->
            if (message.isNotEmpty()){
                logupVM.logup.value = false
                showToast(message)
            }
        }

        logupVM.isLoggedUp.observe(this){ isLogged ->
            when (isLogged){
                true -> {
                    DataLocal.getInstance().setIsLoggedIn(true)
                    showToast("Đăng kí tài khoản thành công!")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                false -> {
                    showToast("Đăng kí tài khoản thất bại!")
                }
            }
        }
    }

    private fun showToast(message: String){
        loadingDialog.dismiss()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}