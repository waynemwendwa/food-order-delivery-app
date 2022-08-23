package com.example.fooddeliveryapp

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.adapter.PlaceYourOrderAdapter
import com.example.fooddeliveryapp.models.RestaurantModel
import kotlinx.android.synthetic.main.activity_place_your_order.*

class PlaceYourOrderActivity : AppCompatActivity() {

    var placeYourOrderAdapter: PlaceYourOrderAdapter? = null
    var isDeliveryOn: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_your_order)

        val restaurantModel: RestaurantModel? = intent.getParcelableExtra("RestaurantModel")
        val actionbar: ActionBar? = supportActionBar
        actionbar?.title = restaurantModel?.name
        actionbar?.subtitle = restaurantModel?.address
        actionbar?.setDisplayHomeAsUpEnabled(true)



        buttonPlaceYourOrder.setOnClickListener {
            onPlaceOrderButtonCLick(restaurantModel)
        }
        buttonCardPayment.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val layout = layoutInflater.inflate(R.layout.custom_dialog,null)
            builder.setTitle("Card Payment Details")
            val editCardNumber : EditText = layout.findViewById(R.id.cardNumber)
            val editCardExpiry : EditText = layout.findViewById(R.id.cardExpiry)
            val editCardPin : EditText = layout.findViewById(R.id.cardPin)
            builder.setView(layout)
            builder.setPositiveButton("OK"){_,_->
               val cardNumber = editCardNumber.text.toString().trim().toIntOrNull()
               val cardExpiry = editCardExpiry.text.toString().trim().toIntOrNull()
               val cardPin = editCardPin.text.toString().trim().toIntOrNull()

                if (cardNumber != null && cardExpiry != null && cardPin != null){
                    saveEntries()
                }else{
                    Toast.makeText(this,"Enter valid values",Toast.LENGTH_LONG).show()
                }
            }
            builder.setNegativeButton("CANCEL"){_,_->

            }
            builder.show()
        }

        buttonMpesaPayment.setOnClickListener {
             val builder = AlertDialog.Builder(this)
            builder.setTitle("M-PESA DETAILS")
            builder.setMessage("Go to MPESA -> Lipa na M-PESA -> Paybill ->Enter business number as 254254 , account number as ORDER")
            builder.show()
        }


        switchDelivery?.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked) {
                inputAddress.visibility = View.VISIBLE
                inputLocation.visibility = View.VISIBLE
                inputPhone.visibility = View.VISIBLE
                tvDeliveryCharge.visibility = View.VISIBLE
                tvDeliveryChargeAmount.visibility = View.VISIBLE
                isDeliveryOn = true
                calculateTotalAmount(restaurantModel)
            } else {
                inputAddress.visibility = View.GONE
                inputLocation.visibility = View.GONE
                inputPhone.visibility = View.GONE
                tvDeliveryCharge.visibility = View.GONE
                tvDeliveryChargeAmount.visibility = View.GONE
                isDeliveryOn = false
                calculateTotalAmount(restaurantModel)
            }
        }

        initRecyclerView(restaurantModel)
        calculateTotalAmount(restaurantModel)
    }

    private fun initRecyclerView(restaurantModel: RestaurantModel?) {
        cartItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        placeYourOrderAdapter = PlaceYourOrderAdapter(restaurantModel?.menus)
        cartItemsRecyclerView.adapter =placeYourOrderAdapter
    }

    private fun calculateTotalAmount(restaurantModel: RestaurantModel?) {
        var subTotalAmount = 0f
        try {
            for(menu in restaurantModel?.menus!!) {
                subTotalAmount += menu?.price!!  * menu?.totalInCart!!

            }
            tvSubtotalAmount.text = "Ksh."+ String.format("%.2f", subTotalAmount)
            if(isDeliveryOn) {
                tvDeliveryChargeAmount.text = "Ksh."+String.format("%.2f", restaurantModel.delivery_charge?.toFloat())
                subTotalAmount += restaurantModel?.delivery_charge?.toFloat()!!
            }
            tvTotalAmount.text = "Ksh."+ String.format("%.2f", subTotalAmount)
        }catch (e:Exception){
            Toast.makeText(this@PlaceYourOrderActivity,"Cart is empty,please place items in cart",Toast.LENGTH_LONG).show()
            finish()
        }

    }

    private fun onPlaceOrderButtonCLick(restaurantModel: RestaurantModel?) {
        if(TextUtils.isEmpty(inputName.text.toString())) {
            inputName.error =  "Enter your name"
            return
        } else if(isDeliveryOn && TextUtils.isEmpty(inputAddress.text.toString())) {
            inputAddress.error =  "Enter your address"
            return
        } else if(isDeliveryOn && TextUtils.isEmpty(inputLocation.text.toString())) {
            inputLocation.error =  "Enter your City Name"
            return
        } else if(isDeliveryOn && TextUtils.isEmpty(inputPhone.text.toString())) {
            inputPhone.error =  "Enter your Phone Number"
            return
        } /*else if( TextUtils.isEmpty(inputCardNumber.text.toString())) {
            inputCardNumber.error =  "Enter your credit card number"
            return
        } else if( TextUtils.isEmpty(inputCardExpiry.text.toString())) {
            inputCardExpiry.error =  "Enter your credit card expiry"
            return
        } else if( TextUtils.isEmpty(inputCardPin.text.toString())) {
            inputCardPin.error =  "Enter your credit card pin/cvv"
            return
        }*/

        val intent = Intent(this@PlaceYourOrderActivity, SuccessOrderActivity::class.java)
        intent.putExtra("RestaurantModel", restaurantModel)
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1000) {
            setResult(RESULT_OK)
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }
    fun saveEntries(){
        Toast.makeText(this,"Details Saved successfully",Toast.LENGTH_LONG).show()
    }
}