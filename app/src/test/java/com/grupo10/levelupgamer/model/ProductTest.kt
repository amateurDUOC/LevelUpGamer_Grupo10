package com.grupo10.levelupgamer.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductTest {

    @Test
    fun `calcular precio final sin descuento devuelve precio original`() {
        val product = Product(
            id = 1,
            name = "PlayStation 5",
            price = 499990.0,
            description = "Consola",
            category = ProductCategory.CONSOLAS,
            discount = 0
        )

        assertEquals(499990.0, product.finalPrice, 0.01)
    }

    @Test
    fun `calcular precio final con descuento del 10 porciento`() {
        val product = Product(
            id = 1,
            name = "PlayStation 5",
            price = 500000.0,
            description = "Consola",
            category = ProductCategory.CONSOLAS,
            discount = 10
        )

        assertEquals(450000.0, product.finalPrice, 0.01)
    }

    @Test
    fun `calcular precio final con descuento del 50 porciento`() {
        val product = Product(
            id = 1,
            name = "Juego Oferta",
            price = 100000.0,
            description = "Juego",
            category = ProductCategory.JUEGOS,
            discount = 50
        )

        assertEquals(50000.0, product.finalPrice, 0.01)
    }

    @Test
    fun `calcular precio final con descuento del 100 porciento`() {
        val product = Product(
            id = 1,
            name = "Regalo",
            price = 10000.0,
            description = "Regalo",
            category = ProductCategory.ACCESORIOS,
            discount = 100
        )

        assertEquals(0.0, product.finalPrice, 0.01)
    }

    @Test
    fun `hasDiscount es verdadero cuando hay descuento mayor a cero`() {
        val product = Product(
            id = 1,
            name = "Producto",
            price = 10000.0,
            description = "Desc",
            category = ProductCategory.CONSOLAS,
            discount = 15
        )

        assertTrue(product.hasDiscount)
    }

    @Test
    fun `hasDiscount es falso cuando no hay descuento`() {
        val product = Product(
            id = 1,
            name = "Producto",
            price = 10000.0,
            description = "Desc",
            category = ProductCategory.CONSOLAS,
            discount = 0
        )

        assertFalse(product.hasDiscount)
    }

    @Test
    fun `producto con categoria CONSOLAS`() {
        val product = Product(
            id = 1,
            name = "PlayStation 5",
            price = 499990.0,
            description = "Consola",
            category = ProductCategory.CONSOLAS
        )

        assertEquals(ProductCategory.CONSOLAS, product.category)
    }

    @Test
    fun `producto con categoria JUEGOS`() {
        val product = Product(
            id = 1,
            name = "God of War",
            price = 59990.0,
            description = "Juego",
            category = ProductCategory.JUEGOS
        )

        assertEquals(ProductCategory.JUEGOS, product.category)
    }

    @Test
    fun `producto con categoria ACCESORIOS`() {
        val product = Product(
            id = 1,
            name = "DualSense",
            price = 69990.0,
            description = "Control",
            category = ProductCategory.ACCESORIOS
        )

        assertEquals(ProductCategory.ACCESORIOS, product.category)
    }

    @Test
    fun `producto con stock y rating`() {
        val product = Product(
            id = 1,
            name = "Producto",
            price = 10000.0,
            description = "Desc",
            category = ProductCategory.CONSOLAS,
            stock = 25,
            rating = 4.7
        )

        assertEquals(25, product.stock)
        assertEquals(4.7, product.rating, 0.01)
    }

    @Test
    fun `producto con valores predeterminados`() {
        val product = Product(
            id = 1,
            name = "Producto Simple",
            price = 10000.0,
            description = "Descripcion",
            category = ProductCategory.CONSOLAS
        )

        assertEquals("", product.imageUrl)
        assertEquals(0, product.stock)
        assertEquals(0, product.discount)
        assertEquals(0.0, product.rating, 0.01)
    }

    @Test
    fun `calcular precio final con descuento del 25 porciento`() {
        val product = Product(
            id = 1,
            name = "Producto",
            price = 80000.0,
            description = "Desc",
            category = ProductCategory.JUEGOS,
            discount = 25
        )

        assertEquals(60000.0, product.finalPrice, 0.01)
    }

    @Test
    fun `producto con rating maximo`() {
        val product = Product(
            id = 1,
            name = "Producto Excelente",
            price = 50000.0,
            description = "Desc",
            category = ProductCategory.CONSOLAS,
            rating = 5.0
        )

        assertEquals(5.0, product.rating, 0.01)
    }

    @Test
    fun `producto con rating minimo`() {
        val product = Product(
            id = 1,
            name = "Producto",
            price = 50000.0,
            description = "Desc",
            category = ProductCategory.CONSOLAS,
            rating = 0.0
        )

        assertEquals(0.0, product.rating, 0.01)
    }

    @Test
    fun `producto sin stock`() {
        val product = Product(
            id = 1,
            name = "Producto Agotado",
            price = 50000.0,
            description = "Desc",
            category = ProductCategory.CONSOLAS,
            stock = 0
        )

        assertEquals(0, product.stock)
    }

    @Test
    fun `producto con imageUrl`() {
        val product = Product(
            id = 1,
            name = "Producto",
            price = 50000.0,
            description = "Desc",
            category = ProductCategory.CONSOLAS,
            imageUrl = "https://example.com/image.jpg"
        )

        assertEquals("https://example.com/image.jpg", product.imageUrl)
    }
}

