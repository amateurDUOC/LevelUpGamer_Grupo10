package com.grupo10.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import com.grupo10.levelupgamer.model.Store
import com.grupo10.levelupgamer.model.StoresData
import com.grupo10.levelupgamer.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import kotlin.math.*

data class StoreUIState(
    val nearestStore: Store? = null,
    val allStores: List<Store> = StoresData.stores,
    val distanceToNearest: Double = 0.0
)

class StoreViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StoreUIState())
    val uiState: StateFlow<StoreUIState> = _uiState.asStateFlow()

    fun updateNearestStore(user: User?) {
        if (user == null || (user.latitude == 0.0 && user.longitude == 0.0)) {
            _uiState.value = StoreUIState()
            return
        }

        val storesWithDistance = StoresData.stores.map { store ->
            val distance = calculateDistance(
                user.latitude, user.longitude,
                store.latitude, store.longitude
            )
            Pair(store, distance)
        }

        val nearest = storesWithDistance.minByOrNull { it.second }

        nearest?.let {
            _uiState.value = StoreUIState(
                nearestStore = it.first,
                allStores = StoresData.stores,
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

