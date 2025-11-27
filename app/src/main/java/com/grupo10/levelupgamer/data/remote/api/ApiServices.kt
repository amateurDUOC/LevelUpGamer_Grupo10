package com.grupo10.levelupgamer.data.remote.api

import com.grupo10.levelupgamer.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>

    @GET("auth/me")
    suspend fun getCurrentUser(): Response<ApiResponse<UserWrapper>>

    @PUT("auth/update-address")
    suspend fun updateAddress(@Body request: UpdateAddressRequest): Response<ApiResponse<UserWrapper>>
}

data class UserWrapper(val user: UserDto)

interface ProductApiService {
    @GET("products")
    suspend fun getAllProducts(
        @Query("category") category: String? = null,
        @Query("search") search: String? = null,
        @Query("hasDiscount") hasDiscount: Boolean? = null
    ): Response<ProductsResponse>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ApiResponse<ProductDto>>
}

interface StoreApiService {
    @GET("stores")
    suspend fun getAllStores(): Response<StoresResponse>

    @GET("stores/nearest")
    suspend fun getNearestStore(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<ApiResponse<StoreDto>>
}

interface CartApiService {
    @GET("cart")
    suspend fun getCart(): Response<CartResponse>

    @POST("cart")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<ApiResponse<CartItemDto>>

    @PUT("cart/{id}")
    suspend fun updateCartItem(
        @Path("id") id: String,
        @Body request: UpdateCartItemRequest
    ): Response<ApiResponse<CartItemDto>>

    @DELETE("cart/{id}")
    suspend fun removeFromCart(@Path("id") id: String): Response<ApiResponse<Unit>>

    @DELETE("cart")
    suspend fun clearCart(): Response<ApiResponse<Unit>>
}

interface GeocodingApiService {
    @GET("search")
    suspend fun searchAddress(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("limit") limit: Int = 1
    ): Response<List<NominatimResponse>>
}

