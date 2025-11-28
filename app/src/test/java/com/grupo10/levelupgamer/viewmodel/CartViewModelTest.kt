package com.grupo10.levelupgamer.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.grupo10.levelupgamer.data.repository.CartRepository
import com.grupo10.levelupgamer.model.CartItem
import com.grupo10.levelupgamer.model.Product
import com.grupo10.levelupgamer.model.ProductCategory
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CartViewModel
    private lateinit var repository: CartRepository
    private lateinit var application: android.app.Application

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        application = mockk(relaxed = true)
        repository = mockk(relaxed = true)

        every { application.applicationContext } returns application

        viewModel = CartViewModel(application)

        val field = CartViewModel::class.java.getDeclaredField("repository")
        field.isAccessible = true
        field.set(viewModel, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `establecer usuario actual actualiza el estado`() {
        viewModel.setCurrentUser(123)
        testDispatcher.scheduler.advanceUntilIdle()

        val cartItems = viewModel.getCartItems()
        assertNotNull(cartItems)
    }

    @Test
    fun `obtener items del carrito devuelve LiveData cuando hay usuario`() {
        val mockLiveData = MutableLiveData<List<CartItem>>()
        every { repository.getCartItems(any()) } returns mockLiveData

        viewModel.setCurrentUser(123)
        val result = viewModel.getCartItems()

        assertNotNull(result)
        verify { repository.getCartItems(123) }
    }

    @Test
    fun `obtener items del carrito devuelve nulo cuando no hay usuario`() {
        val result = viewModel.getCartItems()
        assertNull(result)
    }

    @Test
    fun `obtener total del carrito devuelve LiveData cuando hay usuario`() {
        val mockLiveData = MutableLiveData<Double?>()
        every { repository.getCartTotal(any()) } returns mockLiveData

        viewModel.setCurrentUser(123)
        val result = viewModel.getCartTotal()

        assertNotNull(result)
        verify { repository.getCartTotal(123) }
    }

    @Test
    fun `obtener total del carrito devuelve nulo cuando no hay usuario`() {
        val result = viewModel.getCartTotal()
        assertNull(result)
    }

    @Test
    fun `agregar producto al carrito cuando hay usuario actual`() = runTest {
        viewModel.setCurrentUser(123)

        val product = Product(
            id = 1,
            name = "PlayStation 5",
            price = 499990.0,
            description = "Consola",
            category = ProductCategory.CONSOLAS,
            stock = 10
        )

        coEvery { repository.addToCart(any()) } just Runs

        viewModel.addToCart(product, 2)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.addToCart(match {
            it.productId == 1 && it.quantity == 2 && it.userId == 123
        }) }

        assertEquals("Producto agregado al carrito", viewModel.addToCartResult.value)
    }

    @Test
    fun `agregar producto al carrito sin usuario no hace nada`() = runTest {
        val product = Product(
            id = 1,
            name = "PlayStation 5",
            price = 499990.0,
            description = "Consola",
            category = ProductCategory.CONSOLAS
        )

        viewModel.addToCart(product, 1)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { repository.addToCart(any()) }
    }

    @Test
    fun `agregar producto con error muestra mensaje de error`() = runTest {
        viewModel.setCurrentUser(123)

        val product = Product(
            id = 1,
            name = "PlayStation 5",
            price = 499990.0,
            description = "Consola",
            category = ProductCategory.CONSOLAS
        )

        coEvery { repository.addToCart(any()) } throws Exception("Error de base de datos")

        viewModel.addToCart(product, 1)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Error al agregar al carrito", viewModel.addToCartResult.value)
    }

    @Test
    fun `actualizar cantidad de item en el carrito`() = runTest {
        val cartItem = CartItem(
            id = 1,
            productId = 1,
            productName = "PlayStation 5",
            productPrice = 499990.0,
            quantity = 1,
            userId = 123
        )

        coEvery { repository.updateCartItem(any()) } just Runs

        viewModel.updateQuantity(cartItem, 3)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.updateCartItem(match { it.quantity == 3 }) }
    }

    @Test
    fun `actualizar cantidad a cero elimina el item del carrito`() = runTest {
        val cartItem = CartItem(
            id = 1,
            productId = 1,
            productName = "PlayStation 5",
            productPrice = 499990.0,
            quantity = 1,
            userId = 123
        )

        coEvery { repository.removeFromCart(any()) } just Runs

        viewModel.updateQuantity(cartItem, 0)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.removeFromCart(cartItem) }
        coVerify(exactly = 0) { repository.updateCartItem(any()) }
    }

    @Test
    fun `remover item del carrito llama al repositorio`() = runTest {
        val cartItem = CartItem(
            id = 1,
            productId = 1,
            productName = "PlayStation 5",
            productPrice = 499990.0,
            quantity = 1,
            userId = 123
        )

        coEvery { repository.removeFromCart(any()) } just Runs

        viewModel.removeFromCart(cartItem)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.removeFromCart(cartItem) }
    }

    @Test
    fun `limpiar carrito cuando hay usuario actual`() = runTest {
        viewModel.setCurrentUser(123)
        coEvery { repository.clearCart(any()) } just Runs

        viewModel.clearCart()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.clearCart(123) }
    }

    @Test
    fun `limpiar carrito sin usuario no hace nada`() = runTest {
        viewModel.clearCart()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { repository.clearCart(any()) }
    }

    @Test
    fun `mostrar dialogo de checkout actualiza el estado a verdadero`() {
        viewModel.showCheckoutDialog()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(true, viewModel.showCheckoutDialog.value)
    }

    @Test
    fun `ocultar dialogo de checkout actualiza el estado a falso`() {
        viewModel.showCheckoutDialog()
        viewModel.hideCheckoutDialog()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.showCheckoutDialog.value)
    }

    @Test
    fun `procesar checkout limpia el carrito y oculta el dialogo`() = runTest {
        viewModel.setCurrentUser(123)
        viewModel.showCheckoutDialog()

        coEvery { repository.clearCart(any()) } just Runs

        viewModel.processCheckout()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.clearCart(123) }
        assertEquals(false, viewModel.showCheckoutDialog.value)
    }

    @Test
    fun `estado inicial del dialogo de checkout es falso`() {
        assertEquals(false, viewModel.showCheckoutDialog.value)
    }

    @Test
    fun `agregar producto con cantidad predeterminada usa 1`() = runTest {
        viewModel.setCurrentUser(123)

        val product = Product(
            id = 1,
            name = "PlayStation 5",
            price = 499990.0,
            description = "Consola",
            category = ProductCategory.CONSOLAS
        )

        coEvery { repository.addToCart(any()) } just Runs

        viewModel.addToCart(product)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.addToCart(match { it.quantity == 1 }) }
    }

    @Test
    fun `actualizar cantidad a negativo elimina el item`() = runTest {
        val cartItem = CartItem(
            id = 1,
            productId = 1,
            productName = "PlayStation 5",
            productPrice = 499990.0,
            quantity = 1,
            userId = 123
        )

        coEvery { repository.removeFromCart(any()) } just Runs

        viewModel.updateQuantity(cartItem, -5)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.removeFromCart(cartItem) }
    }
}

