package com.locnguyen.saleclothesapplication.model

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.createStringArrayList()?.toList() ?: emptyList(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createTypedArrayList(ClothesColor)?.toList() ?: emptyList(),
        parcel.createStringArrayList()?.toList() ?: emptyList(),
        parcel.readLong(),
        parcel.createTypedArrayList(Comment)?.toList() ?: emptyList(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(imgs)
        parcel.writeString(name)
        parcel.writeString(group)
        parcel.writeString(description)
        parcel.writeTypedList(colors)
        parcel.writeStringList(sizes)
        parcel.writeLong(price)
        parcel.writeTypedList(comments)
        parcel.writeLong(sell)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Clothes> {
        override fun createFromParcel(parcel: Parcel): Clothes {
            return Clothes(parcel)
        }

        override fun newArray(size: Int): Array<Clothes?> {
            return arrayOfNulls(size)
        }
    }
}