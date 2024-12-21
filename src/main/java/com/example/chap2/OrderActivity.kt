package com.example.chap2

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog  // 추가
import androidx.appcompat.app.AppCompatActivity
import com.example.chap2.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "주문서"

        // 배송 정보 표시
        displayDeliveryInfo()
        // 선택된 도서 목록 표시
        displaySelectedBooks()
        // 버튼 설정
        setupButtons()
    }

    private fun displayDeliveryInfo() {
        // 배송 정보 표시
        binding.nameText.text = "성명: ${intent.getStringExtra("name")}"
        binding.deliveryDateText.text = "배송일: ${intent.getStringExtra("deliveryDate")}"
        binding.phoneText.text = "연락처: ${intent.getStringExtra("phone")}"
        binding.addressText.text = "주소: ${intent.getStringExtra("zipCode")}\n${intent.getStringExtra("address")}"
    }

    private fun displaySelectedBooks() {
        var totalPrice = 0

        // CartManager에서 선택된 도서만 필터링하여 표시
        CartManager.getItems().filter { it.isSelected }.forEach { book ->
            val bookView = layoutInflater.inflate(R.layout.item_order_book, binding.bookListContainer, false)
            bookView.findViewById<TextView>(R.id.bookTitle).text = book.title
            bookView.findViewById<TextView>(R.id.bookPrice).text = "${book.price}원"
            binding.bookListContainer.addView(bookView)
            totalPrice += book.price
        }

        // 총액 표시
        binding.totalPriceText.text = "총액: ${totalPrice}원"
    }

    private fun setupButtons() {
        // 주문 취소 버튼
        binding.cancelButton.setOnClickListener {
            finish()  // 이전 화면(배송 정보)으로 돌아감
        }

        // 주문 완료 버튼
        binding.orderButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("도서 상품 주문 완료")
                .setMessage("주문해 주셔서 감사합니다.")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    // 주문 완료 후 메인 화면으로 이동하고 스택 비우기
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}