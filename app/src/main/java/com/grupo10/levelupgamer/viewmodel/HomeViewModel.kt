package com.grupo10.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo10.levelupgamer.data.mapper.toDomain
import com.grupo10.levelupgamer.data.repository.ProductRepository
import com.grupo10.levelupgamer.data.repository.ProductResult
import com.grupo10.levelupgamer.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUIState(
    val searchQuery: String = "",
    val selectedProduct: Product? = null,
    val allProducts: List<Product> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    val productsOnSale: List<Product> = emptyList(),
    val notificationCount: Int = 3,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isNetworkError: Boolean = false
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    private val productRepository = ProductRepository()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                isNetworkError = false
            )

            when (val result = productRepository.getAllProducts()) {
                is ProductResult.Success -> {
                    val products = result.products.toDomain()
                    _uiState.value = _uiState.value.copy(
                        allProducts = products,
                        filteredProducts = products,
                        productsOnSale = products.filter { it.hasDiscount },
                        isLoading = false,
                        error = null,
                        isNetworkError = false
                    )
                }
                is ProductResult.NetworkError -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No se puede conectar al servidor. Verifica tu conexión a internet.",
                        isNetworkError = true
                    )
                }
                is ProductResult.ServerError -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error del servidor. Por favor, intenta más tarde.",
                        isNetworkError = false
                    )
                }
                is ProductResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error: ${result.message}",
                        isNetworkError = false
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            searchQuery = query,
            filteredProducts = if (query.isBlank()) {
                currentState.allProducts
            } else {
                currentState.allProducts.filter { product ->
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

