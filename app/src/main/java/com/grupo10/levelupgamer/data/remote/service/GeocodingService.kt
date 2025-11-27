package com.grupo10.levelupgamer.data.remote.service

import com.grupo10.levelupgamer.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class LocationResult(
    val latitude: Double,
    val longitude: Double,
    val formattedAddress: String
)

data class AddressSuggestion(
    val displayName: String,
    val latitude: Double,
    val longitude: Double
)

class GeocodingService {

    /**
     * Obtiene sugerencias de direcciones mientras el usuario escribe
     * @param query Texto ingresado por el usuario (ej: "Alvarez")
     * @return Lista de sugerencias de direcciones
     */
    suspend fun getAddressSuggestions(query: String): Result<List<AddressSuggestion>> = withContext(Dispatchers.IO) {
        try {
            if (query.length < 3) {
                return@withContext Result.success(emptyList())
            }

            // Agregar ", Chile" para mejores resultados
            val searchQuery = if (query.contains("Chile", ignoreCase = true)) {
                query
            } else {
                "$query, Chile"
            }

            val response = RetrofitClient.geocodingApi.searchAddress(
                query = searchQuery,
                format = "json",
                addressDetails = 1,
                limit = 5 // Máximo 5 sugerencias
            )

            if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                val suggestions = response.body()!!.map { location ->
                    AddressSuggestion(
                        displayName = location.displayName,
                        latitude = location.lat.toDouble(),
                        longitude = location.lon.toDouble()
                    )
                }
                Result.success(suggestions)
            } else {
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error al buscar direcciones: ${e.message}"))
        }
    }

    /**
     * Obtiene las coordenadas de una dirección usando Nominatim (OpenStreetMap)
     * @param address Dirección a geocodificar (ej: "Alvarez 1130, Viña del Mar, Chile")
     * @return LocationResult con latitud, longitud y dirección formateada
     */
    suspend fun getCoordinatesFromAddress(address: String): Result<LocationResult> = withContext(Dispatchers.IO) {
        try {
            // Agregar ", Chile" si no está presente para mejor precisión
            val fullAddress = if (address.contains("Chile", ignoreCase = true)) {
                address
            } else {
                "$address, Chile"
            }

            val response = RetrofitClient.geocodingApi.searchAddress(
                query = fullAddress,
                format = "json",
                addressDetails = 1,
                limit = 1
            )

            if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                val location = response.body()!!.first()
                Result.success(
                    LocationResult(
                        latitude = location.lat.toDouble(),
                        longitude = location.lon.toDouble(),
                        formattedAddress = location.displayName
                    )
                )
            } else {
                Result.failure(Exception("No se encontraron coordenadas para la dirección"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error al geocodificar: ${e.message}"))
        }
    }

    /**
     * Valida y formatea una dirección antes de geocodificarla
     */
    fun formatAddress(street: String, city: String, country: String = "Chile"): String {
        return "$street, $city, $country"
    }

    /**
     * Obtiene coordenadas por defecto si falla la geocodificación
     * Retorna coordenadas de Viña del Mar centro
     */
    fun getDefaultCoordinates(): LocationResult {
        return LocationResult(
            latitude = -33.0245,
            longitude = -71.5516,
            formattedAddress = "Viña del Mar, Chile"
        )
    }
}

