package com.locnguyen.saleclothesapplication.model

import android.os.Parcel
import android.os.Parcelable
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
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(ClothesColor::class.java.getClassLoader())!!,
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(img)
        parcel.writeString(name)
        parcel.writeString(group)
        parcel.writeString(description)
        parcel.writeParcelable(color, flags)
        parcel.writeString(size)
        parcel.writeLong(price)
        parcel.writeLong(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun isSameTypeButSmallerOrEqualQuantity(clothes:SellClothes): Boolean{
        return img == clothes.img &&
                name == clothes.name &&
                group == clothes.group &&
                description == clothes.description &&
                color == clothes.color &&
                size == clothes.size &&
                price == clothes.price &&
                (quantity < clothes.quantity || quantity == clothes.quantity)
    }

    companion object CREATOR : Parcelable.Creator<SellClothes> {
        override fun createFromParcel(parcel: Parcel): SellClothes {
            return SellClothes(parcel)
        }

        override fun newArray(size: Int): Array<SellClothes?> {
            return arrayOfNulls(size)
        }
    }
}