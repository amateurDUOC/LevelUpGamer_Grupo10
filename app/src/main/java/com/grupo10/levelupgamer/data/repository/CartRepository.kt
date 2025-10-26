package com.grupo10.levelupgamer.data.repository

import androidx.lifecycle.LiveData
import com.grupo10.levelupgamer.data.dao.CartDao
import com.grupo10.levelupgamer.model.CartItem

class CartRepository(private val cartDao: CartDao) {

    fun getCartItems(userId: Int): LiveData<List<CartItem>> {
        return cartDao.getCartItems(userId)
    }

    fun getCartTotal(userId: Int): LiveData<Double?> {
        return cartDao.getCartTotal(userId)
    }

    suspend fun addToCart(cartItem: CartItem) {
        // Verificar si el producto ya existe en el carrito
        val existingItem = cartDao.getCartItemByProduct(cartItem.userId, cartItem.productId)
        if (existingItem != null) {
            // Si existe, actualizar la cantidad
            val updatedItem = existingItem.copy(
                quantity = existingItem.quantity + cartItem.quantity
            )
            cartDao.update(updatedItem)
        } else {
            // Si no existe, insertar nuevo item
            cartDao.insert(cartItem)
        }
    }

    suspend fun updateCartItem(cartItem: CartItem) {
        cartDao.update(cartItem)
    }

    suspend fun removeFromCart(cartItem: CartItem) {
        cartDao.delete(cartItem)
    }

    suspend fun clearCart(userId: Int) {
        cartDao.clearCart(userId)
    }
}

