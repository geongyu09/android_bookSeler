package com.example.chap2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog  // 추가
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.chap2.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    "android.permission.POST_NOTIFICATIONS"
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf("android.permission.POST_NOTIFICATIONS"),
                    1
                )
            }
        }

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

    // 알림을 위한 코드
    companion object {
        private const val CHANNEL_ID = "order_notification"
        private const val NOTIFICATION_ID = 1
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "주문 알림"
            val descriptionText = "주문 관련 알림을 표시합니다"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showOrderCompleteNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("주문 완료")
            .setContentText("주문해 주셔서 감사합니다.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(
                        this@OrderActivity,
                        "android.permission.POST_NOTIFICATIONS"
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notify(NOTIFICATION_ID, builder.build())
                }
            } else {
                notify(NOTIFICATION_ID, builder.build())
            }
        }
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
            CartManager.clearCart()
            AlertDialog.Builder(this)
                .setTitle("도서 상품 주문 완료")
                .setMessage("주문해 주셔서 감사합니다.")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    // 알림 표시
                    createNotificationChannel()
                    showOrderCompleteNotification()
                    // 메인 화면으로 이동
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