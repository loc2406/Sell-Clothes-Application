package com.locnguyen.saleclothesapplication.application

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedRef(private val contex: Context) {

    private val SHARE_REF_CODE = "SHARE_REF_CODE"
    private val sharedRef: SharedPreferences by lazy { contex.getSharedPreferences(SHARE_REF_CODE, Context.MODE_PRIVATE) }

    fun addBooleanValue(key: String, value: Boolean){
        sharedRef.edit {
            putBoolean(key, value)
            apply()
        }
    }

    fun getStringValue(key: String): String{
        return sharedRef.getString(key, "") ?: ""
    }

    fun addStringValue(key: String, value: String){
        sharedRef.edit {
            putString(key, value)
            apply()
        }
    }

    fun getBooleanValue(key: String): Boolean{
        return sharedRef.getBoolean(key, false)
    }

}

class DataLocal{

    private lateinit var sharedRef: SharedRef
    private val USER_ID = "USER_ID"
    private val IS_LOGGED_IN = "IS_LOGGED_IN"

    companion object{
        private lateinit var instance: DataLocal

        fun init(context: Context){
            instance = DataLocal()
            instance.sharedRef = SharedRef(context)
        }

        fun getInstance(): DataLocal = instance
    }

    fun setUserId(value: String){
        sharedRef.addStringValue(USER_ID, value)
    }

    fun getUserId(): String = sharedRef.getStringValue(USER_ID)

    fun setIsLoggedIn(value: Boolean){
        sharedRef.addBooleanValue(IS_LOGGED_IN, value)
    }

    fun getIsLoggedIn(): Boolean = sharedRef.getBooleanValue(IS_LOGGED_IN)

}