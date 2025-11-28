package com.grupo10.levelupgamer.data.mapper

import com.grupo10.levelupgamer.data.remote.dto.StoreDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class StoreMapperTest {

    @Test
    fun `mapear StoreDto a Store convierte correctamente todos los campos`() {
        val storeDto = StoreDto(
            id = 1,
            name = "Level Up Gamer Santiago Centro",
            address = "Av. Libertador Bernardo O'Higgins 1234",
            city = "Santiago",
            latitude = -33.4489,
            longitude = -70.6693,
            phone = "+56 2 1234 5678",
            hours = "Lunes a Viernes: 10:00 - 20:00"
        )

        val store = storeDto.toDomain()

        assertEquals(1, store.id)
        assertEquals("Level Up Gamer Santiago Centro", store.name)
        assertEquals("Av. Libertador Bernardo O'Higgins 1234", store.address)
        assertEquals("Santiago", store.city)
        assertEquals(-33.4489, store.latitude, 0.0001)
        assertEquals(-70.6693, store.longitude, 0.0001)
        assertEquals("+56 2 1234 5678", store.phone)
        assertEquals("Lunes a Viernes: 10:00 - 20:00", store.hours)
    }

    @Test
    fun `mapear lista vacia de StoreDto retorna lista vacia`() {
        val emptyList = emptyList<StoreDto>()

        val result = emptyList.toDomain()

        assertNotNull(result)
        assertEquals(0, result.size)
    }

    @Test
    fun `mapear lista de StoreDto a lista de Store`() {
        val storeDtos = listOf(
            StoreDto(
                id = 1,
                name = "Tienda 1",
                address = "Dirección 1",
                city = "Santiago",
                latitude = -33.4489,
                longitude = -70.6693,
                phone = "123456789",
                hours = "9:00 - 18:00"
            ),
            StoreDto(
                id = 2,
                name = "Tienda 2",
                address = "Dirección 2",
                city = "Valparaíso",
                latitude = -33.0472,
                longitude = -71.6127,
                phone = "987654321",
                hours = "10:00 - 20:00"
            ),
            StoreDto(
                id = 3,
                name = "Tienda 3",
                address = "Dirección 3",
                city = "Concepción",
                latitude = -36.8201,
                longitude = -73.0444,
                phone = "555555555",
                hours = "9:30 - 19:30"
            )
        )

        val stores = storeDtos.toDomain()

        assertEquals(3, stores.size)
        assertEquals("Tienda 1", stores[0].name)
        assertEquals("Tienda 2", stores[1].name)
        assertEquals("Tienda 3", stores[2].name)
        assertEquals("Santiago", stores[0].city)
        assertEquals("Valparaíso", stores[1].city)
        assertEquals("Concepción", stores[2].city)
    }

    @Test
    fun `mapear tienda con coordenadas positivas`() {
        val storeDto = StoreDto(
            id = 1,
            name = "Tienda Internacional",
            address = "Address",
            city = "City",
            latitude = 40.7128,
            longitude = 74.0060,
            phone = "1234567890",
            hours = "24/7"
        )

        val store = storeDto.toDomain()

        assertEquals(40.7128, store.latitude, 0.0001)
        assertEquals(74.0060, store.longitude, 0.0001)
    }

    @Test
    fun `mapear tienda con nombre largo`() {
        val longName = "Level Up Gamer - Tienda Principal Centro Comercial Mall Plaza Santiago"
        val storeDto = StoreDto(
            id = 1,
            name = longName,
            address = "Address",
            city = "Santiago",
            latitude = -33.4489,
            longitude = -70.6693,
            phone = "123456789",
            hours = "10:00 - 21:00"
        )

        val store = storeDto.toDomain()

        assertEquals(longName, store.name)
    }

    @Test
    fun `mapear tienda con direccion larga`() {
        val longAddress = "Avenida Libertador General Bernardo O'Higgins número 1234, Piso 5, Local 123"
        val storeDto = StoreDto(
            id = 1,
            name = "Tienda",
            address = longAddress,
            city = "Santiago",
            latitude = -33.4489,
            longitude = -70.6693,
            phone = "123456789",
            hours = "10:00 - 21:00"
        )

        val store = storeDto.toDomain()

        assertEquals(longAddress, store.address)
    }

    @Test
    fun `mapear tienda con horario extendido`() {
        val hours = "Lunes a Jueves: 10:00 - 20:00, Viernes y Sábado: 10:00 - 22:00, Domingo: 11:00 - 19:00"
        val storeDto = StoreDto(
            id = 1,
            name = "Tienda",
            address = "Address",
            city = "Santiago",
            latitude = -33.4489,
            longitude = -70.6693,
            phone = "123456789",
            hours = hours
        )

        val store = storeDto.toDomain()

        assertEquals(hours, store.hours)
    }

    @Test
    fun `mapear tienda con telefono con formato internacional`() {
        val phone = "+56 9 8765 4321"
        val storeDto = StoreDto(
            id = 1,
            name = "Tienda",
            address = "Address",
            city = "Santiago",
            latitude = -33.4489,
            longitude = -70.6693,
            phone = phone,
            hours = "10:00 - 20:00"
        )

        val store = storeDto.toDomain()

        assertEquals(phone, store.phone)
    }

    @Test
    fun `mapear tienda con ciudad diferente`() {
        val cities = listOf("Santiago", "Valparaíso", "Concepción", "La Serena", "Temuco")

        cities.forEachIndexed { index, city ->
            val storeDto = StoreDto(
                id = index + 1,
                name = "Tienda $city",
                address = "Dirección en $city",
                city = city,
                latitude = -30.0 - index,
                longitude = -70.0 - index,
                phone = "12345678$index",
                hours = "10:00 - 20:00"
            )

            val store = storeDto.toDomain()
            assertEquals(city, store.city)
        }
    }

    @Test
    fun `mapear tienda con id grande`() {
        val storeDto = StoreDto(
            id = 999999,
            name = "Tienda",
            address = "Address",
            city = "Santiago",
            latitude = -33.4489,
            longitude = -70.6693,
            phone = "123456789",
            hours = "10:00 - 20:00"
        )

        val store = storeDto.toDomain()

        assertEquals(999999, store.id)
    }

    @Test
    fun `mapear tiendas con coordenadas muy precisas`() {
        val storeDto = StoreDto(
            id = 1,
            name = "Tienda",
            address = "Address",
            city = "Santiago",
            latitude = -33.44889912,
            longitude = -70.66930075,
            phone = "123456789",
            hours = "10:00 - 20:00"
        )

        val store = storeDto.toDomain()

        assertEquals(-33.44889912, store.latitude, 0.00000001)
        assertEquals(-70.66930075, store.longitude, 0.00000001)
    }

    @Test
    fun `mapear multiples tiendas preserva el orden`() {
        val storeDtos = (1..10).map { i ->
            StoreDto(
                id = i,
                name = "Tienda $i",
                address = "Dirección $i",
                city = "Ciudad $i",
                latitude = -33.0 - i,
                longitude = -70.0 - i,
                phone = "12345678$i",
                hours = "10:00 - 20:00"
            )
        }

        val stores = storeDtos.toDomain()

        assertEquals(10, stores.size)
        stores.forEachIndexed { index, store ->
            assertEquals(index + 1, store.id)
            assertEquals("Tienda ${index + 1}", store.name)
        }
    }

    @Test
    fun `mapear tienda con campos vacios`() {
        val storeDto = StoreDto(
            id = 1,
            name = "",
            address = "",
            city = "",
            latitude = 0.0,
            longitude = 0.0,
            phone = "",
            hours = ""
        )

        val store = storeDto.toDomain()

        assertEquals("", store.name)
        assertEquals("", store.address)
        assertEquals("", store.city)
        assertEquals("", store.phone)
        assertEquals("", store.hours)
    }
}

