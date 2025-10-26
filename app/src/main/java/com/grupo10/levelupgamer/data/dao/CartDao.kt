package com.grupo10.levelupgamer.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.grupo10.levelupgamer.model.CartItem

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items WHERE userId = :userId ORDER BY id DESC")
    fun getCartItems(userId: Int): LiveData<List<CartItem>>

    @Query("SELECT SUM(productPrice * quantity) FROM cart_items WHERE userId = :userId")
    fun getCartTotal(userId: Int): LiveData<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItem)

    @Update
    suspend fun update(cartItem: CartItem)

    @Delete
    suspend fun delete(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: Int)

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId")
    suspend fun getCartItemByProduct(userId: Int, productId: Int): CartItem?
}

