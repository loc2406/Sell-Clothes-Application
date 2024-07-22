package com.locnguyen.saleclothesapplication.application

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.provider.ContactsContract.Data
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.ui.graphics.Color
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

    fun showToast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getAlertDialog(context: Context, title: String, content: String, positive: String? = null, negative: String? = null, positiveAction: (() -> Unit)? = null, negativeAction: (() -> Unit)? = null): AlertDialog{
        val dialogView = View.inflate(context, R.layout.my_custom_dialog, null)

        val titleView = dialogView.findViewById<TextView>(R.id.dialog_title)
        val contentView = dialogView.findViewById<TextView>(R.id.dialog_content)
        val positiveView = dialogView.findViewById<TextView>(R.id.dialog_positive)
        val negativeView = dialogView.findViewById<TextView>(R.id.dialog_negative)

        title.let{ titleView?.text = it }
        content.let{ contentView?.text = it }
        positive?.let{ positiveView?.text = it }

        if (negative!= null){
            negativeView?.text = negative
        }
        else{
            negativeView?.visibility = GONE
        }

        val dialog =  AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawable(AppCompatResources.getDrawable(context, R.drawable.background_transparent_rectangle))

        positiveView?.setOnClickListener {
            positiveAction?.invoke()
            dialog.dismiss()
        }
        negativeView?.setOnClickListener {
            negativeAction?.invoke()
            dialog.dismiss()
        }

        return dialog
    }
}