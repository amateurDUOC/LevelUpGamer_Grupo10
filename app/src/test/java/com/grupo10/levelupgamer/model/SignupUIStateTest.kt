package com.grupo10.levelupgamer.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SignupUIStateTest {

    @Test
    fun `SignupUIState inicial tiene valores por defecto`() {
        val state = SignupUIState()

        assertEquals("", state.name)
        assertEquals("", state.lastName)
        assertEquals("", state.rut)
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertEquals("", state.confirmPassword)
        assertEquals("", state.address)
        assertFalse(state.signupSuccess)
        assertNull(state.signupError)
        assertNull(state.userId)
    }

    @Test
    fun `SignupUIState con nombre actualizado`() {
        val state = SignupUIState(name = "Juan")

        assertEquals("Juan", state.name)
    }

    @Test
    fun `SignupUIState con apellido actualizado`() {
        val state = SignupUIState(lastName = "Pérez")

        assertEquals("Pérez", state.lastName)
    }

    @Test
    fun `SignupUIState con rut actualizado`() {
        val state = SignupUIState(rut = "12345678-9")

        assertEquals("12345678-9", state.rut)
    }

    @Test
    fun `SignupUIState con email actualizado`() {
        val state = SignupUIState(email = "test@duoc.cl")

        assertEquals("test@duoc.cl", state.email)
    }

    @Test
    fun `SignupUIState con password actualizado`() {
        val state = SignupUIState(password = "password123")

        assertEquals("password123", state.password)
    }

    @Test
    fun `SignupUIState con confirmPassword actualizado`() {
        val state = SignupUIState(confirmPassword = "password123")

        assertEquals("password123", state.confirmPassword)
    }

    @Test
    fun `SignupUIState con address actualizado`() {
        val state = SignupUIState(address = "Calle Falsa 123")

        assertEquals("Calle Falsa 123", state.address)
    }

    @Test
    fun `SignupUIState con signupSuccess true`() {
        val state = SignupUIState(signupSuccess = true)

        assertTrue(state.signupSuccess)
    }

    @Test
    fun `SignupUIState con signupError`() {
        val state = SignupUIState(signupError = "Error al registrar")

        assertEquals("Error al registrar", state.signupError)
    }

    @Test
    fun `SignupUIState con userId`() {
        val state = SignupUIState(userId = 123)

        assertEquals(123, state.userId)
    }

    @Test
    fun `SignupUIState con errores de validacion`() {
        val errors = SignupErrors(
            name = "Nombre requerido",
            lastName = "Apellido requerido",
            rut = "RUT requerido",
            email = "Email requerido",
            password = "Password requerido",
            confirmPassword = "Confirmación requerida",
            address = "Dirección requerida"
        )
        val state = SignupUIState(errors = errors)

        assertEquals("Nombre requerido", state.errors.name)
        assertEquals("Apellido requerido", state.errors.lastName)
        assertEquals("RUT requerido", state.errors.rut)
        assertEquals("Email requerido", state.errors.email)
        assertEquals("Password requerido", state.errors.password)
        assertEquals("Confirmación requerida", state.errors.confirmPassword)
        assertEquals("Dirección requerida", state.errors.address)
    }

    @Test
    fun `SignupUIState copy actualiza solo campos especificados`() {
        val state = SignupUIState(name = "Juan", lastName = "Pérez")
        val updated = state.copy(email = "juan@duoc.cl")

        assertEquals("Juan", updated.name)
        assertEquals("Pérez", updated.lastName)
        assertEquals("juan@duoc.cl", updated.email)
    }

    @Test
    fun `dos SignupUIState con mismos valores son iguales`() {
        val state1 = SignupUIState(name = "Juan", email = "test@duoc.cl")
        val state2 = SignupUIState(name = "Juan", email = "test@duoc.cl")

        assertEquals(state1, state2)
    }

    @Test
    fun `SignupErrors inicial sin errores`() {
        val errors = SignupErrors()

        assertNull(errors.name)
        assertNull(errors.lastName)
        assertNull(errors.rut)
        assertNull(errors.email)
        assertNull(errors.password)
        assertNull(errors.confirmPassword)
        assertNull(errors.address)
    }

    @Test
    fun `SignupErrors con solo error de nombre`() {
        val errors = SignupErrors(name = "Nombre inválido")

        assertEquals("Nombre inválido", errors.name)
        assertNull(errors.lastName)
        assertNull(errors.rut)
        assertNull(errors.email)
        assertNull(errors.password)
        assertNull(errors.confirmPassword)
        assertNull(errors.address)
    }

    @Test
    fun `SignupErrors con todos los errores`() {
        val errors = SignupErrors(
            name = "Error 1",
            lastName = "Error 2",
            rut = "Error 3",
            email = "Error 4",
            password = "Error 5",
            confirmPassword = "Error 6",
            address = "Error 7"
        )

        assertEquals("Error 1", errors.name)
        assertEquals("Error 2", errors.lastName)
        assertEquals("Error 3", errors.rut)
        assertEquals("Error 4", errors.email)
        assertEquals("Error 5", errors.password)
        assertEquals("Error 6", errors.confirmPassword)
        assertEquals("Error 7", errors.address)
    }

    @Test
    fun `SignupErrors copy actualiza solo error especificado`() {
        val errors = SignupErrors(name = "Nombre requerido")
        val updated = errors.copy(email = "Email requerido")

        assertEquals("Nombre requerido", updated.name)
        assertEquals("Email requerido", updated.email)
        assertNull(updated.lastName)
    }
}

