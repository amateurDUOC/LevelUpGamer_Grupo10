package com.grupo10.levelupgamer.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.grupo10.levelupgamer.data.remote.service.AddressSuggestion
import io.mockk.clearAllMocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignupViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: SignupViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SignupViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `estado inicial es correcto`() {
        val state = viewModel.state.value
        assertEquals("", state.name)
        assertEquals("", state.lastName)
        assertEquals("", state.rut)
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertEquals("", state.confirmPassword)
        assertEquals("", state.address)
        assertFalse(state.signupSuccess)
        assertNull(state.errors.name)
        assertNull(state.errors.lastName)
    }

    @Test
    fun `onNameChange actualiza nombre y limpia error`() {
        viewModel.onNameChange("Juan")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Juan", state.name)
        assertNull(state.errors.name)
    }

    @Test
    fun `onLastNameChange actualiza apellido y limpia error`() {
        viewModel.onLastNameChange("Pérez")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Pérez", state.lastName)
        assertNull(state.errors.lastName)
    }

    @Test
    fun `onRutChange actualiza rut y limpia error`() {
        viewModel.onRutChange("12345678-9")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("12345678-9", state.rut)
        assertNull(state.errors.rut)
    }

    @Test
    fun `onEmailChange actualiza email y limpia error`() {
        viewModel.onEmailChange("test@duoc.cl")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("test@duoc.cl", state.email)
        assertNull(state.errors.email)
    }

    @Test
    fun `onPasswordChange actualiza password y limpia error`() {
        viewModel.onPasswordChange("password123")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("password123", state.password)
        assertNull(state.errors.password)
    }

    @Test
    fun `onConfirmPasswordChange actualiza confirmPassword y limpia error`() {
        viewModel.onConfirmPasswordChange("password123")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("password123", state.confirmPassword)
        assertNull(state.errors.confirmPassword)
    }

    @Test
    fun `onAddressChange actualiza address y limpia error`() {
        viewModel.onAddressChange("Calle Falsa 123")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Calle Falsa 123", state.address)
        assertNull(state.errors.address)
    }

    @Test
    fun `validateSignupForm retorna false cuando campos estan vacios`() {
        val result = viewModel.validateSignupForm()

        assertFalse(result)
        val state = viewModel.state.value
        assertNotNull(state.errors.name)
        assertNotNull(state.errors.lastName)
        assertNotNull(state.errors.rut)
        assertNotNull(state.errors.email)
        assertNotNull(state.errors.password)
        assertNotNull(state.errors.confirmPassword)
        assertNotNull(state.errors.address)
    }

    @Test
    fun `validateSignupForm retorna false cuando email no es duoc o gmail`() {
        viewModel.onNameChange("Juan")
        viewModel.onLastNameChange("Pérez")
        viewModel.onRutChange("12345678-9")
        viewModel.onEmailChange("test@yahoo.com")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")
        viewModel.onAddressChange("Calle Falsa 123")

        val result = viewModel.validateSignupForm()

        assertFalse(result)
        val state = viewModel.state.value
        assertNotNull(state.errors.email)
        assertEquals("Solo se permiten correos @duoc.cl o @gmail.com", state.errors.email)
    }

    @Test
    fun `validateSignupForm retorna false cuando passwords no coinciden`() {
        viewModel.onNameChange("Juan")
        viewModel.onLastNameChange("Pérez")
        viewModel.onRutChange("12345678-9")
        viewModel.onEmailChange("test@duoc.cl")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password456")
        viewModel.onAddressChange("Calle Falsa 123")

        val result = viewModel.validateSignupForm()

        assertFalse(result)
        val state = viewModel.state.value
        assertNotNull(state.errors.confirmPassword)
        assertEquals("Las contraseñas no coinciden", state.errors.confirmPassword)
    }

    @Test
    fun `validateSignupForm retorna true cuando todos los campos son validos con email duoc`() {
        viewModel.onNameChange("Juan")
        viewModel.onLastNameChange("Pérez")
        viewModel.onRutChange("12345678-9")
        viewModel.onEmailChange("test@duoc.cl")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")
        viewModel.onAddressChange("Calle Falsa 123")

        val result = viewModel.validateSignupForm()

        assertTrue(result)
        val state = viewModel.state.value
        assertNull(state.errors.name)
        assertNull(state.errors.lastName)
        assertNull(state.errors.rut)
        assertNull(state.errors.email)
        assertNull(state.errors.password)
        assertNull(state.errors.confirmPassword)
        assertNull(state.errors.address)
    }

    @Test
    fun `validateSignupForm retorna true cuando todos los campos son validos con email gmail`() {
        viewModel.onNameChange("Juan")
        viewModel.onLastNameChange("Pérez")
        viewModel.onRutChange("12345678-9")
        viewModel.onEmailChange("test@gmail.com")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")
        viewModel.onAddressChange("Calle Falsa 123")

        val result = viewModel.validateSignupForm()

        assertTrue(result)
    }

    @Test
    fun `clearAddressSuggestions limpia las sugerencias`() {
        viewModel.clearAddressSuggestions()
        testDispatcher.scheduler.advanceUntilIdle()

        val suggestions = viewModel.addressSuggestions.value
        assertTrue(suggestions.isEmpty())
    }

    @Test
    fun `selectAddressSuggestion actualiza la direccion y limpia sugerencias`() {
        val suggestion = AddressSuggestion(
            displayName = "Av. Siempre Viva 742",
            latitude = -33.4489,
            longitude = -70.6693
        )

        viewModel.selectAddressSuggestion(suggestion)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Av. Siempre Viva 742", state.address)
        assertTrue(viewModel.addressSuggestions.value.isEmpty())
    }

    @Test
    fun `isLoading estado inicial es false`() {
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `isLoadingSuggestions estado inicial es false`() {
        assertFalse(viewModel.isLoadingSuggestions.value)
    }

    @Test
    fun `addressSuggestions estado inicial esta vacio`() {
        assertTrue(viewModel.addressSuggestions.value.isEmpty())
    }

    @Test
    fun `registeredUser estado inicial es null`() {
        assertNull(viewModel.registeredUser.value)
    }

    @Test
    fun `onAddressChange con menos de 3 caracteres no busca sugerencias`() = runTest {
        viewModel.onAddressChange("Ab")
        advanceTimeBy(600) // Esperar más que el debounce

        assertTrue(viewModel.addressSuggestions.value.isEmpty())
    }

    @Test
    fun `validateSignupForm con email vacio retorna error apropiado`() {
        viewModel.onNameChange("Juan")
        viewModel.onLastNameChange("Pérez")
        viewModel.onRutChange("12345678-9")
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")
        viewModel.onAddressChange("Calle Falsa 123")

        val result = viewModel.validateSignupForm()

        assertFalse(result)
        val state = viewModel.state.value
        assertEquals("Debe ingresar un correo electrónico", state.errors.email)
    }

    @Test
    fun `validateSignupForm con confirmPassword vacio retorna error apropiado`() {
        viewModel.onNameChange("Juan")
        viewModel.onLastNameChange("Pérez")
        viewModel.onRutChange("12345678-9")
        viewModel.onEmailChange("test@duoc.cl")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("")
        viewModel.onAddressChange("Calle Falsa 123")

        val result = viewModel.validateSignupForm()

        assertFalse(result)
        val state = viewModel.state.value
        assertEquals("Debe confirmar la contraseña", state.errors.confirmPassword)
    }
}

