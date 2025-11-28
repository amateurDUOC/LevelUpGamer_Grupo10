package com.grupo10.levelupgamer.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginUIStateTest {

    @Test
    fun `LoginUIState inicial tiene valores por defecto`() {
        val state = LoginUIState()

        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.loginSuccess)
        assertEquals(null, state.loginError)
        assertEquals(null, state.errors.email)
        assertEquals(null, state.errors.password)
    }

    @Test
    fun `LoginUIState con email actualizado`() {
        val state = LoginUIState(email = "test@duoc.cl")

        assertEquals("test@duoc.cl", state.email)
        assertEquals("", state.password)
    }

    @Test
    fun `LoginUIState con password actualizado`() {
        val state = LoginUIState(password = "password123")

        assertEquals("", state.email)
        assertEquals("password123", state.password)
    }

    @Test
    fun `LoginUIState con loginSuccess true`() {
        val state = LoginUIState(loginSuccess = true)

        assertTrue(state.loginSuccess)
    }

    @Test
    fun `LoginUIState con loginError`() {
        val state = LoginUIState(loginError = "Error de login")

        assertEquals("Error de login", state.loginError)
    }

    @Test
    fun `LoginUIState con errores de validacion`() {
        val errors = LoginErrors(
            email = "Email requerido",
            password = "Password requerido"
        )
        val state = LoginUIState(errors = errors)

        assertEquals("Email requerido", state.errors.email)
        assertEquals("Password requerido", state.errors.password)
    }

    @Test
    fun `LoginUIState copy actualiza solo campos especificados`() {
        val state = LoginUIState(email = "test@duoc.cl", password = "pass123")
        val updated = state.copy(loginSuccess = true)

        assertEquals("test@duoc.cl", updated.email)
        assertEquals("pass123", updated.password)
        assertTrue(updated.loginSuccess)
    }

    @Test
    fun `dos LoginUIState con mismos valores son iguales`() {
        val state1 = LoginUIState(email = "test@duoc.cl", password = "pass123")
        val state2 = LoginUIState(email = "test@duoc.cl", password = "pass123")

        assertEquals(state1, state2)
    }

    @Test
    fun `LoginErrors inicial sin errores`() {
        val errors = LoginErrors()

        assertEquals(null, errors.email)
        assertEquals(null, errors.password)
    }

    @Test
    fun `LoginErrors con error de email`() {
        val errors = LoginErrors(email = "Email inválido")

        assertEquals("Email inválido", errors.email)
        assertEquals(null, errors.password)
    }

    @Test
    fun `LoginErrors con error de password`() {
        val errors = LoginErrors(password = "Password muy corto")

        assertEquals(null, errors.email)
        assertEquals("Password muy corto", errors.password)
    }

    @Test
    fun `LoginErrors con ambos errores`() {
        val errors = LoginErrors(
            email = "Email requerido",
            password = "Password requerido"
        )

        assertEquals("Email requerido", errors.email)
        assertEquals("Password requerido", errors.password)
    }
}

