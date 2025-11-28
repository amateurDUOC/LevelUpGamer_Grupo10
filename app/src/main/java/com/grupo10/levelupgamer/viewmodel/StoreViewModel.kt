package com.grupo10.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo10.levelupgamer.data.mapper.toDomain
import com.grupo10.levelupgamer.data.repository.StoreRepository
import com.grupo10.levelupgamer.data.repository.StoreResult
import com.grupo10.levelupgamer.model.Store
import com.grupo10.levelupgamer.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.*

data class StoreUIState(
    val nearestStore: Store? = null,
    val allStores: List<Store> = emptyList(),
    val distanceToNearest: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class StoreViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StoreUIState())
    val uiState: StateFlow<StoreUIState> = _uiState.asStateFlow()

    private val repository = StoreRepository()

    init {
        loadStores()
    }

    fun loadStores() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = repository.getAllStores()) {
                is StoreResult.Success -> {
                    val stores = result.stores.toDomain()
                    _uiState.value = _uiState.value.copy(
                        allStores = stores,
                        isLoading = false,
                        error = null
                    )
                }
                is StoreResult.NetworkError -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error de conexión con el servidor"
                    )
                }
                is StoreResult.ServerError -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error del servidor"
                    )
                }
                is StoreResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> {}
            }
        }
    }

    fun updateNearestStore(user: User?) {
        if (user == null || user.latitude == null || user.longitude == null) {
            return
        }

        viewModelScope.launch {
            when (val result = repository.getNearestStore(user.latitude, user.longitude)) {
                is StoreResult.NearestStoreSuccess -> {
                    val store = result.store.toDomain()
                    val distance = result.store.distance ?: 0.0
                    _uiState.value = _uiState.value.copy(
                        nearestStore = store,
                        distanceToNearest = distance
                    )
                }
                else -> {
                    // Fallback: calcular manualmente si falla el backend
                    calculateNearestStoreManually(user)
                }
            }
        }
    }

    private fun calculateNearestStoreManually(user: User) {
        val stores = _uiState.value.allStores
        if (stores.isEmpty() || user.latitude == null || user.longitude == null) return

        val storesWithDistance = stores.map { store ->
            val distance = calculateDistance(
                user.latitude, user.longitude,
                store.latitude, store.longitude
            )
            Pair(store, distance)
        }

        val nearest = storesWithDistance.minByOrNull { it.second }

        nearest?.let {
            _uiState.value = _uiState.value.copy(
                nearestStore = it.first,
                distanceToNearest = it.second
            )
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        // Fórmula de Haversine para calcular distancia entre dos puntos geográficos
        val earthRadius = 6371.0 // Radio de la Tierra en km

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    fun getFormattedDistance(distance: Double): String {
        return if (distance < 1.0) {
            "${(distance * 1000).toInt()} m"
        } else {
            String.format(Locale.getDefault(), "%.1f km", distance)
        }
    }
}

