package com.locnguyen.saleclothesapplication.model

import java.io.Serializable

data class Clothes(
    var img: String = "",
    var name: String = "",
    var group: String = "",
    var description: String = "",
    var color: List<Int> = emptyList(),
    var size: List<String> = emptyList(),
    var price: Long = 0,
    var coment: List<Comment> = emptyList()
): Serializable {
    constructor(): this("")
}