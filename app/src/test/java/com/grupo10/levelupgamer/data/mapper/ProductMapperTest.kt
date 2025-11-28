package com.grupo10.levelupgamer.data.mapper

import com.grupo10.levelupgamer.data.remote.dto.CategoryDto
import com.grupo10.levelupgamer.data.remote.dto.ProductDto
import com.grupo10.levelupgamer.model.ProductCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `mapear ProductDto a Product convierte correctamente todos los campos`() {
        val productDto = ProductDto(
            id = 1,
            name = "PlayStation 5",
            description = "Consola de nueva generación",
            price = 499990.0,
            discount = 10,
            hasDiscount = true,
            finalPrice = 449991.0,
            stock = 10,
            category = CategoryDto(1, "CONSOLAS"),
            imageUrl = "https://example.com/ps5.jpg",
            rating = 4.5
        )

        val product = productDto.toDomain()

        assertEquals(1, product.id)
        assertEquals("PlayStation 5", product.name)
        assertEquals(499990.0, product.price, 0.01)
        assertEquals("Consola de nueva generación", product.description)
        assertEquals(ProductCategory.CONSOLAS, product.category)
        assertEquals("https://example.com/ps5.jpg", product.imageUrl)
        assertEquals(10, product.stock)
        assertEquals(10, product.discount)
        assertEquals(4.5, product.rating, 0.01)
    }

    @Test
    fun `mapear categoria CONSOLAS correctamente`() {
        val productDto = ProductDto(
            id = 1,
            name = "Xbox Series X",
            description = "Consola",
            price = 499990.0,
            discount = 0,
            hasDiscount = false,
            finalPrice = 499990.0,
            stock = 5,
            category = CategoryDto(1, "CONSOLAS"),
            imageUrl = "url",
            rating = 4.0
        )

        val product = productDto.toDomain()

        assertEquals(ProductCategory.CONSOLAS, product.category)
    }

    @Test
    fun `mapear categoria JUEGOS correctamente`() {
        val productDto = ProductDto(
            id = 2,
            name = "God of War",
            description = "Juego de acción",
            price = 59990.0,
            discount = 0,
            hasDiscount = false,
            finalPrice = 59990.0,
            stock = 20,
            category = CategoryDto(2, "JUEGOS"),
            imageUrl = "url",
            rating = 5.0
        )

        val product = productDto.toDomain()

        assertEquals(ProductCategory.JUEGOS, product.category)
    }

    @Test
    fun `mapear categoria ACCESORIOS correctamente`() {
        val productDto = ProductDto(
            id = 3,
            name = "DualSense",
            description = "Control para PS5",
            price = 59990.0,
            discount = 5,
            hasDiscount = true,
            finalPrice = 56990.5,
            stock = 15,
            category = CategoryDto(3, "ACCESORIOS"),
            imageUrl = "url",
            rating = 4.8
        )

        val product = productDto.toDomain()

        assertEquals(ProductCategory.ACCESORIOS, product.category)
    }

    @Test
    fun `mapear categoria desconocida usa JUEGOS como default`() {
        val productDto = ProductDto(
            id = 4,
            name = "Producto desconocido",
            description = "Descripción",
            price = 9990.0,
            discount = 0,
            hasDiscount = false,
            finalPrice = 9990.0,
            stock = 1,
            category = CategoryDto(99, "CATEGORIA_DESCONOCIDA"),
            imageUrl = "url",
            rating = 3.0
        )

        val product = productDto.toDomain()

        assertEquals(ProductCategory.JUEGOS, product.category)
    }

    @Test
    fun `mapear lista vacia retorna lista vacia`() {
        val emptyList = emptyList<ProductDto>()

        val result = emptyList.toDomain()

        assertNotNull(result)
        assertEquals(0, result.size)
    }

    @Test
    fun `mapear lista de ProductDto a lista de Product`() {
        val productDtos = listOf(
            ProductDto(
                id = 1,
                name = "PlayStation 5",
                description = "Consola",
                price = 499990.0,
                discount = 0,
                hasDiscount = false,
                finalPrice = 499990.0,
                stock = 10,
                category = CategoryDto(1, "CONSOLAS"),
                imageUrl = "url",
                rating = 4.5
            ),
            ProductDto(
                id = 2,
                name = "God of War",
                description = "Juego",
                price = 59990.0,
                discount = 0,
                hasDiscount = false,
                finalPrice = 59990.0,
                stock = 20,
                category = CategoryDto(2, "JUEGOS"),
                imageUrl = "url",
                rating = 5.0
            ),
            ProductDto(
                id = 3,
                name = "DualSense",
                description = "Control",
                price = 59990.0,
                discount = 0,
                hasDiscount = false,
                finalPrice = 59990.0,
                stock = 15,
                category = CategoryDto(3, "ACCESORIOS"),
                imageUrl = "url",
                rating = 4.8
            )
        )

        val products = productDtos.toDomain()

        assertEquals(3, products.size)
        assertEquals("PlayStation 5", products[0].name)
        assertEquals("God of War", products[1].name)
        assertEquals("DualSense", products[2].name)
        assertEquals(ProductCategory.CONSOLAS, products[0].category)
        assertEquals(ProductCategory.JUEGOS, products[1].category)
        assertEquals(ProductCategory.ACCESORIOS, products[2].category)
    }

    @Test
    fun `mapear producto con descuento`() {
        val productDto = ProductDto(
            id = 1,
            name = "Producto en oferta",
            description = "Descripción",
            price = 100000.0,
            discount = 25,
            hasDiscount = true,
            finalPrice = 75000.0,
            stock = 5,
            category = CategoryDto(1, "CONSOLAS"),
            imageUrl = "url",
            rating = 4.0
        )

        val product = productDto.toDomain()

        assertEquals(25, product.discount)
    }

    @Test
    fun `mapear producto sin descuento`() {
        val productDto = ProductDto(
            id = 1,
            name = "Producto sin oferta",
            description = "Descripción",
            price = 100000.0,
            discount = 0,
            hasDiscount = false,
            finalPrice = 100000.0,
            stock = 5,
            category = CategoryDto(1, "CONSOLAS"),
            imageUrl = "url",
            rating = 4.0
        )

        val product = productDto.toDomain()

        assertEquals(0, product.discount)
    }

    @Test
    fun `mapear producto con stock cero`() {
        val productDto = ProductDto(
            id = 1,
            name = "Producto agotado",
            description = "Descripción",
            price = 100000.0,
            discount = 0,
            hasDiscount = false,
            finalPrice = 100000.0,
            stock = 0,
            category = CategoryDto(1, "CONSOLAS"),
            imageUrl = "url",
            rating = 4.0
        )

        val product = productDto.toDomain()

        assertEquals(0, product.stock)
    }

    @Test
    fun `mapear categorias en minusculas`() {
        val productDto = ProductDto(
            id = 1,
            name = "Test",
            description = "Test",
            price = 100.0,
            discount = 0,
            hasDiscount = false,
            finalPrice = 100.0,
            stock = 1,
            category = CategoryDto(1, "consolas"),
            imageUrl = "url",
            rating = 3.0
        )

        val product = productDto.toDomain()

        assertEquals(ProductCategory.CONSOLAS, product.category)
    }

    @Test
    fun `mapear categorias en mixto`() {
        val productDto = ProductDto(
            id = 1,
            name = "Test",
            description = "Test",
            price = 100.0,
            discount = 0,
            hasDiscount = false,
            finalPrice = 100.0,
            stock = 1,
            category = CategoryDto(2, "JuEgOs"),
            imageUrl = "url",
            rating = 3.0
        )

        val product = productDto.toDomain()

        assertEquals(ProductCategory.JUEGOS, product.category)
    }

    @Test
    fun `mapear producto con rating minimo`() {
        val productDto = ProductDto(
            id = 1,
            name = "Test",
            description = "Test",
            price = 100.0,
            discount = 0,
            hasDiscount = false,
            finalPrice = 100.0,
            stock = 1,
            category = CategoryDto(1, "CONSOLAS"),
            imageUrl = "url",
            rating = 0.0
        )

        val product = productDto.toDomain()

        assertEquals(0.0, product.rating, 0.01)
    }

    @Test
    fun `mapear producto con rating maximo`() {
        val productDto = ProductDto(
            id = 1,
            name = "Test",
            description = "Test",
            price = 100.0,
            discount = 0,
            hasDiscount = false,
            finalPrice = 100.0,
            stock = 1,
            category = CategoryDto(1, "CONSOLAS"),
            imageUrl = "url",
            rating = 5.0
        )

        val product = productDto.toDomain()

        assertEquals(5.0, product.rating, 0.01)
    }
}
