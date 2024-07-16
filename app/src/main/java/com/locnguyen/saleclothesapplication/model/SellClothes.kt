package com.locnguyen.saleclothesapplication.model

import java.io.Serializable

data class SellClothes(
    var img: String = "",
    var name: String = "",
    var group: String = "",
    var description: String = "",
    var color: ClothesColor = ClothesColor(),
    var size: String = "",
    var price: Long = 0,
    var quantity: Long = 0
) {
    fun isSameTypeWithoutQuantity(clothes: SellClothes): Boolean {
        return img == clothes.img &&
                name == clothes.name &&
                group == clothes.group &&
                description == clothes.description &&
                color == clothes.color &&
                size == clothes.size &&
                price == clothes.price &&
                quantity != clothes.quantity
    }
}