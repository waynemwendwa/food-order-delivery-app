package com.example.fooddeliveryapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fooddeliveryapp.adapter.MenuListAdapter
import com.example.fooddeliveryapp.models.Menus
import com.example.fooddeliveryapp.models.RestaurantModel
import kotlinx.android.synthetic.main.activity_restaurant_menu.*

class RestaurantMenuActivity : AppCompatActivity(), MenuListAdapter.MenuListClickListener {

    private var itemsInTheCartList: MutableList<Menus?>? = null
    private var totalItemInCartCount = 0
    private var menuList: List<Menus?>? = null
    private var menuListAdapter: MenuListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)

        val restaurantModel = intent?.getParcelableExtra<RestaurantModel>("RestaurantModel")

        val actionBar: ActionBar? = supportActionBar

        actionBar?.title = restaurantModel?.name
        actionBar?.subtitle = restaurantModel?.address
        actionBar?.setDisplayHomeAsUpEnabled(true)

        menuList = restaurantModel?.menus

        initRecyclerView(menuList)
        checkoutButton.setOnClickListener {
             if (itemsInTheCartList != null && itemsInTheCartList!!.size <= 0){
                 Toast.makeText(this@RestaurantMenuActivity,"Please place some items in the cart",Toast.LENGTH_LONG).show()
             }else{
                 restaurantModel?.menus = itemsInTheCartList
                 val intent = Intent(this@RestaurantMenuActivity, PlaceYourOrderActivity::class.java)
                 intent.putExtra("RestaurantModel",restaurantModel)
                 startActivityForResult(intent,1000)

             }
        }
    }
    private fun initRecyclerView(menus: List<Menus?>?){
        menuRecyclerView.layoutManager =GridLayoutManager(this,2)
        menuListAdapter = MenuListAdapter(menus,this)
        menuRecyclerView.adapter = menuListAdapter
    }

    override fun addToCartClickListener(menu: Menus) {
         if (itemsInTheCartList == null){
             itemsInTheCartList = ArrayList()
         }
        itemsInTheCartList?.add(menu)
        totalItemInCartCount = 0
        for (menu in itemsInTheCartList!!){
            totalItemInCartCount += menu?.totalInCart!!
        }
        checkoutButton.text = "Checkout(" + totalItemInCartCount +")Items"
    }

    override fun updateCartClickListener(menu: Menus) {
        val index =  itemsInTheCartList!!.indexOf(menu)
        itemsInTheCartList?.removeAt(index)
        itemsInTheCartList?.add(menu)
        totalItemInCartCount = 0
        for (menu in itemsInTheCartList!!){
            totalItemInCartCount += menu?.totalInCart!!
        }
        checkoutButton.text = "Checkout(" + totalItemInCartCount +")Items"

    }

    override fun deleteCartClickListener(menu: Menus) {
        if (itemsInTheCartList!!.contains(menu)){
            itemsInTheCartList?.remove(menu)
            totalItemInCartCount = 0
            for (menu in itemsInTheCartList!!){
                totalItemInCartCount += menu?.totalInCart!!
            }
            checkoutButton.text = "Checkout(" + totalItemInCartCount +")Items"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK){
            finish()
        }
    }
}