package com.locnguyen.saleclothesapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locnguyen.saleclothesapplication.model.Order
import com.locnguyen.saleclothesapplication.repo.UserRepo

class MainVM: ViewModel() {

    private val userRepo = UserRepo()

    private val _deleteState: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}
    val deleteState: LiveData<Boolean> by lazy { _deleteState }

    private val _confirmDelete: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}
    val confirmDelete: LiveData<Boolean> by lazy { _confirmDelete }

    private val _createOrder: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}
    val createOrder: LiveData<Boolean> by lazy { _createOrder }

    val changePassword: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}
    val editInfo: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}
    val logout: MutableLiveData<Boolean> by lazy {MutableLiveData(false)}

    val listOrder: LiveData<List<Order>> = userRepo.getListOrder()

    val user = userRepo.getUserInfo()

    fun setDeleteState(){
        _deleteState.value = (deleteState.value != true)
    }

    fun offDeleteState(){
        _deleteState.value = false
    }

    fun confirmDelete(){
        _confirmDelete.value = true
    }

    fun createOrder(){
        _createOrder.value = true
    }

    fun changePassword(){
        changePassword.value = true
    }

    fun editInfo(){
        editInfo.value = true
    }

    fun logout(){
        logout.value = true
    }
}