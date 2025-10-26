package com.grupo10.levelupgamer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.grupo10.levelupgamer.data.database.LevelUpDatabase
import com.grupo10.levelupgamer.data.repository.ProductRepository
import com.grupo10.levelupgamer.model.Product
import com.grupo10.levelupgamer.model.ProductCategory


class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository
    private val _searchQuery = MutableLiveData<String>("")
    private val _selectedCategory = MutableLiveData<ProductCategory?>()

    init {
        val productDao = LevelUpDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDao)
    }

    val allProducts: LiveData<List<Product>> = repository.getAllProducts()

    val filteredProducts: LiveData<List<Product>> = _searchQuery.switchMap { query ->
        if (query.isNullOrBlank()) {
            _selectedCategory.value?.let { category ->
                repository.getProductsByCategory(category)
            } ?: allProducts
        } else {
            repository.searchProducts(query)
        }
    }

    fun searchProducts(query: String) {
        _searchQuery.value = query
    }

    fun filterByCategory(category: ProductCategory?) {
        _selectedCategory.value = category
        _searchQuery.value = "" // Reset search
    }

    fun getProductById(productId: Int): LiveData<Product> {
        return repository.getProductById(productId)
    }
}

