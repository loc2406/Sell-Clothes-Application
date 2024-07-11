package com.locnguyen.saleclothesapplication.model

import java.io.Serializable

data class Clothes(
    var imgs: List<String> = emptyList(),
    var name: String = "",
    var group: String = "",
    var description: String = "",
    var colors: List<ClothesColor> = emptyList(),
    var sizes: List<String> = emptyList(),
    var price: Long = 0,
    var comments: List<Comment> = emptyList(),
    var sell: Long = 0
) : Serializable