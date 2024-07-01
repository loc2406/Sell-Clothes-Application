package com.locnguyen.saleclothesapplication.model

import java.io.Serializable

data class ClothesColor(
    var name: String="",
    var hexCode: String=""
): Serializable{
    constructor(): this("")
}
