package com.grupo10.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import com.grupo10.levelupgamer.model.Product
import com.grupo10.levelupgamer.model.ProductsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUIState(
    val searchQuery: String = "",
    val selectedProduct: Product? = null,
    val filteredProducts: List<Product> = ProductsData.sampleProducts,
    val productsOnSale: List<Product> = ProductsData.sampleProducts.filter { it.hasDiscount },
    val notificationCount: Int = 3
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            filteredProducts = if (query.isBlank()) {
                ProductsData.sampleProducts
            } else {
                ProductsData.sampleProducts.filter { product ->
                    product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true) ||
                    product.category.name.contains(query, ignoreCase = true)
                }
            }
        )
    }

    fun selectProduct(product: Product?) {
        _uiState.value = _uiState.value.copy(selectedProduct = product)
    }

    fun clearNotifications() {
        _uiState.value = _uiState.value.copy(notificationCount = 0)
    }

    fun decrementNotificationCount() {
        val current = _uiState.value.notificationCount
        if (current > 0) {
            _uiState.value = _uiState.value.copy(notificationCount = current - 1)
        }
    }
}

