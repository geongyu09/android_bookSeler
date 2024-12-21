package com.example.chap2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chap2.databinding.ActivityDeliveryInfoBinding

class DeliveryInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliveryInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "배송 정보"

        setupButtons()
    }

    private fun setupButtons() {
        // 취소 버튼
        binding.cancelButton.setOnClickListener {
            finish()  // 이전 화면(장바구니)으로 돌아감
        }

        // 등록 버튼
        binding.submitButton.setOnClickListener {
            // 입력값 검증
            if (validateInput()) {
                // 주문서 화면으로 이동
                val intent = Intent(this, OrderActivity::class.java).apply {
                    putExtra("name", binding.nameEdit.text.toString())
                    putExtra("deliveryDate", binding.deliveryDateEdit.text.toString())
                    putExtra("phone", binding.phoneEdit.text.toString())
                    putExtra("zipCode", binding.zipCodeEdit.text.toString())
                    putExtra("address", binding.addressEdit.text.toString())
                }
                startActivity(intent)
            }
        }
    }

    private fun validateInput(): Boolean {
        // 필수 입력값 검증
        when {
            binding.nameEdit.text.isBlank() -> {
                showToast("성명을 입력해주세요")
                return false
            }
            binding.deliveryDateEdit.text.isBlank() -> {
                showToast("배송일을 입력해주세요")
                return false
            }
            binding.phoneEdit.text.isBlank() -> {
                showToast("연락처를 입력해주세요")
                return false
            }
            binding.zipCodeEdit.text.isBlank() -> {
                showToast("우편번호를 입력해주세요")
                return false
            }
            binding.addressEdit.text.isBlank() -> {
                showToast("주소를 입력해주세요")
                return false
            }
            else -> return true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}