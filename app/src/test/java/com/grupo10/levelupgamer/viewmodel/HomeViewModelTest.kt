package com.grupo10.levelupgamer.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.grupo10.levelupgamer.data.repository.ProductRepository
import com.grupo10.levelupgamer.data.repository.ProductResult
import com.grupo10.levelupgamer.data.remote.dto.CategoryDto
import com.grupo10.levelupgamer.data.remote.dto.ProductDto
import com.grupo10.levelupgamer.model.Product
import com.grupo10.levelupgamer.model.ProductCategory
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: ProductRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = HomeViewModel()

        val field = HomeViewModel::class.java.getDeclaredField("productRepository")
        field.isAccessible = true
        field.set(viewModel, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `estado inicial es correcto`() {
        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertNull(state.selectedProduct)
        assertTrue(state.allProducts.isEmpty())
        assertTrue(state.filteredProducts.isEmpty())
        assertTrue(state.productsOnSale.isEmpty())
        assertEquals(3, state.notificationCount)
        assertNull(state.error)
        assertFalse(state.isNetworkError)
    }

    @Test
    fun `loadProducts exitoso actualiza el estado con productos`() = runTest {
        val mockProducts = listOf(
            ProductDto(
                id = 1,
                name = "PlayStation 5",
                price = 499990.0,
                description = "Consola next-gen",
                category = CategoryDto(1, "CONSOLAS"),
                imageUrl = "url",
                stock = 10,
                discount = 10,
                hasDiscount = true,
                finalPrice = 449991.0,
                rating = 4.5
            ),
            ProductDto(
                id = 2,
                name = "God of War",
                price = 59990.0,
                description = "Juego de acción",
                category = CategoryDto(2, "JUEGOS"),
                imageUrl = "url",
                stock = 20,
                discount = 0,
                hasDiscount = false,
                finalPrice = 59990.0,
                rating = 5.0
            )
        )

        coEvery { repository.getAllProducts() } returns ProductResult.Success(mockProducts)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.allProducts.size)
        assertEquals(2, state.filteredProducts.size)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `loadProducts con error de red actualiza estado con mensaje de error`() = runTest {
        coEvery { repository.getAllProducts() } returns ProductResult.NetworkError

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("No se puede conectar al servidor. Verifica tu conexión a internet.", state.error)
        assertTrue(state.isNetworkError)
    }

    @Test
    fun `loadProducts con error de servidor actualiza estado con mensaje de error`() = runTest {
        coEvery { repository.getAllProducts() } returns ProductResult.ServerError

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Error del servidor. Por favor, intenta más tarde.", state.error)
        assertFalse(state.isNetworkError)
    }

    @Test
    fun `loadProducts con error generico actualiza estado con mensaje de error`() = runTest {
        coEvery { repository.getAllProducts() } returns ProductResult.Error("Error desconocido")

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Error: Error desconocido", state.error)
        assertFalse(state.isNetworkError)
    }

    @Test
    fun `onSearchQueryChange filtra productos por nombre`() = runTest {
        val mockProducts = listOf(
            ProductDto(
                id = 1,
                name = "PlayStation 5",
                price = 499990.0,
                description = "Consola",
                category = CategoryDto(1, "CONSOLAS"),
                imageUrl = "url",
                stock = 10,
                discount = 0,
                hasDiscount = false,
                finalPrice = 499990.0,
                rating = 4.5
            ),
            ProductDto(
                id = 2,
                name = "Xbox Series X",
                price = 499990.0,
                description = "Consola",
                category = CategoryDto(1, "CONSOLAS"),
                imageUrl = "url",
                stock = 10,
                discount = 0,
                hasDiscount = false,
                finalPrice = 499990.0,
                rating = 4.5
            )
        )

        coEvery { repository.getAllProducts() } returns ProductResult.Success(mockProducts)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSearchQueryChange("PlayStation")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("PlayStation", state.searchQuery)
        assertEquals(1, state.filteredProducts.size)
        assertEquals("PlayStation 5", state.filteredProducts[0].name)
    }

    @Test
    fun `onSearchQueryChange filtra productos por descripcion`() = runTest {
        val mockProducts = listOf(
            ProductDto(
                id = 1,
                name = "PS5",
                price = 499990.0,
                description = "Consola potente",
                category = CategoryDto(1, "CONSOLAS"),
                imageUrl = "url",
                stock = 10,
                discount = 0,
                hasDiscount = false,
                finalPrice = 499990.0,
                rating = 4.5
            )
        )

        coEvery { repository.getAllProducts() } returns ProductResult.Success(mockProducts)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSearchQueryChange("potente")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.filteredProducts.size)
    }

    @Test
    fun `onSearchQueryChange con query vacio muestra todos los productos`() = runTest {
        val mockProducts = listOf(
            ProductDto(
                id = 1,
                name = "PlayStation 5",
                price = 499990.0,
                description = "Consola",
                category = CategoryDto(1, "CONSOLAS"),
                imageUrl = "url",
                stock = 10,
                discount = 0,
                hasDiscount = false,
                finalPrice = 499990.0,
                rating = 4.5
            ),
            ProductDto(
                id = 2,
                name = "Xbox Series X",
                price = 499990.0,
                description = "Consola",
                category = CategoryDto(1, "CONSOLAS"),
                imageUrl = "url",
                stock = 10,
                discount = 0,
                hasDiscount = false,
                finalPrice = 499990.0,
                rating = 4.5
            )
        )

        coEvery { repository.getAllProducts() } returns ProductResult.Success(mockProducts)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSearchQueryChange("PlayStation")
        viewModel.onSearchQueryChange("")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.filteredProducts.size)
    }

    @Test
    fun `selectProduct actualiza el producto seleccionado`() {
        val product = Product(
            id = 1,
            name = "PlayStation 5",
            price = 499990.0,
            description = "Consola",
            category = ProductCategory.CONSOLAS
        )

        viewModel.selectProduct(product)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.selectedProduct)
        assertEquals("PlayStation 5", state.selectedProduct?.name)
    }

    @Test
    fun `selectProduct con null limpia el producto seleccionado`() {
        val product = Product(
            id = 1,
            name = "PlayStation 5",
            price = 499990.0,
            description = "Consola",
            category = ProductCategory.CONSOLAS
        )

        viewModel.selectProduct(product)
        viewModel.selectProduct(null)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.selectedProduct)
    }

    @Test
    fun `clearNotifications establece contador a cero`() {
        viewModel.clearNotifications()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(0, state.notificationCount)
    }

    @Test
    fun `decrementNotificationCount decrementa el contador`() {
        viewModel.decrementNotificationCount()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.notificationCount)
    }

    @Test
    fun `decrementNotificationCount no va por debajo de cero`() {
        viewModel.clearNotifications()
        viewModel.decrementNotificationCount()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(0, state.notificationCount)
    }

    @Test
    fun `loadProducts filtra productos en oferta`() = runTest {
        val mockProducts = listOf(
            ProductDto(
                id = 1,
                name = "PlayStation 5",
                price = 499990.0,
                description = "Consola",
                category = CategoryDto(1, "CONSOLAS"),
                imageUrl = "url",
                stock = 10,
                discount = 10,
                hasDiscount = true,
                finalPrice = 449991.0,
                rating = 4.5
            ),
            ProductDto(
                id = 2,
                name = "Xbox Series X",
                price = 499990.0,
                description = "Consola",
                category = CategoryDto(1, "CONSOLAS"),
                imageUrl = "url",
                stock = 10,
                discount = 0,
                hasDiscount = false,
                finalPrice = 499990.0,
                rating = 4.5
            )
        )

        coEvery { repository.getAllProducts() } returns ProductResult.Success(mockProducts)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.productsOnSale.size)
        assertTrue(state.productsOnSale[0].hasDiscount)
    }

    @Test
    fun `onSearchQueryChange filtra productos por categoria`() = runTest {
        val mockProducts = listOf(
            ProductDto(
                id = 1,
                name = "PlayStation 5",
                price = 499990.0,
                description = "Consola next-gen",
                category = CategoryDto(1, "CONSOLAS"),
                imageUrl = "url",
                stock = 10,
                discount = 0,
                hasDiscount = false,
                finalPrice = 499990.0,
                rating = 4.5
            ),
            ProductDto(
                id = 2,
                name = "God of War",
                price = 59990.0,
                description = "Juego de acción",
                category = CategoryDto(2, "JUEGOS"),
                imageUrl = "url",
                stock = 20,
                discount = 0,
                hasDiscount = false,
                finalPrice = 59990.0,
                rating = 5.0
            )
        )

        coEvery { repository.getAllProducts() } returns ProductResult.Success(mockProducts)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSearchQueryChange("CONSOLAS")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.filteredProducts.size)
        assertEquals(ProductCategory.CONSOLAS, state.filteredProducts[0].category)
    }
}

