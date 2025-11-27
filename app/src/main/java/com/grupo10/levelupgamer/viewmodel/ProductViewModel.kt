package com.grupo10.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo10.levelupgamer.data.mapper.toDomain
import com.grupo10.levelupgamer.data.repository.ProductRepository
import com.grupo10.levelupgamer.data.repository.ProductResult
import com.grupo10.levelupgamer.model.Product
import com.grupo10.levelupgamer.model.ProductCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductUIState(
    val allProducts: List<Product> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: ProductCategory? = null,
    val selectedProduct: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isNetworkError: Boolean = false
)

class ProductViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProductUIState())
    val uiState: StateFlow<ProductUIState> = _uiState.asStateFlow()

    private val repository = ProductRepository()

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

            when (val result = repository.getAllProducts()) {
                is ProductResult.Success -> {
                    val products = result.products.toDomain()
                    _uiState.value = _uiState.value.copy(
                        allProducts = products,
                        filteredProducts = filterProducts(products, _uiState.value.searchQuery, _uiState.value.selectedCategory),
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

    fun searchProducts(query: String) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            searchQuery = query,
            filteredProducts = filterProducts(currentState.allProducts, query, currentState.selectedCategory)
        )
    }

    fun filterByCategory(category: ProductCategory?) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            selectedCategory = category,
            searchQuery = "", // Reset search
            filteredProducts = filterProducts(currentState.allProducts, "", category)
        )
    }

    fun selectProduct(product: Product?) {
        _uiState.value = _uiState.value.copy(selectedProduct = product)
    }

    fun getProductById(productId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = repository.getProductById(productId)) {
                is ProductResult.Success -> {
                    val product = result.products.firstOrNull()?.toDomain()
                    _uiState.value = _uiState.value.copy(
                        selectedProduct = product,
                        isLoading = false
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Producto no encontrado"
                    )
                }
            }
        }
    }

    private fun filterProducts(
        products: List<Product>,
        query: String,
        category: ProductCategory?
    ): List<Product> {
        var filtered = products

        // Filtrar por categoría
        if (category != null) {
            filtered = filtered.filter { it.category == category }
        }

        // Filtrar por búsqueda
        if (query.isNotBlank()) {
            filtered = filtered.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                product.description.contains(query, ignoreCase = true)
            }
        }

        return filtered
    }
}

