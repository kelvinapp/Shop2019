package com.kelvin.shop2019

import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(var title:String,
                var price:Int,
                var imageUri : String,
                var id: String,
                var content: String ,
                var ViewCount: Int
) : Parcelable{
    constructor(): this("",0,"", "","",0)
}
//firebase 加入content