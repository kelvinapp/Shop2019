package com.kelvin.shop2019


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row.view.*

class ItemHolderA (view: View):RecyclerView.ViewHolder(view){
    //在ItemA內容已firebase資料進入,在itemHolderA控制插入資料在item_row,(首先item_row設計排列位置)
    var titleText  = view.item_title
    var priceText  = view.item_price
    //在ItemA內容已firebase資料進入,在itemHolderA控制插入圖片在item_row,(首先item_row設計排列位置)
    var image = view.item_image
    var countText = view.item_count

    fun bindTo(itemA: ItemA){
        titleText.setText(itemA.title_a)
        priceText.setText(itemA.price_a.toString())
        Glide.with(itemView.context)
            .load(itemA.imageUri)
            .apply(RequestOptions().override(120))
            .into(image)
           countText.setText(itemA.ViewCount.toString())//畫面加入點擊次數
            countText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.eye,0,0,0)//畫面加入圖像在點擊次數位置

    }
}