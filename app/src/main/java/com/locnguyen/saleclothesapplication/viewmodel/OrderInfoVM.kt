package com.locnguyen.saleclothesapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locnguyen.saleclothesapplication.model.Order
import com.locnguyen.saleclothesapplication.model.SellClothes
import com.locnguyen.saleclothesapplication.model.User
import com.locnguyen.saleclothesapplication.repo.UserRepo

class OrderInfoVM: ViewModel() {

    private val userRepo: UserRepo by lazy {UserRepo()}
    val user: LiveData<User> = userRepo.getUserInfo()

    private val _back: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}
    val back: LiveData<Boolean> by lazy{_back}

    val confirmCreateOrder: MutableLiveData<Boolean> by lazy{MutableLiveData(false)}

    fun back(){
        _back.value = true
    }

    fun confirmCreateOrder() {
        confirmCreateOrder.value = true
    }

    fun createOrder(order: Order): LiveData<Boolean> {
        return userRepo.createOrder(order)
    }

    fun updateCartAfterOrder(listBoughtClothes: List<SellClothes>): LiveData<Boolean> {
        return userRepo.updateCartAfterOrder(listBoughtClothes)
    }
}