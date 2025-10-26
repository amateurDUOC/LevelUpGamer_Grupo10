package com.grupo10.levelupgamer.data.repository

import androidx.lifecycle.LiveData
import com.grupo10.levelupgamer.data.dao.ProductDao
import com.grupo10.levelupgamer.model.Product
import com.grupo10.levelupgamer.model.ProductCategory

class ProductRepository(private val productDao: ProductDao) {

    fun getAllProducts(): LiveData<List<Product>> {
        return productDao.getAllProducts()
    }

    fun getProductsByCategory(category: ProductCategory): LiveData<List<Product>> {
        return productDao.getProductsByCategory(category)
    }

    fun searchProducts(query: String): LiveData<List<Product>> {
        return productDao.searchProducts(query)
    }

    fun getProductById(productId: Int): LiveData<Product> {
        return productDao.getProductById(productId)
    }

    fun getTopRatedProducts(limit: Int = 10): LiveData<List<Product>> {
        return productDao.getTopRatedProducts(limit)
    }

    suspend fun addProduct(product: Product) {
        productDao.insert(product)
    }

    suspend fun addProducts(products: List<Product>) {
        productDao.insertAll(products)
    }

    suspend fun updateProduct(product: Product) {
        productDao.update(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.delete(product)
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAll()
    }

    suspend fun updateStock(productId: Int, newStock: Int) {
        productDao.updateStock(productId, newStock)
    }
}

