package com.example.chap2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chap2.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "장바구니"

        setupCartItems()
        updateTotalPrice()

        binding.selectAllCheckbox.setOnCheckedChangeListener { _, isChecked ->
            CartManager.getItems().forEach { it.isSelected = isChecked }
            setupCartItems()
            updateTotalPrice()
        }

        binding.buyButton.setOnClickListener {
            val intent = Intent(this, DeliveryInfoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupCartItems() {
        binding.cartItemsContainer.removeAllViews()

        CartManager.getItems().forEach { cartItem ->
            val itemView = LayoutInflater.from(this)
                .inflate(R.layout.item_cart, binding.cartItemsContainer, false)

            itemView.findViewById<CheckBox>(R.id.checkBox).apply {
                isChecked = cartItem.isSelected
                setOnCheckedChangeListener { _, isChecked ->
                    cartItem.isSelected = isChecked
                    updateTotalPrice()
                }
            }

            itemView.findViewById<ImageView>(R.id.bookImage)
                .setImageResource(cartItem.imageResId)
            itemView.findViewById<TextView>(R.id.bookTitle).text = cartItem.title
            itemView.findViewById<TextView>(R.id.bookPrice).text =
                "정가: ${cartItem.price}원"

            itemView.findViewById<ImageButton>(R.id.deleteButton).setOnClickListener {
                CartManager.removeItem(cartItem.bookId)
                setupCartItems()
                updateTotalPrice()
            }

            binding.cartItemsContainer.addView(itemView)
        }
    }

    private fun updateTotalPrice() {
        binding.totalPrice.text = "금액: ${CartManager.getTotalPrice()}원"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}