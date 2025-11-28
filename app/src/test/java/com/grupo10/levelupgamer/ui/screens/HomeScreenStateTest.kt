package com.grupo10.levelupgamer.ui.screens

import com.grupo10.levelupgamer.model.Product
import com.grupo10.levelupgamer.model.ProductCategory
import com.grupo10.levelupgamer.viewmodel.HomeUIState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeScreenStateTest {

    @Test
    fun `HomeUIState inicial tiene valores por defecto`() {
        val state = HomeUIState()

        assertEquals("", state.searchQuery)
        assertNull(state.selectedProduct)
        assertTrue(state.allProducts.isEmpty())
        assertTrue(state.filteredProducts.isEmpty())
        assertTrue(state.productsOnSale.isEmpty())
        assertEquals(3, state.notificationCount)
        assertNull(state.error)
        assertFalse(state.isNetworkError)
        assertFalse(state.isLoading)
    }

    @Test
    fun `HomeUIState con productos actualiza correctamente`() {
        val products = listOf(
            Product(1, "PS5", 499990.0, "Consola", ProductCategory.CONSOLAS),
            Product(2, "Xbox", 479990.0, "Consola", ProductCategory.CONSOLAS)
        )

        val state = HomeUIState(
            allProducts = products,
            filteredProducts = products
        )

        assertEquals(2, state.allProducts.size)
        assertEquals(2, state.filteredProducts.size)
    }

    @Test
    fun `HomeUIState con producto seleccionado`() {
        val product = Product(1, "PS5", 499990.0, "Consola", ProductCategory.CONSOLAS)
        val state = HomeUIState(selectedProduct = product)

        assertEquals(product, state.selectedProduct)
    }

    @Test
    fun `HomeUIState con query de busqueda`() {
        val state = HomeUIState(searchQuery = "PlayStation")

        assertEquals("PlayStation", state.searchQuery)
    }

    @Test
    fun `HomeUIState con error de red`() {
        val state = HomeUIState(
            error = "Error de conexión",
            isNetworkError = true
        )

        assertEquals("Error de conexión", state.error)
        assertTrue(state.isNetworkError)
    }

    @Test
    fun `HomeUIState con productos en oferta`() {
        val productsOnSale = listOf(
            Product(1, "PS5", 499990.0, "Consola", ProductCategory.CONSOLAS, discount = 10)
        )

        val state = HomeUIState(productsOnSale = productsOnSale)

        assertEquals(1, state.productsOnSale.size)
        assertTrue(state.productsOnSale[0].hasDiscount)
    }

    @Test
    fun `HomeUIState con contador de notificaciones`() {
        val state = HomeUIState(notificationCount = 5)

        assertEquals(5, state.notificationCount)
    }

    @Test
    fun `HomeUIState isLoading actualiza correctamente`() {
        val state = HomeUIState(isLoading = true)

        assertTrue(state.isLoading)
    }

    @Test
    fun `HomeUIState copy actualiza solo campos especificados`() {
        val originalState = HomeUIState(searchQuery = "test")
        val updatedState = originalState.copy(isLoading = true)

        assertEquals("test", updatedState.searchQuery)
        assertTrue(updatedState.isLoading)
    }

    @Test
    fun `HomeUIState con multiples productos filtrados`() {
        val allProducts = listOf(
            Product(1, "PlayStation 5", 499990.0, "Consola", ProductCategory.CONSOLAS),
            Product(2, "Xbox Series X", 479990.0, "Consola", ProductCategory.CONSOLAS),
            Product(3, "God of War", 59990.0, "Juego", ProductCategory.JUEGOS)
        )

        val filteredProducts = allProducts.filter { it.category == ProductCategory.CONSOLAS }

        val state = HomeUIState(
            allProducts = allProducts,
            filteredProducts = filteredProducts
        )

        assertEquals(3, state.allProducts.size)
        assertEquals(2, state.filteredProducts.size)
    }
}

