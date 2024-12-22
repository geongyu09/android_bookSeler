package com.example.chap2

import android.app.DatePickerDialog
import java.util.Calendar
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chap2.databinding.ActivityDeliveryInfoBinding

class DeliveryInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliveryInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "배송 정보"

        // 배송일 EditText 클릭 시 DatePicker 표시
        binding.deliveryDateEdit.setOnClickListener {
            showDatePicker()
        }

        // 폰 번호 입력시 하이픈 추가
        binding.phoneEdit.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            private var deletingHyphen = false
            private var hyphenStart = 0
            private val maxLength = 13 // 하이픈 포함 최대 길이

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (!isFormatting) {
                    deletingHyphen = count == 1 && s?.get(start) == '-'
                    hyphenStart = start
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isFormatting) {
                    isFormatting = true

                    val digits = s.toString().replace("-", "")
                    var formatted = ""

                    when {
                        digits.length >= 3 -> formatted += digits.substring(0, 3) + "-"
                        else -> formatted += digits
                    }

                    if (digits.length > 3) {
                        when {
                            digits.length >= 7 -> {
                                formatted += digits.substring(3, 7) + "-"
                                if (digits.length > 7) {
                                    formatted += digits.substring(7)
                                }
                            }
                            else -> formatted += digits.substring(3)
                        }
                    }

                    if (formatted != s.toString()) {
                        s?.replace(0, s.length, formatted)
                    }

                    isFormatting = false
                }
            }
        })

        // 키보드 입력 방지
        binding.deliveryDateEdit.isFocusable = false

        setupButtons()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            this,
            { _, year, month, day ->
                // 선택된 날짜를 EditText에 설정
                val selectedDate = String.format("%d-%02d-%02d", year, month + 1, day)
                binding.deliveryDateEdit.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            // 오늘 이후의 날짜만 선택 가능하도록 설정
            datePicker.minDate = System.currentTimeMillis() - 1000
            show()
        }
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