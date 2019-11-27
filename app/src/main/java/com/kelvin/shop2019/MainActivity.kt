package com.kelvin.shop2019

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.activity_test.view.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


class MainActivity : AppCompatActivity(),FirebaseAuth.AuthStateListener {


    private val RC_TEST = 200
//   private val RC_test = 200
    private val RC_SIGNIN = 100
    private val TAG = MainActivity::class.java.simpleName
//    private lateinit var adapter : FirestoreRecyclerAdapter<Item,ItemHolder>
    var categories = mutableListOf<Category>()
   lateinit var adapter: ItemAdapter
   lateinit var itemViewModel : ItemViewModel //加入此全部都能使用,產生物件

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        verify_email.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Snackbar.make(it,"Verify email sent @.@",Snackbar.LENGTH_LONG)
                    }
                }
        }

        //製作分類
        FirebaseFirestore.getInstance().collection("categories")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful){
                    task.result?.let {
                        categories.add(Category("","不分類"))
                        for (doc in it){
                            categories.add(Category(doc.id,doc.data.get("name").toString()))
                        }
                        spinner. adapter = ArrayAdapter<Category>(
                            this@MainActivity,
                            android.R.layout.simple_spinner_item,
                            categories)
                            .apply {
                                setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                            }
                        spinner.setSelection(0,false)
                        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                              setupAdapter()
                            }
                        }
                    }
                }
            }

        //setupRecyclerView
        recycler_C.setHasFixedSize(true)
        recycler_C.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recycler_A.setHasFixedSize(true)
        recycler_A.layoutManager = LinearLayoutManager(this)
       adapter = ItemAdapter(mutableListOf<Item>())
        recycler_A.adapter = adapter
        recycler_C.adapter = adapter

           itemViewModel = ViewModelProviders.of(this)
              .get(ItemViewModel::class.java)
            itemViewModel.getItems().observe(this,androidx.lifecycle.Observer {
                Log.d(TAG,"observe: ${it.size}")
                adapter.items = it
                adapter.notifyDataSetChanged()
                /*//己有ViewModel資料 ,在Main Activity裡面使用,
                //ViewMode 物件取得方法
                itemViewModel = ViewModelProviders.of(this)
                    .get(ItemViewModel::class.java)//取得這個(ItemViewModel)物件
                itemViewModel.getItems().observe(this,androidx.lifecycle.Observer {
                    //内容(observe) 觀察適合幫我以下呼叫以下observe顯示
                    Log.d(TAG,"observe: ${it.size}")
                    adapter.items = it
                    androidx.lifecycle.Observer
                    adapter.notifyDataSetChanged()//發現adapter的數据更新*/
            })
//       setupAdapter()
    }

    inner class ItemAdapter(var items:List<Item>) : RecyclerView.Adapter<ItemHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
          return ItemHolder(
              LayoutInflater.from(parent.context)
                  .inflate(R.layout.item_row,parent,false)
          )
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.bindTo(items.get(position))
            holder.itemView.setOnClickListener {
                itemClicked(items.get(position),position)
            }
        }
    }

    /*inner class ItemAdapter(var items: List<Item>) : RecyclerView.Adapter<ItemHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            return ItemHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_row,parent,false)
            )
        }
        override fun getItemCount(): Int {
           return items.size
        }
        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
           holder.bindTo(items.get(position))
            holder.itemView.setOnClickListener {
                itemClicked(items.get(position),position)
            }
        }
    }*/

/*private fun setupAdapter() {
        val selected = spinner.selectedItemPosition
        var query = if (selected >0){
            adapter.stopListening()
            FirebaseFirestore.getInstance()
                .collection("items")
                .whereEqualTo("category",categories.get(selected).id)
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(10)
        }else{
            FirebaseFirestore.getInstance()
                .collection("items")
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(10)
        }

        val options = FirestoreRecyclerOptions.Builder<Item>()
            .setQuery(query, Item::class.java)
            .build()
        adapter = object : FirestoreRecyclerAdapter<Item, ItemHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_row, parent, false)
                return ItemHolder(view)
            }

            override fun onBindViewHolder(holder: ItemHolder, position: Int, item: Item) {
                item.id = snapshots.getSnapshot(position).id   //加入firebase的id
                holder.bindTo(item)
                holder.itemView.setOnClickListener {
                    itemClicked(item, position)
                }
            }
        }
        recycler_A.adapter = adapter
        recycler_C.adapter = adapter
        adapter.startListening()
    }*/

    private fun itemClicked(item: Item, position: Int) {
        Log.d(TAG, "itemClicked: ${item.title} / $position")
        //按下商品是傳送到DetailActivity
        val intent = Intent(this,DetailActivity::class.java)
        intent.putExtra("ITEM",item)
        startActivity(intent)
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
            val user = auth.currentUser
            Log.d(TAG,"onAuthStateChanged: ${user?.uid}")
            if (user !=null){
                user_info.setText("Email: ${user.email} / ${user.isEmailVerified}")
                verify_email.visibility = if (user.isEmailVerified) View.GONE else View.VISIBLE
                ss_email.visibility = if (user.isEmailVerified) View.GONE else View.VISIBLE
            }else{
                user_info.setText("Not login-^.^")
                verify_email.visibility = View.GONE
                ss_email.visibility = View.GONE
            }
        }
    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
//        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
//        adapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.Action_signin ->{
                val whiteList = listOf<String>("hk")
               val myLayout = AuthMethodPickerLayout.Builder(R.layout.activity_singup)
                    .setGoogleButtonId(R.id.signup_email)
                    .setPhoneButtonId(R.id.signup_phone)
                    .build()
                startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(Arrays.asList(
                        /*AuthUI.IdpConfig.EmailBuilder().build(),*/
                        AuthUI.IdpConfig.GoogleBuilder().build(),
                        AuthUI.IdpConfig.PhoneBuilder()
                            .setWhitelistedCountries(whiteList)
                            .setDefaultCountryIso("hk")
                            .build()
                    ))
                    .setIsSmartLockEnabled(false) //ture
                    .setTheme(R.style.SignUp)
                    .setAuthMethodPickerLayout(myLayout)
                    .build(),
                    RC_SIGNIN)

                /*startActivityForResult(Intent(this, SinginActivity::class.java),
                 RC_SIGNIN  )*/
                true
            }
            R.id.action_test ->{
                startActivityForResult(Intent(this,TestActivity::class.java),
                    RC_TEST  )

                true
            }
            R.id.action_signout ->{
                FirebaseAuth.getInstance().signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
