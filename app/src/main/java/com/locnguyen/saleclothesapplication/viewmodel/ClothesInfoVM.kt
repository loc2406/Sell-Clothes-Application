package com.locnguyen.saleclothesapplication.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.model.Clothes

class ClothesInfoVM: ViewModel() {

    private val _back: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}
    val back: LiveData<Boolean> by lazy {_back}

    val clothes: MutableLiveData<Clothes> by lazy { MutableLiveData() }

    private val _selectedColor: MutableLiveData<Int> by lazy { MutableLiveData(R.id.first_color) }
    val selectedColor: LiveData<Int> by lazy {_selectedColor}

    private val _selectedSize: MutableLiveData<Int> by lazy { MutableLiveData(R.id.first_size) }
    val selectedSize: LiveData<Int> by lazy {_selectedSize}

    private val _addCart: MutableLiveData<Boolean> by lazy {MutableLiveData()}
    val addCart: LiveData<Boolean> by lazy {_addCart}

    fun back(){
        _back.value = true
    }

    fun selectColor(view: View){
        _selectedColor.value = view.id
    }

    fun selectSize(view: View){
        _selectedSize.value = view.id
    }

    fun addCart(){
        _addCart.value = true
    }

}