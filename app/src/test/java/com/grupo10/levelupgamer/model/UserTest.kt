package com.grupo10.levelupgamer.model

import org.junit.Assert.assertEquals
import org.junit.Test

class UserTest {

    @Test
    fun `crear usuario con todos los atributos`() {
        val user = User(
            id = 1,
            email = "test@test.com",
            name = "Usuario Test",
            address = "Calle Falsa 123",
            latitude = -33.0245,
            longitude = -71.5516
        )

        assertEquals(1, user.id)
        assertEquals("test@test.com", user.email)
        assertEquals("Usuario Test", user.name)
        assertEquals("Calle Falsa 123", user.address)
        assertEquals(-33.0245, user.latitude, 0.0001)
        assertEquals(-71.5516, user.longitude, 0.0001)
    }

    @Test
    fun `crear usuario con valores predeterminados`() {
        val user = User(
            id = 1,
            email = "test@test.com"
        )

        assertEquals("", user.name)
        assertEquals("", user.address)
        assertEquals(0.0, user.latitude, 0.0001)
        assertEquals(0.0, user.longitude, 0.0001)
    }

    @Test
    fun `usuario con email valido`() {
        val user = User(
            id = 1,
            email = "usuario@dominio.com"
        )

        assertEquals("usuario@dominio.com", user.email)
    }

    @Test
    fun `usuario con nombre completo`() {
        val user = User(
            id = 1,
            email = "test@test.com",
            name = "Juan Pablo González Martínez"
        )

        assertEquals("Juan Pablo González Martínez", user.name)
    }

    @Test
    fun `usuario con direccion completa`() {
        val user = User(
            id = 1,
            email = "test@test.com",
            address = "Av. Principal 1234, Depto 567, Comuna, Ciudad"
        )

        assertEquals("Av. Principal 1234, Depto 567, Comuna, Ciudad", user.address)
    }

    @Test
    fun `usuario con coordenadas de Vina del Mar`() {
        val user = User(
            id = 1,
            email = "test@test.com",
            latitude = -33.0245,
            longitude = -71.5516
        )

        assertEquals(-33.0245, user.latitude, 0.0001)
        assertEquals(-71.5516, user.longitude, 0.0001)
    }

    @Test
    fun `usuario con coordenadas de Valparaiso`() {
        val user = User(
            id = 1,
            email = "test@test.com",
            latitude = -33.0458,
            longitude = -71.6197
        )

        assertEquals(-33.0458, user.latitude, 0.0001)
        assertEquals(-71.6197, user.longitude, 0.0001)
    }

    @Test
    fun `usuario sin coordenadas tiene valores cero`() {
        val user = User(
            id = 1,
            email = "test@test.com"
        )

        assertEquals(0.0, user.latitude, 0.0001)
        assertEquals(0.0, user.longitude, 0.0001)
    }

    @Test
    fun `crear multiples usuarios con diferentes ids`() {
        val user1 = User(id = 1, email = "user1@test.com")
        val user2 = User(id = 2, email = "user2@test.com")

        assertEquals(1, user1.id)
        assertEquals(2, user2.id)
        assert(user1.id != user2.id)
    }

    @Test
    fun `usuario con email en mayusculas`() {
        val user = User(
            id = 1,
            email = "USUARIO@DOMINIO.COM"
        )

        assertEquals("USUARIO@DOMINIO.COM", user.email)
    }

    @Test
    fun `usuario con nombre vacio pero con direccion`() {
        val user = User(
            id = 1,
            email = "test@test.com",
            name = "",
            address = "Dirección completa"
        )

        assertEquals("", user.name)
        assertEquals("Dirección completa", user.address)
    }

    @Test
    fun `usuario con direccion vacia pero con nombre`() {
        val user = User(
            id = 1,
            email = "test@test.com",
            name = "Nombre Completo",
            address = ""
        )

        assertEquals("Nombre Completo", user.name)
        assertEquals("", user.address)
    }

    @Test
    fun `comparar dos usuarios con mismos valores`() {
        val user1 = User(
            id = 1,
            email = "test@test.com",
            name = "Test User"
        )

        val user2 = User(
            id = 1,
            email = "test@test.com",
            name = "Test User"
        )

        assertEquals(user1, user2)
    }

    @Test
    fun `usuario con coordenadas positivas`() {
        val user = User(
            id = 1,
            email = "test@test.com",
            latitude = 40.7128,
            longitude = -74.0060
        )

        assertEquals(40.7128, user.latitude, 0.0001)
        assertEquals(-74.0060, user.longitude, 0.0001)
    }

    // ...existing code...

    @Test
    fun `usuario solo con email requerido`() {
        val user = User(id = 1, email = "minimal@test.com")

        assertEquals(1, user.id)
        assertEquals("minimal@test.com", user.email)
        assertEquals("", user.name)
        assertEquals("", user.address)
        assertEquals(0.0, user.latitude, 0.0001)
        assertEquals(0.0, user.longitude, 0.0001)
    }
}

