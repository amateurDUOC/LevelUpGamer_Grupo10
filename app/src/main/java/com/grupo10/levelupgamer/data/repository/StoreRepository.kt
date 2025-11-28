package com.grupo10.levelupgamer.data.repository

import com.grupo10.levelupgamer.data.remote.RetrofitClient
import com.grupo10.levelupgamer.data.remote.api.StoreApiService
import com.grupo10.levelupgamer.data.remote.dto.StoreDto

sealed class StoreResult {
    data class Success(val stores: List<StoreDto>) : StoreResult()
    data class NearestStoreSuccess(val store: StoreDto) : StoreResult()
    object NetworkError : StoreResult()
    object ServerError : StoreResult()
    data class Error(val message: String) : StoreResult()
}

class StoreRepository(
    private val api: StoreApiService = RetrofitClient.storeApi
) {

    suspend fun getAllStores(): StoreResult {
        return try {
            val response = api.getAllStores()

            if (response.isSuccessful && response.body() != null) {
                val stores = response.body()!!.data
                StoreResult.Success(stores)
            } else {
                StoreResult.ServerError
            }
        } catch (e: java.net.UnknownHostException) {
            StoreResult.NetworkError
        } catch (e: java.net.ConnectException) {
            StoreResult.NetworkError
        } catch (e: java.net.SocketTimeoutException) {
            StoreResult.NetworkError
        } catch (e: Exception) {
            StoreResult.Error(e.message ?: "Error desconocido")
        }
    }

    suspend fun getNearestStore(latitude: Double, longitude: Double): StoreResult {
        return try {
            val response = api.getNearestStore(latitude, longitude)

            if (response.isSuccessful && response.body()?.success == true && response.body()?.data != null) {
                val store = response.body()!!.data!!
                StoreResult.NearestStoreSuccess(store)
            } else {
                StoreResult.ServerError
            }
        } catch (e: java.net.UnknownHostException) {
            StoreResult.NetworkError
        } catch (e: java.net.ConnectException) {
            StoreResult.NetworkError
        } catch (e: java.net.SocketTimeoutException) {
            StoreResult.NetworkError
        } catch (e: Exception) {
            StoreResult.Error(e.message ?: "Error desconocido")
        }
    }
}

