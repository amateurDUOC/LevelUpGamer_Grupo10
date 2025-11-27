package com.grupo10.levelupgamer.data.repository

import com.grupo10.levelupgamer.data.remote.RetrofitClient
import com.grupo10.levelupgamer.data.remote.dto.ProductDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class ProductResult {
    data class Success(val products: List<ProductDto>) : ProductResult()
    data class Error(val message: String) : ProductResult()
    object NetworkError : ProductResult()
    object ServerError : ProductResult()
}

class ProductRepository {

    suspend fun getAllProducts(
        category: String? = null,
        search: String? = null,
        hasDiscount: Boolean? = null
    ): ProductResult = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.productApi.getAllProducts(
                category = category,
                search = search,
                hasDiscount = hasDiscount
            )

            if (response.isSuccessful && response.body() != null) {
                val products = response.body()!!.data
                ProductResult.Success(products)
            } else {
                ProductResult.ServerError
            }
        } catch (e: java.net.UnknownHostException) {
            ProductResult.NetworkError
        } catch (e: java.net.ConnectException) {
            ProductResult.NetworkError
        } catch (e: java.net.SocketTimeoutException) {
            ProductResult.NetworkError
        } catch (e: Exception) {
            ProductResult.Error(e.message ?: "Error desconocido")
        }
    }

    suspend fun getProductById(id: Int): ProductResult = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.productApi.getProductById(id)

            if (response.isSuccessful && response.body()?.data != null) {
                val product = response.body()!!.data!!
                ProductResult.Success(listOf(product))
            } else {
                ProductResult.ServerError
            }
        } catch (e: java.net.UnknownHostException) {
            ProductResult.NetworkError
        } catch (e: java.net.ConnectException) {
            ProductResult.NetworkError
        } catch (e: java.net.SocketTimeoutException) {
            ProductResult.NetworkError
        } catch (e: Exception) {
            ProductResult.Error(e.message ?: "Error desconocido")
        }
    }

    suspend fun getProductsOnSale(): ProductResult = getAllProducts(hasDiscount = true)
}

