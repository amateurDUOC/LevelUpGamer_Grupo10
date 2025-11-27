package com.grupo10.levelupgamer.data.remote.dto

import com.google.gson.annotations.SerializedName

// Response genérico de la API
data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null,
    val errors: List<ValidationError>? = null
)

data class ValidationError(
    val msg: String,
    val param: String? = null
)

// Auth DTOs
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

data class AuthResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val role: String
)

data class UpdateAddressRequest(
    val address: String,
    val latitude: Double,
    val longitude: Double
)

// Product DTOs
data class ProductDto(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val discount: Int,
    val hasDiscount: Boolean,
    val finalPrice: Double,
    val stock: Int,
    val category: CategoryDto,
    val imageUrl: String,
    val rating: Double
)

data class CategoryDto(
    val id: Int,
    val name: String
)

data class ProductsResponse(
    val count: Int,
    val data: List<ProductDto>
)

// Store DTOs
data class StoreDto(
    val id: Int,
    val name: String,
    val address: String,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val phone: String,
    val hours: String,
    val distance: Double? = null
)

data class StoresResponse(
    val count: Int,
    val data: List<StoreDto>
)

// Cart DTOs
data class CartItemDto(
    @SerializedName("_id") val id: String,
    val userId: String,
    val productId: Int,
    val productName: String,
    val productPrice: Double,
    val productImage: String,
    val quantity: Int
)

data class CartResponse(
    val count: Int,
    val total: Double,
    val data: List<CartItemDto>
)

data class AddToCartRequest(
    val productId: Int,
    val productName: String,
    val productPrice: Double,
    val productImage: String,
    val quantity: Int = 1
)

data class UpdateCartItemRequest(
    val quantity: Int
)

// Nominatim (Geocoding) DTOs
data class NominatimResponse(
    val lat: String,
    val lon: String,
    @SerializedName("display_name") val displayName: String,
    val address: NominatimAddress? = null
)

data class NominatimAddress(
    val road: String? = null,
    val house_number: String? = null,
    val city: String? = null,
    val town: String? = null,
    val village: String? = null,
    val state: String? = null,
    val country: String? = null
)

