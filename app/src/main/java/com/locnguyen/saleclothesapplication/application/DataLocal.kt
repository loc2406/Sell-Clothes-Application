package com.locnguyen.saleclothesapplication.application

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract.Data
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.edit
import com.bumptech.glide.Glide
import com.locnguyen.saleclothesapplication.R
import java.text.NumberFormat
import java.util.Locale

class SharedRef(val context: Context) {

    private val SHARE_REF_CODE = "SHARE_REF_CODE"
    private val sharedRef: SharedPreferences by lazy { context.getSharedPreferences(SHARE_REF_CODE, Context.MODE_PRIVATE) }

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
    var densityValue: Float = 0.0f
    lateinit var priceFormat: NumberFormat

    companion object{
        private lateinit var instance: DataLocal

        fun init(context: Context){
            instance = DataLocal()
            instance.sharedRef = SharedRef(context)
            instance.densityValue = context.resources.displayMetrics.density
            instance.priceFormat = NumberFormat.getNumberInstance(Locale.GERMANY)
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

    fun bindImg(context: Context, img: String, view: ImageView){
        Glide.with(context)
            .load(img)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_loading_err)
            .into(view)
    }

    fun bindImgWithoutPlaceHolder(context: Context, img: String, view: ImageView){
        Glide.with(context)
            .load(img)
            .error(R.drawable.ic_loading_err)
            .into(view)
    }

    fun showToast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}