package com.grupo10.levelupgamer.ui.components

import com.grupo10.levelupgamer.model.Product
import com.grupo10.levelupgamer.model.ProductCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductCardDataTest {

    @Test
    fun `Product con descuento calcula finalPrice correctamente`() {
        val product = Product(
            id = 1,
            name = "Test Product",
            price = 100000.0,
            description = "Test",
            category = ProductCategory.CONSOLAS,
            discount = 10
        )

        val expectedFinalPrice = 90000.0
        assertEquals(expectedFinalPrice, product.finalPrice, 0.01)
    }

    @Test
    fun `Product sin descuento mantiene precio original`() {
        val product = Product(
            id = 1,
            name = "Test Product",
            price = 100000.0,
            description = "Test",
            category = ProductCategory.CONSOLAS,
            discount = 0
        )

        assertEquals(100000.0, product.finalPrice, 0.01)
    }

    @Test
    fun `Product con descuento tiene hasDiscount true`() {
        val product = Product(
            id = 1,
            name = "Test Product",
            price = 100000.0,
            description = "Test",
            category = ProductCategory.CONSOLAS,
            discount = 15
        )

        assertTrue(product.hasDiscount)
    }

    @Test
    fun `Product sin descuento tiene hasDiscount false`() {
        val product = Product(
            id = 1,
            name = "Test Product",
            price = 100000.0,
            description = "Test",
            category = ProductCategory.CONSOLAS,
            discount = 0
        )

        assertFalse(product.hasDiscount)
    }

    @Test
    fun `Product con diferentes categorias se crean correctamente`() {
        val consola = Product(
            id = 1,
            name = "PlayStation 5",
            price = 499990.0,
            description = "Consola",
            category = ProductCategory.CONSOLAS
        )

        val juego = Product(
            id = 2,
            name = "God of War",
            price = 59990.0,
            description = "Juego",
            category = ProductCategory.JUEGOS
        )

        val accesorio = Product(
            id = 3,
            name = "DualSense",
            price = 59990.0,
            description = "Control",
            category = ProductCategory.ACCESORIOS
        )

        assertEquals(ProductCategory.CONSOLAS, consola.category)
        assertEquals(ProductCategory.JUEGOS, juego.category)
        assertEquals(ProductCategory.ACCESORIOS, accesorio.category)
    }

    @Test
    fun `Product con descuento del 50 porciento calcula correctamente`() {
        val product = Product(
            id = 1,
            name = "Test",
            price = 200000.0,
            description = "Test",
            category = ProductCategory.CONSOLAS,
            discount = 50
        )

        assertEquals(100000.0, product.finalPrice, 0.01)
    }

    @Test
    fun `Product con descuento del 100 porciento da precio cero`() {
        val product = Product(
            id = 1,
            name = "Test",
            price = 100000.0,
            description = "Test",
            category = ProductCategory.CONSOLAS,
            discount = 100
        )

        assertEquals(0.0, product.finalPrice, 0.01)
    }

    @Test
    fun `Product con stock actualiza correctamente`() {
        val product = Product(
            id = 1,
            name = "Test",
            price = 100000.0,
            description = "Test",
            category = ProductCategory.CONSOLAS,
            stock = 10
        )

        assertEquals(10, product.stock)
    }

    @Test
    fun `Product con rating actualiza correctamente`() {
        val product = Product(
            id = 1,
            name = "Test",
            price = 100000.0,
            description = "Test",
            category = ProductCategory.CONSOLAS,
            rating = 4.5
        )

        assertEquals(4.5, product.rating, 0.01)
    }

    @Test
    fun `Product con imageUrl se guarda correctamente`() {
        val imageUrl = "https://example.com/image.jpg"
        val product = Product(
            id = 1,
            name = "Test",
            price = 100000.0,
            description = "Test",
            category = ProductCategory.CONSOLAS,
            imageUrl = imageUrl
        )

        assertEquals(imageUrl, product.imageUrl)
    }
}

