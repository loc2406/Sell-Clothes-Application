package com.locnguyen.saleclothesapplication.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locnguyen.saleclothesapplication.R
import com.locnguyen.saleclothesapplication.model.Clothes
import com.locnguyen.saleclothesapplication.model.ClothesColor
import com.locnguyen.saleclothesapplication.model.SellClothes
import com.locnguyen.saleclothesapplication.repo.UserRepo

class ClothesInfoVM: ViewModel() {
    val userRepo = UserRepo()

    private val _back: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}
    val back: LiveData<Boolean> by lazy {_back}

    private val _selectedColorId: MutableLiveData<Int> by lazy { MutableLiveData() }
    val selectedColorId: LiveData<Int> by lazy {_selectedColorId}

    private val _selectedSizeId: MutableLiveData<Int> by lazy { MutableLiveData() }
    val selectedSizeId: LiveData<Int> by lazy {_selectedSizeId}

    private val _addCart: MutableLiveData<Boolean> by lazy {MutableLiveData()}
    val addCart: LiveData<Boolean> by lazy {_addCart}

    private val _selectedFilterId: MutableLiveData<Int> by lazy {MutableLiveData(R.id.filter_all)}
    val selectedFilterId: LiveData<Int> by lazy {_selectedFilterId}

    val haveComments: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }

    private val _clothesQuantity: MutableLiveData<Long> by lazy { MutableLiveData(1) }
    val clothesQuantity: LiveData<Long> by lazy{_clothesQuantity}

    fun back(){
        _back.value = true
    }

    fun selectColor(id: Int){
        _selectedColorId.value = id
    }

    fun selectSize(id: Int){
        _selectedSizeId.value = id
    }

    fun selectFilter(id: Int){
        _selectedFilterId.value = id
    }

    fun increaseQuantity(){
        _clothesQuantity.value = _clothesQuantity.value!! + 1
    }

    fun decreaseQuantity(){
        _clothesQuantity.value = _clothesQuantity.value!! - 1
    }

    fun addCart(){
        _addCart.value = true
    }

    fun setHaveCommentsValue(value: Boolean){
        haveComments.value = value
    }

    fun addClothesToCart(clothes: SellClothes) : LiveData<Boolean>{
        return userRepo.addClothesToCartOnFb(clothes)
    }

    fun isExistedClothes(clothes: SellClothes): LiveData<Boolean> {
        return userRepo.isExistedClothes(clothes)
    }

    fun updateClothesQuantity(clothesNamePath: String, quantity: Long): LiveData<Boolean>{
        return userRepo.updateClothesQuantityOnFb(clothesNamePath, quantity)
    }

}