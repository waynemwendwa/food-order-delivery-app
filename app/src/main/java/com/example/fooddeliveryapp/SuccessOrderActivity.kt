package com.example.fooddeliveryapp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.example.fooddeliveryapp.models.RestaurantModel
import kotlinx.android.synthetic.main.activity_success_order.*

class SuccessOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_order)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        val restaurantModel: RestaurantModel? = intent.getParcelableExtra("RestaurantModel")
        val actionbar: ActionBar? = supportActionBar
        actionbar?.setTitle(restaurantModel?.name)
        actionbar?.setSubtitle(restaurantModel?.address)
        actionbar?.setDisplayHomeAsUpEnabled(false)

        buttonDone.setOnClickListener {
            Toast.makeText(this,"Order will be delivered shortly",Toast.LENGTH_LONG).show()
            setResult(RESULT_OK)
            finish()
        }
        buttonCancel.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure you want to cancel?")
            builder.setMessage("Once you cancel your order you will have to make a new one")
            builder.setPositiveButton("Ok"){_,_->
                Toast.makeText(this,"Order Cancelled",Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
            builder.setNegativeButton("Cancel"){_,_->
                //Toast.makeText(this,"Cancelled",Toast.LENGTH_SHORT).show()
            }
            builder.show()

        }
    }
}