package com.kelvin.shop2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail2.*

class Detail2Activity : AppCompatActivity() {
    lateinit var itemA: ItemA
    private val TAG = Detail2Activity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail2)
       itemA = intent.getParcelableExtra<ItemA>("ITEMA")
        Log.d(TAG,"onCreate: ${itemA.id} / ${itemA.title_a}")
        web2.settings.javaScriptEnabled = true  //  web 啟動功能javaScriptEnabled
        web2.loadUrl(itemA.content)  // 首先firebase 加入content=網址資料,啟動content
    }

    override fun onStart() {
        super.onStart()
        //加入點擊次數
        itemA.ViewCount++
        itemA.id?.let {
            FirebaseFirestore.getInstance().collection("items_a")
                .document(it).set(itemA)
        }
    }
}
