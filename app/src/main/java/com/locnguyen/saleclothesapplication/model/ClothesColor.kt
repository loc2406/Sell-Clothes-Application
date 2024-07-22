package com.locnguyen.saleclothesapplication.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class ClothesColor(
    var name: String="",
    var hexCode: String=""
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(hexCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ClothesColor> {
        override fun createFromParcel(parcel: Parcel): ClothesColor {
            return ClothesColor(parcel)
        }

        override fun newArray(size: Int): Array<ClothesColor?> {
            return arrayOfNulls(size)
        }
    }
}
