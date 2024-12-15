package com.example.chap2

data class CartItem(
    val bookId: String,
    val title: String,
    val price: Int,
    val imageResId: Int,
    var isSelected: Boolean = true
)

object CartManager {
    private val cartItems = mutableListOf<CartItem>()
    private val listeners = mutableListOf<(Int) -> Unit>()

    fun addItem(bookId: String, title: String, price: Int, imageResId: Int) {
        // 이미 있는 아이템이면 추가하지 않음
        if (cartItems.none { it.bookId == bookId }) {
            cartItems.add(CartItem(bookId, title, price, imageResId))
            notifyListeners()
        }
    }

    fun removeItem(bookId: String) {
        cartItems.removeAll { it.bookId == bookId }
        notifyListeners()
    }

    fun getItems(): List<CartItem> = cartItems

    fun getItemCount(): Int = cartItems.size

    fun getTotalPrice(): Int = cartItems.filter { it.isSelected }.sumOf { it.price }

    fun addListener(listener: (Int) -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: (Int) -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it(getItemCount()) }
    }
}

