package com.grupo10.levelupgamer.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CartItemTest {

    @Test
    fun `crear item de carrito con todos los atributos`() {
        val cartItem = CartItem(
            id = 1,
            productId = 10,
            productName = "PlayStation 5",
            productPrice = 499990.0,
            productImage = "https://example.com/ps5.jpg",
            quantity = 2,
            userId = 100
        )

        assertEquals(1, cartItem.id)
        assertEquals(10, cartItem.productId)
        assertEquals("PlayStation 5", cartItem.productName)
        assertEquals(499990.0, cartItem.productPrice, 0.01)
        assertEquals("https://example.com/ps5.jpg", cartItem.productImage)
        assertEquals(2, cartItem.quantity)
        assertEquals(100, cartItem.userId)
    }

    @Test
    fun `calcular subtotal de item con cantidad uno`() {
        val cartItem = CartItem(
            id = 1,
            productId = 1,
            productName = "Producto",
            productPrice = 50000.0,
            quantity = 1,
            userId = 1
        )

        assertEquals(50000.0, cartItem.subtotal, 0.01)
    }

    @Test
    fun `calcular subtotal de item con cantidad multiple`() {
        val cartItem = CartItem(
            id = 1,
            productId = 1,
            productName = "Producto",
            productPrice = 25000.0,
            quantity = 3,
            userId = 1
        )

        assertEquals(75000.0, cartItem.subtotal, 0.01)
    }

    @Test
    fun `calcular subtotal de item con cantidad cero`() {
        val cartItem = CartItem(
            id = 1,
            productId = 1,
            productName = "Producto",
            productPrice = 10000.0,
            quantity = 0,
            userId = 1
        )

        assertEquals(0.0, cartItem.subtotal, 0.01)
    }

    @Test
    fun `item con valores predeterminados`() {
        val cartItem = CartItem(
            productId = 1,
            productName = "Producto",
            productPrice = 10000.0,
            quantity = 1,
            userId = 1
        )

        assertEquals(0, cartItem.id)
        assertEquals("", cartItem.productImage)
    }

    @Test
    fun `item sin imagen`() {
        val cartItem = CartItem(
            productId = 1,
            productName = "Producto Sin Imagen",
            productPrice = 5000.0,
            productImage = "",
            quantity = 1,
            userId = 1
        )

        assertEquals("", cartItem.productImage)
    }

    @Test
    fun `item con precio decimal`() {
        val cartItem = CartItem(
            productId = 1,
            productName = "Producto",
            productPrice = 12345.67,
            quantity = 2,
            userId = 1
        )

        assertEquals(24691.34, cartItem.subtotal, 0.01)
    }

    @Test
    fun `multiples items del mismo producto`() {
        val cartItem = CartItem(
            productId = 1,
            productName = "PlayStation 5",
            productPrice = 499990.0,
            quantity = 5,
            userId = 1
        )

        assertEquals(2499950.0, cartItem.subtotal, 0.01)
    }

    @Test
    fun `item con cantidad grande`() {
        val cartItem = CartItem(
            productId = 1,
            productName = "Accesorio",
            productPrice = 1000.0,
            quantity = 100,
            userId = 1
        )

        assertEquals(100000.0, cartItem.subtotal, 0.01)
    }

    @Test
    fun `item con precio alto`() {
        val cartItem = CartItem(
            productId = 1,
            productName = "Producto Premium",
            productPrice = 999999.99,
            quantity = 1,
            userId = 1
        )

        assertEquals(999999.99, cartItem.subtotal, 0.01)
    }

    @Test
    fun `crear copia de item con cantidad modificada`() {
        val original = CartItem(
            id = 1,
            productId = 10,
            productName = "Producto",
            productPrice = 10000.0,
            quantity = 2,
            userId = 1
        )

        val modificado = original.copy(quantity = 5)

        assertEquals(5, modificado.quantity)
        assertEquals(50000.0, modificado.subtotal, 0.01)
        assertEquals(original.productId, modificado.productId)
    }

    @Test
    fun `items de diferentes usuarios`() {
        val item1 = CartItem(
            productId = 1,
            productName = "Producto",
            productPrice = 10000.0,
            quantity = 1,
            userId = 100
        )

        val item2 = CartItem(
            productId = 1,
            productName = "Producto",
            productPrice = 10000.0,
            quantity = 1,
            userId = 200
        )

        assertEquals(100, item1.userId)
        assertEquals(200, item2.userId)
        assert(item1.userId != item2.userId)
    }

    @Test
    fun `item con nombre largo`() {
        val longName = "PlayStation 5 Digital Edition Bundle con 2 controles y 3 juegos incluidos"
        val cartItem = CartItem(
            productId = 1,
            productName = longName,
            productPrice = 599990.0,
            quantity = 1,
            userId = 1
        )

        assertEquals(longName, cartItem.productName)
    }

    @Test
    fun `item con URL de imagen completa`() {
        val imageUrl = "https://cdn.example.com/products/images/ps5-digital-edition.jpg"
        val cartItem = CartItem(
            productId = 1,
            productName = "PlayStation 5",
            productPrice = 499990.0,
            productImage = imageUrl,
            quantity = 1,
            userId = 1
        )

        assertEquals(imageUrl, cartItem.productImage)
    }

    @Test
    fun `comparar dos items identicos`() {
        val item1 = CartItem(
            id = 1,
            productId = 10,
            productName = "Producto",
            productPrice = 10000.0,
            quantity = 2,
            userId = 1
        )

        val item2 = CartItem(
            id = 1,
            productId = 10,
            productName = "Producto",
            productPrice = 10000.0,
            quantity = 2,
            userId = 1
        )

        assertEquals(item1, item2)
    }
}

