package com.locnguyen.saleclothesapplication.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.model.Clothes
import com.locnguyen.saleclothesapplication.model.ClothesColor
import com.locnguyen.saleclothesapplication.repo.UserRepo

class ClothesInfoVM: ViewModel() {
    val userRepo = UserRepo()

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

    fun selectColor(id: Int){
        _selectedColor.value = id
    }

    fun selectSize(view: View){
        _selectedSize.value = view.id
    }

    fun addCart(){
        _addCart.value = true
    }

    fun getImgName(listImg: List<String>, clothesName: String, colorName: String): String {
       return userRepo.getImgNameFromUri(listImg, clothesName, colorName)
    }

}