package com.kelvin.shop2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail.*
import kotlin.math.log

class DetailActivity : AppCompatActivity() {
    lateinit var item: Item
    private val TAG = DetailActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        item = intent.getParcelableExtra<Item>("ITEM") //加入item尋找
        Log.d(TAG,"onCreate: ${item.id} / ${item.title}")//加入item = id / title
        web.settings.javaScriptEnabled = true  //  web 啟動功能javaScriptEnabled
        web.loadUrl(item.content)  // 首先firebase 加入content=網址資料,啟動content

    }
    override fun onStart() {
        super.onStart()
        item.ViewCount++
        item.id?.let {
            FirebaseFirestore.getInstance().collection("items")
                .document(it).set(item)
         }
    }
}
//update("viewCount",item.ViewCount)