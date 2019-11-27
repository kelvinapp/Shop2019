package com.kelvin.shop2019

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
//在firebase加入資料,firebase資料進入ItemA,之後ItemA提供itemHolderA控制資料到item_row,(首先item_row設計排列位置)
data class ItemA(var title_a : String,
                 var price_a:Int,
                 var imageUri : String,
                 var id : String,
                 var  content: String,
                 var ViewCount: Int
    ):Parcelable{
    constructor(): this("",0,"","","",0)
}