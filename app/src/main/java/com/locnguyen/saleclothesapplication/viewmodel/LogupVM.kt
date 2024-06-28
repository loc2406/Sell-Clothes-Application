package com.locnguyen.saleclothesapplication.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.locnguyen.saleclothesapplication.repo.UserRepo

class LogupVM: ViewModel() {

    private val userRepo: UserRepo = UserRepo()

    val logup: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }

    val email: MutableLiveData<String> by lazy { MutableLiveData("") }
    val password: MutableLiveData<String> by lazy { MutableLiveData("") }
    val confirmPassword: MutableLiveData<String> by lazy { MutableLiveData("") }

    private val isValidInfo:MutableLiveData<Boolean> by lazy { MutableLiveData(false) }

    private val _messageInvalid: MutableLiveData<String> by lazy {MutableLiveData("")}
    val messageInvalid: LiveData<String> by lazy {_messageInvalid}

    private val isExist: LiveData<Boolean> = isValidInfo.switchMap { isValid ->
        if (isValid){
            userRepo.isExistAccount(email.value!!)
        }
        else{
            MutableLiveData()
        }
    }

    val isLoggedUp: LiveData<Boolean> = isExist.switchMap { isExist ->
        if (isExist){
            MutableLiveData()
        }
        else{
            userRepo.logupNewAccount(email.value!!, password.value!!)
        }
    }

    fun logup(){
        logup.value = true
        isValidInfo.value = checkValid()
    }

    private fun checkValid(): Boolean {
        return when{
            email.value!!.isBlank() -> {
                _messageInvalid.value = "Email không được trống!"
                false
            }

            !isValidEmail() -> {
                _messageInvalid.value = "Email không hợp lệ!"
                false
            }

            password.value!!.isBlank() -> {
                _messageInvalid.value = "Mật khẩu không được trống!"
                false
            }

            !isNotContainSpecialChar(password.value!!) -> {
                _messageInvalid.value = "Mật khẩu không được chứa kí tự đặc biệt!"
                false
            }

            !isMoreThan7Char() -> {
                _messageInvalid.value = "Mật khẩu phải từ 8 kí tự trở lên!"
                false
            }

            confirmPassword.value!!.isBlank() -> {
                _messageInvalid.value = "Mật khẩu xác nhận không được trống!"
                false
            }

            password.value != confirmPassword.value   -> {
                _messageInvalid.value = "Mật khẩu xác nhận không giống!"
                false
            }

            else -> {
                _messageInvalid.value = ""
                true
            }
        }
    }

    private fun isMoreThan7Char(): Boolean {
        return password.value!!.length >= 8
    }

    private fun isValidEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email.value!!).matches()
    }

    private fun isNotContainSpecialChar(value: String): Boolean {
        return value.matches(Regex("^[a-zA-Z0-9]*$"))
    }

}