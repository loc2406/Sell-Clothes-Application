package com.locnguyen.saleclothesapplication.model

data class Comment(
    var name: String="",
    var content: String="",
    var star: Int = 0,
    var like: Int = 0,
    var unlike: Int = 0
){
    constructor(): this("")
}
