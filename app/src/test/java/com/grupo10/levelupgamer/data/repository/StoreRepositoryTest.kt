package com.grupo10.levelupgamer.data.repository

import com.grupo10.levelupgamer.data.remote.api.StoreApiService
import com.grupo10.levelupgamer.data.remote.dto.ApiResponse
import com.grupo10.levelupgamer.data.remote.dto.StoreDto
import com.grupo10.levelupgamer.data.remote.dto.StoresResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class StoreRepositoryTest {

    private lateinit var repository: StoreRepository
    private lateinit var storeApi: StoreApiService

    @Before
    fun setup() {
        storeApi = mockk()
        repository = StoreRepository(storeApi)
    }


    @Test
    fun `obtener todas las tiendas exitosamente devuelve Success`() = runTest {
        val storesDto = listOf(
            StoreDto(1, "Tienda 1", "Dirección 1", "Ciudad 1", -33.0, -71.5, "123456", "9-18", null)
        )
        val response = Response.success(StoresResponse(count = 1, data = storesDto))

        coEvery { storeApi.getAllStores() } returns response

        val result = repository.getAllStores()

        assertTrue(result is StoreResult.Success)
        assertEquals(1, (result as StoreResult.Success).stores.size)
        assertEquals("Tienda 1", result.stores[0].name)
    }

    @Test
    fun `obtener todas las tiendas con error del servidor devuelve ServerError`() = runTest {
        val response = Response.error<StoresResponse>(500, "".toResponseBody())

        coEvery { storeApi.getAllStores() } returns response

        val result = repository.getAllStores()

        assertTrue(result is StoreResult.ServerError)
    }

    @Test
    fun `obtener todas las tiendas con UnknownHostException devuelve NetworkError`() = runTest {
        coEvery { storeApi.getAllStores() } throws UnknownHostException()

        val result = repository.getAllStores()

        assertTrue(result is StoreResult.NetworkError)
    }

    @Test
    fun `obtener todas las tiendas con ConnectException devuelve NetworkError`() = runTest {
        coEvery { storeApi.getAllStores() } throws ConnectException()

        val result = repository.getAllStores()

        assertTrue(result is StoreResult.NetworkError)
    }

    @Test
    fun `obtener todas las tiendas con SocketTimeoutException devuelve NetworkError`() = runTest {
        coEvery { storeApi.getAllStores() } throws SocketTimeoutException()

        val result = repository.getAllStores()

        assertTrue(result is StoreResult.NetworkError)
    }

    @Test
    fun `obtener todas las tiendas con excepcion generica devuelve Error`() = runTest {
        coEvery { storeApi.getAllStores() } throws RuntimeException("Error inesperado")

        val result = repository.getAllStores()

        assertTrue(result is StoreResult.Error)
        assertEquals("Error inesperado", (result as StoreResult.Error).message)
    }

    @Test
    fun `obtener tienda mas cercana exitosamente devuelve NearestStoreSuccess`() = runTest {
        val storeDto = StoreDto(
            1, "Tienda Cercana", "Dirección", "Ciudad", -33.0, -71.5, "123456", "9-18", 0.5
        )
        val response = Response.success(ApiResponse(success = true, data = storeDto))

        coEvery { storeApi.getNearestStore(-33.0, -71.5) } returns response

        val result = repository.getNearestStore(-33.0, -71.5)

        assertTrue(result is StoreResult.NearestStoreSuccess)
        assertEquals("Tienda Cercana", (result as StoreResult.NearestStoreSuccess).store.name)
        assertEquals(0.5, result.store.distance)
    }

    @Test
    fun `obtener tienda mas cercana con error del servidor devuelve ServerError`() = runTest {
        val response = Response.error<ApiResponse<StoreDto>>(500, "".toResponseBody())

        coEvery { storeApi.getNearestStore(any(), any()) } returns response

        val result = repository.getNearestStore(-33.0, -71.5)

        assertTrue(result is StoreResult.ServerError)
    }

    @Test
    fun `obtener tienda mas cercana con success false devuelve ServerError`() = runTest {
        val response = Response.success(ApiResponse<StoreDto>(success = false, data = null))

        coEvery { storeApi.getNearestStore(any(), any()) } returns response

        val result = repository.getNearestStore(-33.0, -71.5)

        assertTrue(result is StoreResult.ServerError)
    }

    @Test
    fun `obtener tienda mas cercana con UnknownHostException devuelve NetworkError`() = runTest {
        coEvery { storeApi.getNearestStore(any(), any()) } throws UnknownHostException()

        val result = repository.getNearestStore(-33.0, -71.5)

        assertTrue(result is StoreResult.NetworkError)
    }

    @Test
    fun `obtener tienda mas cercana con ConnectException devuelve NetworkError`() = runTest {
        coEvery { storeApi.getNearestStore(any(), any()) } throws ConnectException()

        val result = repository.getNearestStore(-33.0, -71.5)

        assertTrue(result is StoreResult.NetworkError)
    }

    @Test
    fun `obtener tienda mas cercana con SocketTimeoutException devuelve NetworkError`() = runTest {
        coEvery { storeApi.getNearestStore(any(), any()) } throws SocketTimeoutException()

        val result = repository.getNearestStore(-33.0, -71.5)

        assertTrue(result is StoreResult.NetworkError)
    }

    @Test
    fun `obtener tienda mas cercana con excepcion generica devuelve Error`() = runTest {
        coEvery { storeApi.getNearestStore(any(), any()) } throws RuntimeException("Error custom")

        val result = repository.getNearestStore(-33.0, -71.5)

        assertTrue(result is StoreResult.Error)
        assertEquals("Error custom", (result as StoreResult.Error).message)
    }

    @Test
    fun `obtener todas las tiendas con body nulo devuelve ServerError`() = runTest {
        val response = Response.success<StoresResponse>(null)

        coEvery { storeApi.getAllStores() } returns response

        val result = repository.getAllStores()

        assertTrue(result is StoreResult.ServerError)
    }

    @Test
    fun `obtener tienda mas cercana con data nulo devuelve ServerError`() = runTest {
        val response = Response.success(ApiResponse<StoreDto>(success = true, data = null))

        coEvery { storeApi.getNearestStore(any(), any()) } returns response

        val result = repository.getNearestStore(-33.0, -71.5)

        assertTrue(result is StoreResult.ServerError)
    }
}

