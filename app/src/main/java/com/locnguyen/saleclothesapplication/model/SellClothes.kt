package com.locnguyen.saleclothesapplication.model

import java.io.Serializable

data class SellClothes(
    var img: String = "",
    var name: String = "",
    var group: String = "",
    var description: String = "",
    var color: Int = 0,
    var size: String = "",
    var price: Long = 0,
    var quality: Long = 0
) : Serializable{
    constructor(): this("")
}