package com.locnguyen.saleclothesapplication.model

data class User(
    var id: String = "",
    var name: String = "",
    var img: String = "",
    var phone: Long = 0,
    var location: String = "",
    var email: String = "",
    var password: String = ""
) {
    constructor(): this("","", "", -1, "", "", "")
}