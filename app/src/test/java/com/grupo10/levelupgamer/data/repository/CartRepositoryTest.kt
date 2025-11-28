package com.grupo10.levelupgamer.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.grupo10.levelupgamer.data.dao.CartDao
import com.grupo10.levelupgamer.model.CartItem
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: CartRepository
    private lateinit var cartDao: CartDao

    @Before
    fun setup() {
        cartDao = mockk(relaxed = true)
        repository = CartRepository(cartDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getCartItems retorna LiveData del dao`() {
        val mockLiveData = MutableLiveData<List<CartItem>>()
        every { cartDao.getCartItems(any()) } returns mockLiveData

        val result = repository.getCartItems(123)

        assertNotNull(result)
        assertEquals(mockLiveData, result)
        verify { cartDao.getCartItems(123) }
    }

    @Test
    fun `getCartTotal retorna LiveData del dao`() {
        val mockLiveData = MutableLiveData<Double?>()
        every { cartDao.getCartTotal(any()) } returns mockLiveData

        val result = repository.getCartTotal(123)

        assertNotNull(result)
        assertEquals(mockLiveData, result)
        verify { cartDao.getCartTotal(123) }
    }

    @Test
    fun `addToCart inserta nuevo item cuando no existe`() = runTest {
        val cartItem = CartItem(
            id = 0,
            productId = 1,
            productName = "PlayStation 5",
            productPrice = 499990.0,
            quantity = 1,
            userId = 123
        )

        coEvery { cartDao.getCartItemByProduct(any(), any()) } returns null
        coEvery { cartDao.insert(any()) } just Runs

        repository.addToCart(cartItem)

        coVerify { cartDao.getCartItemByProduct(123, 1) }
        coVerify { cartDao.insert(cartItem) }
        coVerify(exactly = 0) { cartDao.update(any()) }
    }

    @Test
    fun `addToCart actualiza cantidad cuando item ya existe`() = runTest {
        val existingItem = CartItem(
            id = 1,
            productId = 1,
            productName = "PlayStation 5",
            productPrice = 499990.0,
            quantity = 2,
            userId = 123
        )

        val newItem = CartItem(
            id = 0,
            productId = 1,
            productName = "PlayStation 5",
            productPrice = 499990.0,
            quantity = 3,
            userId = 123
        )

        coEvery { cartDao.getCartItemByProduct(123, 1) } returns existingItem
        coEvery { cartDao.update(any()) } just Runs

        repository.addToCart(newItem)

        coVerify { cartDao.getCartItemByProduct(123, 1) }
        coVerify { cartDao.update(match { it.quantity == 5 }) }
        coVerify(exactly = 0) { cartDao.insert(any()) }
    }

    @Test
    fun `updateCartItem llama al dao update`() = runTest {
        val cartItem = CartItem(
            id = 1,
            productId = 1,
            productName = "PlayStation 5",
            productPrice = 499990.0,
            quantity = 5,
            userId = 123
        )

        coEvery { cartDao.update(any()) } just Runs

        repository.updateCartItem(cartItem)

        coVerify { cartDao.update(cartItem) }
    }

    @Test
    fun `removeFromCart llama al dao delete`() = runTest {
        val cartItem = CartItem(
            id = 1,
            productId = 1,
            productName = "PlayStation 5",
            productPrice = 499990.0,
            quantity = 1,
            userId = 123
        )

        coEvery { cartDao.delete(any()) } just Runs

        repository.removeFromCart(cartItem)

        coVerify { cartDao.delete(cartItem) }
    }

    @Test
    fun `clearCart llama al dao clearCart con userId correcto`() = runTest {
        coEvery { cartDao.clearCart(any()) } just Runs

        repository.clearCart(123)

        coVerify { cartDao.clearCart(123) }
    }

    @Test
    fun `addToCart con diferentes usuarios`() = runTest {
        val item1 = CartItem(
            id = 0,
            productId = 1,
            productName = "Product",
            productPrice = 100.0,
            quantity = 1,
            userId = 100
        )

        val item2 = CartItem(
            id = 0,
            productId = 1,
            productName = "Product",
            productPrice = 100.0,
            quantity = 1,
            userId = 200
        )

        coEvery { cartDao.getCartItemByProduct(any(), any()) } returns null
        coEvery { cartDao.insert(any()) } just Runs

        repository.addToCart(item1)
        repository.addToCart(item2)

        coVerify { cartDao.getCartItemByProduct(100, 1) }
        coVerify { cartDao.getCartItemByProduct(200, 1) }
        coVerify(exactly = 2) { cartDao.insert(any()) }
    }

    @Test
    fun `addToCart suma cantidades correctamente`() = runTest {
        val existingItem = CartItem(
            id = 1,
            productId = 1,
            productName = "Product",
            productPrice = 100.0,
            quantity = 10,
            userId = 123
        )

        val newItem = CartItem(
            id = 0,
            productId = 1,
            productName = "Product",
            productPrice = 100.0,
            quantity = 5,
            userId = 123
        )

        coEvery { cartDao.getCartItemByProduct(123, 1) } returns existingItem
        coEvery { cartDao.update(any()) } just Runs

        repository.addToCart(newItem)

        coVerify { cartDao.update(match { it.quantity == 15 }) }
    }

    @Test
    fun `clearCart con diferentes usuarios`() = runTest {
        coEvery { cartDao.clearCart(any()) } just Runs

        repository.clearCart(100)
        repository.clearCart(200)
        repository.clearCart(300)

        coVerify(exactly = 1) { cartDao.clearCart(100) }
        coVerify(exactly = 1) { cartDao.clearCart(200) }
        coVerify(exactly = 1) { cartDao.clearCart(300) }
    }

    @Test
    fun `updateCartItem modifica cantidad`() = runTest {
        val originalItem = CartItem(
            id = 1,
            productId = 1,
            productName = "Product",
            productPrice = 100.0,
            quantity = 1,
            userId = 123
        )

        val updatedItem = originalItem.copy(quantity = 10)

        coEvery { cartDao.update(any()) } just Runs

        repository.updateCartItem(updatedItem)

        coVerify { cartDao.update(match { it.quantity == 10 }) }
    }

    @Test
    fun `getCartItems con diferentes usuarios retorna diferentes LiveData`() {
        val mockLiveData1 = MutableLiveData<List<CartItem>>()
        val mockLiveData2 = MutableLiveData<List<CartItem>>()

        every { cartDao.getCartItems(100) } returns mockLiveData1
        every { cartDao.getCartItems(200) } returns mockLiveData2

        val result1 = repository.getCartItems(100)
        val result2 = repository.getCartItems(200)

        verify { cartDao.getCartItems(100) }
        verify { cartDao.getCartItems(200) }
        assertNotNull(result1)
        assertNotNull(result2)
    }

    @Test
    fun `addToCart preserva informacion del producto`() = runTest {
        val cartItem = CartItem(
            id = 0,
            productId = 123,
            productName = "Producto Especial",
            productPrice = 999.99,
            quantity = 7,
            userId = 456,
            productImage = "http://example.com/image.jpg"
        )

        coEvery { cartDao.getCartItemByProduct(any(), any()) } returns null
        coEvery { cartDao.insert(any()) } just Runs

        repository.addToCart(cartItem)

        coVerify {
            cartDao.insert(match {
                it.productName == "Producto Especial" &&
                it.productPrice == 999.99 &&
                it.quantity == 7 &&
                it.productImage == "http://example.com/image.jpg"
            })
        }
    }

    @Test
    fun `removeFromCart elimina item específico`() = runTest {
        val item1 = CartItem(id = 1, productId = 1, productName = "P1", productPrice = 100.0, quantity = 1, userId = 123)
        val item2 = CartItem(id = 2, productId = 2, productName = "P2", productPrice = 200.0, quantity = 1, userId = 123)

        coEvery { cartDao.delete(any()) } just Runs

        repository.removeFromCart(item1)
        repository.removeFromCart(item2)

        coVerify { cartDao.delete(item1) }
        coVerify { cartDao.delete(item2) }
    }

    @Test
    fun `addToCart con cantidad cero`() = runTest {
        val cartItem = CartItem(
            id = 0,
            productId = 1,
            productName = "Product",
            productPrice = 100.0,
            quantity = 0,
            userId = 123
        )

        coEvery { cartDao.getCartItemByProduct(any(), any()) } returns null
        coEvery { cartDao.insert(any()) } just Runs

        repository.addToCart(cartItem)

        coVerify { cartDao.insert(match { it.quantity == 0 }) }
    }
}

