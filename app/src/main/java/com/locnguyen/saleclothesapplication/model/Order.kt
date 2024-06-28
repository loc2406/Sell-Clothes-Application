package com.locnguyen.saleclothesapplication.model

data class Order(
    var id: String = "",
    var date: String = "",
    var listClothes: List<SellClothes> = emptyList(),
    var receiver: String = "",
    var phone: Long = 0,
    var address: String = "",
    var paymentMethod: String = "Tiền mặt",
    var state: String = "Chờ xử lí",
    var price: Long = 0
) {
    constructor(): this("")
}