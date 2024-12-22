package com.example.chap2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chap2.databinding.ActivityMainBinding

// 툴바. (뒤로가기, 장바구니)
// 책 클릭시 이동

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // 옵저버로 등록
    private val cartUpdateListener: (Int) -> Unit = { count ->
        updateCartBadge(count)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        //홈 화면 배너
        binding.list.setOnClickListener{
            binding.listItem.visibility = View.VISIBLE
            binding.gridItem.visibility = View.INVISIBLE
        }

        binding.grid.setOnClickListener{
            binding.listItem.visibility = View.INVISIBLE
            binding.gridItem.visibility = View.VISIBLE
        }


        // 액션바
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 장바구니 클릭 리스너
        binding.cartLayout.cartIcon.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
            Toast.makeText(this, "장바구니로 이동", Toast.LENGTH_SHORT).show()
        }


        // 책 클릭시 이동
        binding.ListBook1.setOnClickListener {
            val intent = Intent(this, BookDetailActivity::class.java).apply {
                putExtra("bookId", getString(R.string.book1_id))
                putExtra("bookTitle", getString(R.string.book1_title))
                putExtra("bookPrice", getString(R.string.book1_price).toInt())
                putExtra("imageResId", R.drawable.book11)
                putExtra("description", getString(R.string.book1_description))
            }
            startActivity(intent)
        }
        binding.ListBook2.setOnClickListener {
            val intent = Intent(this, BookDetailActivity::class.java).apply {
                putExtra("bookId", getString(R.string.book2_id))
                putExtra("bookTitle", getString(R.string.book2_title))
                putExtra("bookPrice", getString(R.string.book2_price).toInt())
                putExtra("imageResId", R.drawable.book21)
                putExtra("description", getString(R.string.book2_description))
            }
            startActivity(intent)
        }
        binding.ListBook3.setOnClickListener {
            val intent = Intent(this, BookDetailActivity::class.java).apply {
                putExtra("bookId", getString(R.string.book3_id))
                putExtra("bookTitle", getString(R.string.book3_title))
                putExtra("bookPrice", getString(R.string.book3_price).toInt())
                putExtra("imageResId", R.drawable.book31)
                putExtra("description", getString(R.string.book3_description))
            }
            startActivity(intent)
        }
        binding.ListBook4.setOnClickListener {
            val intent = Intent(this, BookDetailActivity::class.java).apply {
                putExtra("bookId", getString(R.string.book4_id))
                putExtra("bookTitle", getString(R.string.book4_title))
                putExtra("bookPrice", getString(R.string.book4_price).toInt())
                putExtra("imageResId", R.drawable.book41)
                putExtra("description", getString(R.string.book4_description))
            }
            startActivity(intent)
        }

        updateCartBadge(CartManager.getItemCount())

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // 업데이트
    private fun updateCartBadge(count: Int) {
        if (count >= 0) {
            binding.cartLayout.badgeCount.visibility = View.VISIBLE
            binding.cartLayout.badgeCount.text = count.toString()
        } else {
            binding.cartLayout.badgeCount.visibility = View.GONE
        }
    }

    // 옵저빙
    override fun onResume() {
        super.onResume()
        CartManager.addListener(cartUpdateListener)
        updateCartBadge(CartManager.getItemCount())
    }

    override fun onPause() {
        super.onPause()
        CartManager.removeListener(cartUpdateListener)
    }

}