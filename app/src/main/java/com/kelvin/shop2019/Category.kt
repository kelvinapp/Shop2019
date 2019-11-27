package com.kelvin.shop2019

data class Category(var id:String , var name:String){
    override fun toString(): String {
        return name
    }
}