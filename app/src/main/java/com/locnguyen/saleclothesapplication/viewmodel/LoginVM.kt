package com.locnguyen.saleclothesapplication.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.locnguyen.saleclothesapplication.repo.UserRepo

class LoginVM: ViewModel() {

    private val repo: UserRepo = UserRepo()

    val email: MutableLiveData<String> by lazy {MutableLiveData("")}
    val password: MutableLiveData<String> by lazy {MutableLiveData("")}

    val login: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }
    val logup: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }

    private val isValidInfo: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}

    private val _messageInvalid: MutableLiveData<String> by lazy {MutableLiveData("")}
    val messageInvalid: LiveData<String> by lazy {_messageInvalid}

    private val _isLoggedIn: LiveData<Pair<Boolean, String>> = isValidInfo.switchMap { isValid ->
        if (isValid){
            repo.isLoggedIn(email.value!!, password.value!!)
        }
        else {
            MutableLiveData()
        }
    }
    val isLoggedIn: LiveData<Pair<Boolean, String>> by lazy { _isLoggedIn }

    fun login(){
        login.value = true
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
            else -> {
                _messageInvalid.value = ""
                true
            }
        }
    }

    private fun isValidEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email.value!!).matches()
    }

    private fun isNotContainSpecialChar(value: String): Boolean {
        return value.matches(Regex("^[a-zA-Z0-9]*$"))
    }

    fun logup(){
        logup.value = true
    }

}