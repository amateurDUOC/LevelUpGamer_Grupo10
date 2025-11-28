package com.grupo10.levelupgamer.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ProductCategoryTest {

    @Test
    fun `ProductCategory CONSOLAS tiene el nombre correcto`() {
        assertEquals("CONSOLAS", ProductCategory.CONSOLAS.name)
    }

    @Test
    fun `ProductCategory JUEGOS tiene el nombre correcto`() {
        assertEquals("JUEGOS", ProductCategory.JUEGOS.name)
    }

    @Test
    fun `ProductCategory ACCESORIOS tiene el nombre correcto`() {
        assertEquals("ACCESORIOS", ProductCategory.ACCESORIOS.name)
    }

    @Test
    fun `ProductCategory todas las categorias son unicas`() {
        val categories = ProductCategory.values()

        assertEquals(3, categories.size)
        assert(categories.contains(ProductCategory.CONSOLAS))
        assert(categories.contains(ProductCategory.JUEGOS))
        assert(categories.contains(ProductCategory.ACCESORIOS))
    }

    @Test
    fun `ProductCategory valores tienen nombres diferentes`() {
        val consolas = ProductCategory.CONSOLAS
        val juegos = ProductCategory.JUEGOS
        val accesorios = ProductCategory.ACCESORIOS

        assertNotEquals(consolas.name, juegos.name)
        assertNotEquals(juegos.name, accesorios.name)
        assertNotEquals(consolas.name, accesorios.name)
    }

    @Test
    fun `ProductCategory obtener por nombre enum`() {
        assertEquals(ProductCategory.CONSOLAS, ProductCategory.valueOf("CONSOLAS"))
        assertEquals(ProductCategory.JUEGOS, ProductCategory.valueOf("JUEGOS"))
        assertEquals(ProductCategory.ACCESORIOS, ProductCategory.valueOf("ACCESORIOS"))
    }

    @Test
    fun `ProductCategory valores tienen orden correcto`() {
        val values = ProductCategory.values()

        assertEquals(ProductCategory.CONSOLAS, values[0])
        assertEquals(ProductCategory.JUEGOS, values[1])
        assertEquals(ProductCategory.ACCESORIOS, values[2])
    }

    @Test
    fun `ProductCategory comparacion por identidad`() {
        val category1 = ProductCategory.CONSOLAS
        val category2 = ProductCategory.CONSOLAS

        assertEquals(category1, category2)
        assert(category1 === category2)
    }

    @Test
    fun `ProductCategory name no es vacio`() {
        ProductCategory.values().forEach { category ->
            assert(category.name.isNotEmpty())
        }
    }

    @Test
    fun `ProductCategory ordinal correcto`() {
        assertEquals(0, ProductCategory.CONSOLAS.ordinal)
        assertEquals(1, ProductCategory.JUEGOS.ordinal)
        assertEquals(2, ProductCategory.ACCESORIOS.ordinal)
    }
}

