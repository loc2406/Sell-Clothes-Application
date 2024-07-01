package com.locnguyen.saleclothesapplication.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.locnguyen.saleclothesapplication.application.DataLocal
import com.locnguyen.saleclothesapplication.model.Order
import com.locnguyen.saleclothesapplication.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.locnguyen.saleclothesapplication.model.ClothesColor

class UserRepo {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val listUsersInfoRef: DatabaseReference =  Firebase.database.getReference("List users")
    private val listUsersImgRef: StorageReference =  Firebase.storage.getReference("List users")

    fun isLoggedIn(email: String, password: String): LiveData<Pair<Boolean, String>> {
        val isLoggedIn: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()

        listUsersInfoRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    snapshot.children.forEach{ userSnapshot ->
                        val user = userSnapshot.getValue(User::class.java)

                        user?.let {
                            when {
                                it.email == email && it.password == password -> {
                                    auth.signInWithEmailAndPassword(email, password)
                                        .addOnSuccessListener {
                                            val userAuthId = it.user!!.uid

                                            DataLocal.getInstance().setUserId(userAuthId)
                                            isLoggedIn.value = Pair(true, "Tài khoản đã tồn tại!")
                                    }
                                        .addOnFailureListener { e ->
                                            isLoggedIn.value = Pair(false, e.message.toString())
                                        }

                                }

                                it.email == email -> {
                                    isLoggedIn.value = Pair(false, "Sai mật khẩu!")
                                }

                                else -> {
                                    isLoggedIn.value = Pair(false, "Tài khoản chưa được đăng kí!")
                                }
                            }
                        } ?: {
                            isLoggedIn.value = Pair(false, "Không tìm thấy người dùng!")
                        }
                    }
                }
                else{
                    isLoggedIn.value = Pair(false, "Tài khoản chưa được đăng kí!")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                isLoggedIn.value = Pair(false, "Có lỗi xảy ra! Vui lòng khởi động lại!")
            }
        })

        return isLoggedIn
    }

    fun isExistAccount(email: String) : LiveData<Boolean>{
        val isExist: MutableLiveData<Boolean> = MutableLiveData(false)

        listUsersInfoRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    snapshot.children.forEach{ userSnapshot ->
                        val user = userSnapshot.getValue(User::class.java)

                        user?.let {
                            if (it.email == email){
                                isExist.value = true
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        return isExist
    }

    fun logupNewAccount(email: String, password: String): LiveData<Boolean> {
        val isLogedUp: MutableLiveData<Boolean> = MutableLiveData()
        val newUser = User(email = email, password = password)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        val userAuthId = it.user!!.uid
                        newUser.id = userAuthId

                        listUsersInfoRef.child(userAuthId)
                            .setValue(newUser)
                            .addOnSuccessListener {
                                DataLocal.getInstance().setUserId(newUser.id)
                                isLogedUp.value = true
                            }
                            .addOnFailureListener {
                                isLogedUp.value = false
                            }
                    }
                    .addOnFailureListener {
                        isLogedUp.value = false
                    }
            }
            .addOnFailureListener {
                isLogedUp.value = false
            }

        return isLogedUp
    }

    fun getUserInfo(): LiveData<User> {
        val user: MutableLiveData<User> = MutableLiveData()

        listUsersInfoRef.child(DataLocal.getInstance().getUserId()).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user.value = snapshot.getValue(User::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        return user
    }

    fun createOrder(order: Order): LiveData<Boolean> {
        val isCreated: MutableLiveData<Boolean> = MutableLiveData()

       listUsersInfoRef.child(DataLocal.getInstance().getUserId()).child("List orders").child(order.id)
           .setValue(order)
           .addOnSuccessListener {
               isCreated.value = true
           }
           .addOnFailureListener {
               isCreated.value = false
           }

        return isCreated
    }

    fun getListOrder(): LiveData<List<Order>> {
        val listLD: MutableLiveData<List<Order>> = MutableLiveData(emptyList())
        val list = mutableListOf<Order>()

        listUsersInfoRef.child(DataLocal.getInstance().getUserId()).child("List orders").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    snapshot.children.forEach { orderSnapshot ->
                        val order = orderSnapshot.getValue(Order::class.java)
                        order?.let { list.add(it) }
                    }
                    listLD.value = list
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        return listLD
    }

    fun update(map: Map<String, Any>): LiveData<Boolean> {
        val isUpdated: MutableLiveData<Boolean> = MutableLiveData()

        listUsersInfoRef.child(DataLocal.getInstance().getUserId())
            .updateChildren(map)
            .addOnSuccessListener {
                isUpdated.value = true
            }
            .addOnFailureListener {
                isUpdated.value = false
            }

        return isUpdated
    }

    fun getImgNameFromUri(listImg: List<String>, clothesName: String, colorName: String): String {
        var nameFound = ""

        listImg.forEach { img ->
            val imgName = FirebaseStorage.getInstance().getReferenceFromUrl(img).name.substringBeforeLast(".")
            if (imgName == "$clothesName, $colorName") nameFound = imgName
        }

        return nameFound
    }

}