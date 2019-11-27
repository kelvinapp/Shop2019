package com.kelvin.shop2019

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.content_main.*


class TestActivity : AppCompatActivity() {
    private val TAG = TestActivity::class.java.simpleName
    private lateinit var adapter : FirestoreRecyclerAdapter<ItemA,ItemHolderA>
    var categories_A = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        //製作分類
        FirebaseFirestore.getInstance().collection("categories_A")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        categories_A.add(Category("", "不分類"))
                        for (doc in it) {
                            categories_A.add(Category(doc.id, doc.data.get("name").toString()))
                        }
                        spinner2.adapter = ArrayAdapter<Category>(
                            this@TestActivity,
                            android.R.layout.simple_spinner_item,
                            categories_A
                        )
                            .apply {
                                setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                            }
                        spinner2.setSelection(0, false)
                        spinner2.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    setupAdapter_A()
                                }
                            }
                    }
                }
            }

        //setupRecyclerView
        recyclerView_B.setHasFixedSize(true)
        recyclerView_B.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        setupAdapter_A()
    }

    private fun setupAdapter_A() {
        val selected = spinner2.selectedItemPosition
        var query = if (selected >0){
            adapter.stopListening()
            FirebaseFirestore.getInstance()
                .collection("items_a")
                .whereEqualTo("category",categories_A.get(selected).id)
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(10)
        }else{
            FirebaseFirestore.getInstance()
                .collection("items_a")
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(10)
        }
        val options = FirestoreRecyclerOptions.Builder<ItemA>()
            .setQuery(query, ItemA::class.java)
            .build()
        adapter = object : FirestoreRecyclerAdapter<ItemA, ItemHolderA>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolderA {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_row, parent, false)
                return ItemHolderA(view)
            }

            override fun onBindViewHolder(holder: ItemHolderA, position: Int, itemA: ItemA) {
                itemA.id = snapshots.getSnapshot(position).id   //加入firebase的id
                holder.bindTo(itemA)
                holder.itemView.setOnClickListener {
                    itemClicked(itemA, position)
                }
            }
        }
        recyclerView_B.adapter = adapter
                adapter.startListening()
    }
    //2.點清單(商品)後執行的地方
    private fun itemClicked(itemA: ItemA, position: Int) {
        Log.d(TAG, "itemClicked: ${itemA.title_a} / $position")
        Log.d(TAG, "itemClicked: ${itemA.price_a} / $position")
        val intent = Intent(this,Detail2Activity::class.java)
        intent.putExtra("ITEMA",itemA)
        startActivity(intent)
        //按下商品是傳送到DetailActivity
    }
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }
    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}
//製造商品的詳細頁面