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
import com.locnguyen.saleclothesapplication.databinding.LoginActivityBinding
import com.locnguyen.saleclothesapplication.viewmodel.LoginVM

class LoginActivity: AppCompatActivity() {

    private lateinit var binding:LoginActivityBinding
    private lateinit var loginVM: LoginVM
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.login_activity)
        loginVM = ViewModelProvider(this)[LoginVM::class.java]

        binding.lifecycleOwner = this
        binding.loginVM = loginVM

        initObserves()

    }

    private fun initObserves() {
        loginVM.login.observe(this){ isClicked ->
            if (isClicked){
                loadingDialog.show()
            }
        }

        loginVM.logup.observe(this){ isClicked ->
            if (isClicked){
                startActivity(Intent(this, LogupActivity::class.java))
                loginVM.logup.value = false
            }
        }

        loginVM.messageInvalid.observe(this){ message ->
            if (message.isNotEmpty()){
                loginVM.login.value = false
                showToast(message)
            }
        }

        loginVM.isLoggedIn.observe(this){ isLoggedIn ->
            when{
               isLoggedIn.first -> {
                    DataLocal.getInstance().setIsLoggedIn(true)
                   showToast("Đăng nhập thành công!")
                   startActivity(Intent(this, MainActivity::class.java))
                   finish()
                }

                !isLoggedIn.first -> {
                    showToast(isLoggedIn.second)
                }
            }
        }
    }

    private fun showToast(message: String){
        loadingDialog.dismiss()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}