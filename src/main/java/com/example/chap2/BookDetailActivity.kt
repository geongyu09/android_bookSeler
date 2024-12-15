package com.example.chap2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chap2.databinding.ActivityBookDetailBinding

class BookDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailBinding
    private val cartUpdateListener: (Int) -> Unit = { count ->
        updateCartBadge(count)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "도서 상세정보"

        // 카트 아이콘 초기화
        updateCartBadge(CartManager.getItemCount())

        // 전달받은 도서 정보 표시
        val bookId = intent.getStringExtra("bookId") ?: "BOOK1234"
        val bookTitle = intent.getStringExtra("bookTitle") ?: "자바 코딩의 기술"
        val bookPrice = intent.getIntExtra("bookPrice", 22000)
        val imageResId = intent.getIntExtra("imageResId", R.drawable.book11)
        val publishDate = intent.getStringExtra("publishDate") ?: "2020-07-30"
        val description = intent.getStringExtra("description") ?:
        "버그 발생.."

        // UI 업데이트
        binding.bookImage.setImageResource(imageResId)
        binding.bookId.text = "도서ID : $bookId"
        binding.bookTitle.text = "도서명 : $bookTitle"
        binding.bookPrice.text = "정 가 : ${bookPrice}원"
        binding.publishDate.text = "출간일 : $publishDate"
        binding.bookDescription.text = "설 명 : $description"

        // 장바구니 담기 버튼 클릭 리스너
        binding.addToCartButton.setOnClickListener {
            CartManager.addItem(
                bookId = bookId,
                title = bookTitle,
                price = bookPrice,
                imageResId = imageResId
            )
            Toast.makeText(this,
                "장바구니에 추가되었습니다.",
                Toast.LENGTH_SHORT).show()
        }

        // 장바구니 아이콘 클릭 리스너
        binding.cartLayout.cartIcon.setOnClickListener {
            Toast.makeText(this, "장바구니로 이동", Toast.LENGTH_SHORT).show()
        }
    }

    // 업 버튼 클릭 처리
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun updateCartBadge(count: Int) {
        if (count >= 0) {
            binding.cartLayout.badgeCount.visibility = View.VISIBLE
            binding.cartLayout.badgeCount.text = count.toString()
        } else {
            binding.cartLayout.badgeCount.visibility = View.GONE
        }
    }


    override fun onResume() {
        super.onResume()
        CartManager.addListener(cartUpdateListener)
    }

    override fun onPause() {
        super.onPause()
        CartManager.removeListener(cartUpdateListener)
    }
}