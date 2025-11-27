package com.grupo10.levelupgamer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.grupo10.levelupgamer.data.database.LevelUpDatabase
import com.grupo10.levelupgamer.data.repository.CartRepository
import com.grupo10.levelupgamer.model.CartItem
import com.grupo10.levelupgamer.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CartRepository
    private val _currentUserId = MutableLiveData<Int?>()
    private val _addToCartResult = MutableLiveData<String>()
    val addToCartResult: LiveData<String> = _addToCartResult

    private val _showCheckoutDialog = MutableStateFlow(false)
    val showCheckoutDialog: StateFlow<Boolean> = _showCheckoutDialog.asStateFlow()

    init {
        val cartDao = LevelUpDatabase.getDatabase(application).cartDao()
        repository = CartRepository(cartDao)
    }

    fun setCurrentUser(userId: Int) {
        _currentUserId.value = userId
    }

    fun getCartItems(): LiveData<List<CartItem>>? {
        return _currentUserId.value?.let { userId ->
            repository.getCartItems(userId)
        }
    }

    fun getCartTotal(): LiveData<Double?>? {
        return _currentUserId.value?.let { userId ->
            repository.getCartTotal(userId)
        }
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        _currentUserId.value?.let { userId ->
            viewModelScope.launch {
                try {
                    val cartItem = CartItem(
                        productId = product.id,
                        productName = product.name,
                        productPrice = product.price,
                        productImage = product.imageUrl,
                        quantity = quantity,
                        userId = userId
                    )
                    repository.addToCart(cartItem)
                    _addToCartResult.value = "Producto agregado al carrito"
                } catch (e: Exception) {
                    _addToCartResult.value = "Error al agregar al carrito"
                }
            }
        }
    }

    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity > 0) {
                repository.updateCartItem(cartItem.copy(quantity = newQuantity))
            } else {
                repository.removeFromCart(cartItem)
            }
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            repository.removeFromCart(cartItem)
        }
    }

    fun clearCart() {
        _currentUserId.value?.let { userId ->
            viewModelScope.launch {
                repository.clearCart(userId)
            }
        }
    }

    fun showCheckoutDialog() {
        _showCheckoutDialog.value = true
    }

    fun hideCheckoutDialog() {
        _showCheckoutDialog.value = false
    }

    fun processCheckout() {
        viewModelScope.launch {
            // TODO: Implementar lógica de compra (procesar pago, crear orden, etc.)
            clearCart()
            hideCheckoutDialog()
        }
    }

    fun clearCartOnLogout() {
        viewModelScope.launch {
            _currentUserId.value?.let { userId ->
                repository.clearCart(userId)
            }
        }
        _currentUserId.value = null
    }
}

