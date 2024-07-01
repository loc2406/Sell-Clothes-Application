package com.locnguyen.saleclothesapplication.model

import java.io.Serializable

data class Comment(
    var name: String="",
    var content: String="",
    var star: Int = 0,
    var like: Long = 0,
    var unlike: Long = 0
): Serializable{
    constructor(): this("")
}
