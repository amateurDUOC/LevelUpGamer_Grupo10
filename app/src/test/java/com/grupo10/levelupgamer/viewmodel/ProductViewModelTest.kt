package com.grupo10.levelupgamer.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.grupo10.levelupgamer.data.remote.dto.CategoryDto
import com.grupo10.levelupgamer.data.remote.dto.ProductDto
import com.grupo10.levelupgamer.data.repository.ProductRepository
import com.grupo10.levelupgamer.data.repository.ProductResult
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ProductViewModel
    private lateinit var repository: ProductRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        
        coEvery { repository.getAllProducts(any(), any(), any()) } returns ProductResult.Success(emptyList())
        
        viewModel = ProductViewModel()
        
        val field = ProductViewModel::class.java.getDeclaredField("repository")
        field.isAccessible = true
        field.set(viewModel, repository)
        
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    private fun createProductDto(
        id: Int,
        name: String,
        price: Double,
        description: String,
        categoryName: String,
        discount: Int = 0
    ) = ProductDto(
        id = id,
        name = name,
        price = price,
        description = description,
        category = CategoryDto(1, categoryName),
        imageUrl = "",
        stock = 10,
        discount = discount,
        hasDiscount = discount > 0,
        finalPrice = if (discount > 0) price - (price * discount / 100) else price,
        rating = 4.5
    )

    @Test
    fun `al cargar productos exitosamente actualiza el estado con productos`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "PlayStation 5", 499990.0, "Consola de última generación", "CONSOLAS", 10)
        )

        coEvery { repository.getAllProducts(any(), any(), any()) } returns ProductResult.Success(productsDto)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.allProducts.size)
        assertEquals("PlayStation 5", state.allProducts[0].name)
        assertEquals(false, state.isLoading)
        assertNull(state.error)
        assertEquals(false, state.isNetworkError)
    }

    @Test
    fun `al cargar productos con error de red actualiza el estado correctamente`() = runTest {
        coEvery { repository.getAllProducts(any(), any(), any()) } returns ProductResult.NetworkError

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.allProducts.isEmpty())
        assertEquals(false, state.isLoading)
        assertNotNull(state.error)
        assertTrue(state.error!!.contains("conexión"))
        assertEquals(true, state.isNetworkError)
    }

    @Test
    fun `al cargar productos con error del servidor actualiza el estado`() = runTest {
        coEvery { repository.getAllProducts(any(), any(), any()) } returns ProductResult.ServerError

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.error)
        assertTrue(state.error!!.contains("servidor"))
    }

    @Test
    fun `buscar productos filtra correctamente por nombre`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "PlayStation 5", 499990.0, "Consola", "CONSOLAS", 10),
            createProductDto(2, "Xbox Series X", 479990.0, "Consola", "CONSOLAS", 0)
        )

        coEvery { repository.getAllProducts(any(), any(), any()) } returns ProductResult.Success(productsDto)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.searchProducts("PlayStation")

        val state = viewModel.uiState.value
        assertEquals(1, state.filteredProducts.size)
        assertEquals("PlayStation 5", state.filteredProducts[0].name)
        assertEquals("PlayStation", state.searchQuery)
    }

    @Test
    fun `buscar productos filtra correctamente por descripcion`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "PS5", 499990.0, "Consola de última generación", "CONSOLAS", 10),
            createProductDto(2, "Xbox", 479990.0, "Otra consola", "CONSOLAS", 0)
        )

        coEvery { repository.getAllProducts(any(), any(), any()) } returns ProductResult.Success(productsDto)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.searchProducts("última")

        val state = viewModel.uiState.value
        assertEquals(1, state.filteredProducts.size)
        assertEquals("PS5", state.filteredProducts[0].name)
    }

    @Test
    fun `buscar productos con texto vacio muestra todos los productos`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "PlayStation 5", 499990.0, "Consola", "CONSOLAS", 10),
            createProductDto(2, "Xbox Series X", 479990.0, "Consola", "CONSOLAS", 0)
        )

        coEvery { repository.getAllProducts(any(), any(), any()) } returns ProductResult.Success(productsDto)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.searchProducts("")

        val state = viewModel.uiState.value
        assertEquals(2, state.filteredProducts.size)
    }

    @Test
    fun `filtrar por categoria filtra correctamente los productos`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "PlayStation 5", 499990.0, "Consola", "CONSOLAS", 10),
            createProductDto(2, "God of War", 59990.0, "Juego", "JUEGOS", 0),
            createProductDto(3, "DualSense", 69990.0, "Control", "ACCESORIOS", 15)
        )

        coEvery { repository.getAllProducts(any(), any(), any()) } returns ProductResult.Success(productsDto)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.filterByCategory(ProductCategory.JUEGOS)

        val state = viewModel.uiState.value
        assertEquals(1, state.filteredProducts.size)
        assertEquals("God of War", state.filteredProducts[0].name)
        assertEquals(ProductCategory.JUEGOS, state.selectedCategory)
    }

    @Test
    fun `filtrar por categoria nula muestra todos los productos`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "PlayStation 5", 499990.0, "Consola", "CONSOLAS", 10),
            createProductDto(2, "God of War", 59990.0, "Juego", "JUEGOS", 0)
        )

        coEvery { repository.getAllProducts(any(), any(), any()) } returns ProductResult.Success(productsDto)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.filterByCategory(null)

        val state = viewModel.uiState.value
        assertEquals(2, state.filteredProducts.size)
        assertNull(state.selectedCategory)
    }

    @Test
    fun `seleccionar producto actualiza el estado correctamente`() {
        val product = com.grupo10.levelupgamer.model.Product(
            id = 1,
            name = "PlayStation 5",
            price = 499990.0,
            description = "Consola",
            category = ProductCategory.CONSOLAS
        )

        viewModel.selectProduct(product)

        val state = viewModel.uiState.value
        assertNotNull(state.selectedProduct)
        assertEquals("PlayStation 5", state.selectedProduct?.name)
    }

    @Test
    fun `seleccionar producto nulo limpia la seleccion`() {
        viewModel.selectProduct(null)

        val state = viewModel.uiState.value
        assertNull(state.selectedProduct)
    }

    @Test
    fun `obtener producto por id actualiza el producto seleccionado`() = runTest {
        val productDto = createProductDto(1, "PlayStation 5", 499990.0, "Consola", "CONSOLAS", 10)

        coEvery { repository.getProductById(1) } returns ProductResult.Success(listOf(productDto))

        viewModel.getProductById(1)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.selectedProduct)
        assertEquals("PlayStation 5", state.selectedProduct?.name)
    }

    @Test
    fun `obtener producto por id inexistente muestra error`() = runTest {
        coEvery { repository.getProductById(999) } returns ProductResult.ServerError

        viewModel.getProductById(999)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.error)
        assertEquals("Producto no encontrado", state.error)
    }

    @Test
    fun `estado inicial tiene valores predeterminados correctos`() {
        val state = ProductUIState()

        assertTrue(state.allProducts.isEmpty())
        assertTrue(state.filteredProducts.isEmpty())
        assertEquals("", state.searchQuery)
        assertNull(state.selectedCategory)
        assertNull(state.selectedProduct)
        assertEquals(false, state.isLoading)
        assertNull(state.error)
        assertEquals(false, state.isNetworkError)
    }

    @Test
    fun `filtrar por categoria y luego buscar aplica ambos filtros`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "PlayStation 5", 499990.0, "Consola Sony", "CONSOLAS", 10),
            createProductDto(2, "PlayStation 4", 299990.0, "Consola Sony", "CONSOLAS", 15),
            createProductDto(3, "God of War", 59990.0, "Juego PlayStation", "JUEGOS", 0)
        )

        coEvery { repository.getAllProducts(any(), any(), any()) } returns ProductResult.Success(productsDto)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.filterByCategory(ProductCategory.CONSOLAS)
        val afterCategoryFilter = viewModel.uiState.value
        assertEquals(2, afterCategoryFilter.filteredProducts.size)
    }

    @Test
    fun `filtrar por categoria limpia la busqueda anterior`() = runTest {
        val productsDto = listOf(
            createProductDto(1, "PlayStation 5", 499990.0, "Consola", "CONSOLAS", 10),
            createProductDto(2, "God of War", 59990.0, "Juego", "JUEGOS", 0)
        )

        coEvery { repository.getAllProducts(any(), any(), any()) } returns ProductResult.Success(productsDto)

        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.searchProducts("PlayStation")
        viewModel.filterByCategory(ProductCategory.JUEGOS)

        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertEquals(1, state.filteredProducts.size)
    }
}

