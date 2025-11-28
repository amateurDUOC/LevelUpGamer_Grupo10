package com.grupo10.levelupgamer.model

import org.junit.Assert.assertEquals
import org.junit.Test

class StoreTest {

    @Test
    fun `crear tienda con valores predeterminados`() {
        val store = Store(
            id = 1,
            name = "Tienda Simple",
            address = "Calle 123",
            city = "Ciudad",
            latitude = -33.0,
            longitude = -71.5
        )

        assertEquals("", store.phone)
        assertEquals("Lun-Sab: 10:00 - 20:00", store.hours)
    }

    @Test
    fun `tienda con coordenadas especificas`() {
        val store = Store(
            id = 2,
            name = "Tienda Norte",
            address = "15 Norte 961",
            city = "Viña del Mar",
            latitude = -33.0180,
            longitude = -71.5470
        )

        assertEquals(-33.0180, store.latitude, 0.0001)
        assertEquals(-71.5470, store.longitude, 0.0001)
    }

    @Test
    fun `tienda con telefono personalizado`() {
        val store = Store(
            id = 3,
            name = "Tienda Test",
            address = "Direccion Test",
            city = "Ciudad Test",
            latitude = -33.0,
            longitude = -71.5,
            phone = "+56 9 1234 5678"
        )

        assertEquals("+56 9 1234 5678", store.phone)
    }

    @Test
    fun `tienda con horario personalizado`() {
        val store = Store(
            id = 4,
            name = "Tienda 24hrs",
            address = "Direccion",
            city = "Ciudad",
            latitude = -33.0,
            longitude = -71.5,
            hours = "Lun-Dom: 00:00 - 24:00"
        )

        assertEquals("Lun-Dom: 00:00 - 24:00", store.hours)
    }

    @Test
    fun `crear multiples tiendas con diferentes ids`() {
        val store1 = Store(
            id = 1,
            name = "Tienda 1",
            address = "Dirección 1",
            city = "Ciudad 1",
            latitude = -33.0,
            longitude = -71.5
        )

        val store2 = Store(
            id = 2,
            name = "Tienda 2",
            address = "Dirección 2",
            city = "Ciudad 2",
            latitude = -33.1,
            longitude = -71.6
        )

        assertEquals(1, store1.id)
        assertEquals(2, store2.id)
        assert(store1.id != store2.id)
    }

    @Test
    fun `tienda con nombre largo`() {
        val store = Store(
            id = 5,
            name = "Level Up Gamer Viña del Mar 15 Norte Centro Comercial",
            address = "15 Norte",
            city = "Viña del Mar",
            latitude = -33.0,
            longitude = -71.5
        )

        assertEquals("Level Up Gamer Viña del Mar 15 Norte Centro Comercial", store.name)
    }

    @Test
    fun `tienda con direccion completa`() {
        val store = Store(
            id = 6,
            name = "Tienda",
            address = "Avenida Brasil 2020, Oficina 301",
            city = "Valparaíso",
            latitude = -33.0458,
            longitude = -71.6197
        )

        assertEquals("Avenida Brasil 2020, Oficina 301", store.address)
    }

    @Test
    fun `tienda con coordenadas positivas`() {
        val store = Store(
            id = 7,
            name = "Tienda Internacional",
            address = "Dirección",
            city = "Ciudad",
            latitude = 40.7128,
            longitude = -74.0060
        )

        assertEquals(40.7128, store.latitude, 0.0001)
        assertEquals(-74.0060, store.longitude, 0.0001)
    }

    @Test
    fun `comparar dos tiendas con mismos valores`() {
        val store1 = Store(
            id = 1,
            name = "Tienda",
            address = "Dirección",
            city = "Ciudad",
            latitude = -33.0,
            longitude = -71.5
        )

        val store2 = Store(
            id = 1,
            name = "Tienda",
            address = "Dirección",
            city = "Ciudad",
            latitude = -33.0,
            longitude = -71.5
        )

        assertEquals(store1, store2)
    }

    @Test
    fun `datos de ejemplo contienen tiendas validas`() {
        val stores = StoresData.stores

        assert(stores.isNotEmpty())
        assertEquals(3, stores.size)

        val firstStore = stores[0]
        assertEquals(1, firstStore.id)
        assertEquals("Level Up Gamer Viña del Mar Centro", firstStore.name)
    }
}

