package com.locnguyen.saleclothesapplication.model

import java.io.Serializable

data class Comment(
    var name: String="",
    var content: String="",
    var star: Int = 0,
    var favorite: Long = 0,
    var time: String = ""
): Serializable{
    constructor(): this("")
}
