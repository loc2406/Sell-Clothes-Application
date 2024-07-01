package com.locnguyen.saleclothesapplication.model

import java.io.Serializable

data class Clothes(
    var img: List<String> = emptyList(),
    var name: String = "",
    var group: String = "",
    var description: String = "",
    var color: List<ClothesColor> = emptyList(),
    var size: List<String> = emptyList(),
    var price: Long = 0,
    var coment: List<Comment> = emptyList(),
    var sell: Long = 0
): Serializable {
    constructor(): this(emptyList())
}