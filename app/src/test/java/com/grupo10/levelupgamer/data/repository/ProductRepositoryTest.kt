package com.grupo10.levelupgamer.data.repository

import com.grupo10.levelupgamer.data.remote.api.ProductApiService
import com.grupo10.levelupgamer.data.remote.dto.ApiResponse
import com.grupo10.levelupgamer.data.remote.dto.CategoryDto
import com.grupo10.levelupgamer.data.remote.dto.ProductDto
import com.grupo10.levelupgamer.data.remote.dto.ProductsResponse
import io.mockk.*
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ProductRepositoryTest {

    private lateinit var repository: ProductRepository
    private lateinit var productApi: ProductApiService

    @Before
    fun setup() {
        productApi = mockk()
        repository = ProductRepository(productApi)
    }


    private fun createProductDto(
        id: Int,
        name: String,
        price: Double,
        categoryName: String,
        discount: Int = 0
    ) = ProductDto(
        id = id,
        name = name,
        price = price,
        description = "Descripcion",
        category = CategoryDto(1, categoryName),
        imageUrl = "",
        stock = 10,
        discount = discount,
        hasDiscount = discount > 0,
        finalPrice = if (discount > 0) price - (price * discount / 100) else price,
        rating = 4.5
    )

    @Test
    fun `obtener todos los productos exitosamente devuelve Success`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "Producto 1", 100.0, "CONSOLAS", 0)
        )
        val response = Response.success(ProductsResponse(count = 1, data = productsDto))

        coEvery { productApi.getAllProducts(null, null, null) } returns response

        val result = repository.getAllProducts()

        assertTrue(result is ProductResult.Success)
        assertEquals(1, (result as ProductResult.Success).products.size)
        assertEquals("Producto 1", result.products[0].name)
    }

    @Test
    fun `obtener productos con filtro de categoria devuelve productos filtrados`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "Producto 1", 100.0, "CONSOLAS", 0)
        )
        val response = Response.success(ProductsResponse(count = 1, data = productsDto))

        coEvery { productApi.getAllProducts("CONSOLAS", null, null) } returns response

        val result = repository.getAllProducts(category = "CONSOLAS")

        assertTrue(result is ProductResult.Success)
        coVerify { productApi.getAllProducts("CONSOLAS", null, null) }
    }

    @Test
    fun `obtener productos con filtro de busqueda devuelve productos filtrados`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "PlayStation", 100.0, "CONSOLAS", 0)
        )
        val response = Response.success(ProductsResponse(count = 1, data = productsDto))

        coEvery { productApi.getAllProducts(null, "PlayStation", null) } returns response

        val result = repository.getAllProducts(search = "PlayStation")

        assertTrue(result is ProductResult.Success)
        coVerify { productApi.getAllProducts(null, "PlayStation", null) }
    }

    @Test
    fun `obtener productos con filtro de descuento devuelve productos en oferta`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "Producto Oferta", 100.0, "CONSOLAS", 20)
        )
        val response = Response.success(ProductsResponse(count = 1, data = productsDto))

        coEvery { productApi.getAllProducts(null, null, true) } returns response

        val result = repository.getAllProducts(hasDiscount = true)

        assertTrue(result is ProductResult.Success)
        coVerify { productApi.getAllProducts(null, null, true) }
    }

    @Test
    fun `obtener productos con error del servidor devuelve ServerError`() = runTest {
        val response = Response.error<ProductsResponse>(500, "".toResponseBody())

        coEvery { productApi.getAllProducts(any(), any(), any()) } returns response

        val result = repository.getAllProducts()

        assertTrue(result is ProductResult.ServerError)
    }

    @Test
    fun `obtener productos con UnknownHostException devuelve NetworkError`() = runTest {
        coEvery { productApi.getAllProducts(any(), any(), any()) } throws UnknownHostException()

        val result = repository.getAllProducts()

        assertTrue(result is ProductResult.NetworkError)
    }

    @Test
    fun `obtener productos con ConnectException devuelve NetworkError`() = runTest {
        coEvery { productApi.getAllProducts(any(), any(), any()) } throws ConnectException()

        val result = repository.getAllProducts()

        assertTrue(result is ProductResult.NetworkError)
    }

    @Test
    fun `obtener productos con SocketTimeoutException devuelve NetworkError`() = runTest {
        coEvery { productApi.getAllProducts(any(), any(), any()) } throws SocketTimeoutException()

        val result = repository.getAllProducts()

        assertTrue(result is ProductResult.NetworkError)
    }

    @Test
    fun `obtener productos con excepcion generica devuelve Error`() = runTest {
        coEvery { productApi.getAllProducts(any(), any(), any()) } throws RuntimeException("Error custom")

        val result = repository.getAllProducts()

        assertTrue(result is ProductResult.Error)
        assertEquals("Error custom", (result as ProductResult.Error).message)
    }

    @Test
    fun `obtener producto por id exitosamente devuelve Success`() = runTest {
        val productDto = createProductDto(1, "Producto 1", 100.0, "CONSOLAS", 0)
        val response = Response.success(ApiResponse(success = true, data = productDto))

        coEvery { productApi.getProductById(1) } returns response

        val result = repository.getProductById(1)

        assertTrue(result is ProductResult.Success)
        assertEquals("Producto 1", (result as ProductResult.Success).products[0].name)
    }

    @Test
    fun `obtener producto por id con error del servidor devuelve ServerError`() = runTest {
        val response = Response.error<ApiResponse<ProductDto>>(500, "".toResponseBody())

        coEvery { productApi.getProductById(any()) } returns response

        val result = repository.getProductById(1)

        assertTrue(result is ProductResult.ServerError)
    }

    @Test
    fun `obtener producto por id con data nulo devuelve ServerError`() = runTest {
        val response = Response.success(ApiResponse<ProductDto>(success = true, data = null))

        coEvery { productApi.getProductById(any()) } returns response

        val result = repository.getProductById(1)

        assertTrue(result is ProductResult.ServerError)
    }

    @Test
    fun `obtener producto por id con UnknownHostException devuelve NetworkError`() = runTest {
        coEvery { productApi.getProductById(any()) } throws UnknownHostException()

        val result = repository.getProductById(1)

        assertTrue(result is ProductResult.NetworkError)
    }

    @Test
    fun `obtener producto por id con ConnectException devuelve NetworkError`() = runTest {
        coEvery { productApi.getProductById(any()) } throws ConnectException()

        val result = repository.getProductById(1)

        assertTrue(result is ProductResult.NetworkError)
    }

    @Test
    fun `obtener producto por id con SocketTimeoutException devuelve NetworkError`() = runTest {
        coEvery { productApi.getProductById(any()) } throws SocketTimeoutException()

        val result = repository.getProductById(1)

        assertTrue(result is ProductResult.NetworkError)
    }

    @Test
    fun `obtener producto por id con excepcion generica devuelve Error`() = runTest {
        coEvery { productApi.getProductById(any()) } throws RuntimeException("Error al buscar")

        val result = repository.getProductById(1)

        assertTrue(result is ProductResult.Error)
        assertEquals("Error al buscar", (result as ProductResult.Error).message)
    }

    @Test
    fun `obtener productos en oferta llama al metodo correcto`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "Producto Oferta", 100.0, "CONSOLAS", 20)
        )
        val response = Response.success(ProductsResponse(count = 1, data = productsDto))

        coEvery { productApi.getAllProducts(null, null, true) } returns response

        val result = repository.getProductsOnSale()

        assertTrue(result is ProductResult.Success)
        coVerify { productApi.getAllProducts(null, null, true) }
    }

    @Test
    fun `obtener productos con body nulo devuelve ServerError`() = runTest {
        val response = Response.success<ProductsResponse>(null)

        coEvery { productApi.getAllProducts(any(), any(), any()) } returns response

        val result = repository.getAllProducts()

        assertTrue(result is ProductResult.ServerError)
    }

    @Test
    fun `obtener productos con todos los filtros aplica correctamente`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "PlayStation 5", 500.0, "CONSOLAS", 10)
        )
        val response = Response.success(ProductsResponse(count = 1, data = productsDto))

        coEvery { productApi.getAllProducts("CONSOLAS", "PlayStation", true) } returns response

        val result = repository.getAllProducts(
            category = "CONSOLAS",
            search = "PlayStation",
            hasDiscount = true
        )

        assertTrue(result is ProductResult.Success)
        coVerify { productApi.getAllProducts("CONSOLAS", "PlayStation", true) }
    }

    @Test
    fun `obtener productos con respuesta vacia devuelve Success con lista vacia`() = runTest {
        val response = Response.success(ProductsResponse(count = 0, data = emptyList()))

        coEvery { productApi.getAllProducts(any(), any(), any()) } returns response

        val result = repository.getAllProducts()

        assertTrue(result is ProductResult.Success)
        assertTrue((result as ProductResult.Success).products.isEmpty())
    }
}

