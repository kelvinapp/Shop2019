package com.kelvin.shop2019

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ItemViewModel : ViewModel(){
    private var items = MutableLiveData <List<Item>>()
    fun getItems() : MutableLiveData <List<Item>> {
        FirebaseFirestore.getInstance()
            .collection("items")
            .orderBy("ViewCount",Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener { querySnapshot, exception ->
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    items.value = querySnapshot.toObjects(Item::class.java)
                }
            }
        return items
    }
}
/*
private var items = MutableLiveData <List<Item>> ()//提供給人家資料
//資料移到ItemViewModel裡面
fun getItems() : MutableLiveData <List<Item>> {
    FirebaseFirestore.getInstance()  //在FirebaseFirestore查詢到它身上的資料
        .collection("items")
        .orderBy("ViewCount",Query.Direction.DESCENDING) //排位方法(“查看計數”，查詢。 方向。 下降)
        .limit(10)
        .addSnapshotListener { querySnapshot, exception -> //Firebase資料到,轉成資料到現在的LiveData的集合模式
            if (querySnapshot != null && !querySnapshot.isEmpty){//如果Firebase資料有
                items.value = querySnapshot.toObjects(Item::class.java)
*/

//                    val list= mutableListOf<Item>()//集合的方式收集Item起來,給它類別（Item)資料
//                    for (doc in querySnapshot.documents){//所有文件內容

//                        val item = doc.toObject(Item::class.java)?: Item()//先把item資料加入
//                        item.id = doc.id//這個item設定id = doc 裏面的id
//                        list.add(item)//清單加入
//                    }
//                    items.value = list//list集合放進到value身上


//items.value = querySnapshot.toObjects(Item::class.java)