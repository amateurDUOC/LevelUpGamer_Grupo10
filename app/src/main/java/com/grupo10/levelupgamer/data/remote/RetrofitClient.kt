package com.grupo10.levelupgamer.data.remote

import android.content.Context
import com.grupo10.levelupgamer.BuildConfig
import com.grupo10.levelupgamer.data.remote.api.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val TIMEOUT_SECONDS = 30L

    // Token Manager
    private var authToken: String? = null

    fun setAuthToken(token: String?) {
        authToken = token
    }

    fun getAuthToken(): String? = authToken

    fun clearAuthToken() {
        authToken = null
    }

    // Logging Interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    // Auth Interceptor
    private val authInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        authToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        chain.proceed(requestBuilder.build())
    }

    // OkHttp Client para Backend
    private val backendOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    // OkHttp Client para Nominatim (sin auth)
    private val nominatimOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("User-Agent", "LevelUpGamer/1.0")
                .build()
            chain.proceed(request)
        }
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    // Retrofit para Backend
    private val backendRetrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(backendOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Retrofit para Nominatim (Geocoding)
    private val nominatimRetrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.NOMINATIM_URL)
        .client(nominatimOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // API Services
    val authApi: AuthApiService by lazy {
        backendRetrofit.create(AuthApiService::class.java)
    }

    val productApi: ProductApiService by lazy {
        backendRetrofit.create(ProductApiService::class.java)
    }

    val storeApi: StoreApiService by lazy {
        backendRetrofit.create(StoreApiService::class.java)
    }

    val cartApi: CartApiService by lazy {
        backendRetrofit.create(CartApiService::class.java)
    }

    val geocodingApi: GeocodingApiService by lazy {
        nominatimRetrofit.create(GeocodingApiService::class.java)
    }
}

