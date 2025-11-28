package com.grupo10.levelupgamer.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.grupo10.levelupgamer.model.LoginErrors
import io.mockk.clearAllMocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `al cambiar email actualiza el estado y limpia errores`() {
        viewModel.onEmailChange("test@test.com")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("test@test.com", state.email)
        assertNull(state.errors.email)
        assertNull(state.loginError)
    }

    @Test
    fun `al cambiar password actualiza el estado y limpia errores`() {
        viewModel.onPasswordChange("password123")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("password123", state.password)
        assertNull(state.errors.password)
        assertNull(state.loginError)
    }

    @Test
    fun `login con email vacio muestra error de validacion`() = runTest {
        viewModel.onPasswordChange("password123")
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertNotNull(state.errors.email)
        assertEquals("Debe ingresar un correo electrónico", state.errors.email)
    }

    @Test
    fun `login con email invalido muestra error de formato`() = runTest {
        viewModel.onEmailChange("emailinvalido")
        viewModel.onPasswordChange("password123")
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertNotNull(state.errors.email)
        assertTrue(state.errors.email!!.contains("formato"))
    }

    @Test
    fun `login con password vacio muestra error de validacion`() = runTest {
        viewModel.onEmailChange("test@test.com")
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertNotNull(state.errors.password)
        assertEquals("Debe ingresar una contraseña", state.errors.password)
    }

    @Test
    fun `solicitar login biometrico actualiza el estado`() {
        viewModel.onBiometricLoginRequested()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(true, state.showBiometricPrompt)
    }

    @Test
    fun `marcar prompt biometrico como manejado actualiza el estado`() {
        viewModel.onBiometricLoginRequested()
        viewModel.onBiometricPromptHandled()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(false, state.showBiometricPrompt)
    }

    @Test
    fun `error de autenticacion biometrica actualiza el estado con error`() {
        viewModel.onBiometricAuthError("Error de huella digital")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Error de huella digital", state.loginError)
    }

    @Test
    fun `estado inicial tiene valores predeterminados correctos`() {
        val state = viewModel.state.value

        assertEquals("", state.email)
        assertEquals("", state.password)
        assertEquals(false, state.loginSuccess)
        assertNull(state.loginError)
        assertEquals(false, state.showBiometricPrompt)
        assertNull(state.userId)
    }

    @Test
    fun `isLoading es falso inicialmente`() {
        val isLoading = viewModel.isLoading.value
        assertEquals(false, isLoading)
    }

    @Test
    fun `currentUser es nulo inicialmente`() {
        val currentUser = viewModel.currentUser.value
        assertNull(currentUser)
    }

    @Test
    fun `validacion de multiples campos vacios muestra todos los errores`() = runTest {
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertNotNull(state.errors.email)
        assertNotNull(state.errors.password)
    }

    @Test
    fun `cambiar email despues de error limpia el error`() {
        viewModel.onEmailChange("")
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        val stateWithError = viewModel.state.value
        assertNotNull(stateWithError.errors.email)

        viewModel.onEmailChange("test@test.com")
        testDispatcher.scheduler.advanceUntilIdle()

        val stateWithoutError = viewModel.state.value
        assertNull(stateWithoutError.errors.email)
    }

    @Test
    fun `cambiar password despues de error limpia el error`() {
        viewModel.onPasswordChange("")
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        val stateWithError = viewModel.state.value
        assertNotNull(stateWithError.errors.password)

        viewModel.onPasswordChange("password123")
        testDispatcher.scheduler.advanceUntilIdle()

        val stateWithoutError = viewModel.state.value
        assertNull(stateWithoutError.errors.password)
    }

    @Test
    fun `email con formato valido pasa la validacion`() = runTest {
        viewModel.onEmailChange("usuario@dominio.com")
        viewModel.onPasswordChange("password123")

        val state = viewModel.state.value
        assertEquals("usuario@dominio.com", state.email)
        assertNull(state.errors.email)
    }

    @Test
    fun `LoginErrors por defecto tiene todos los campos nulos`() {
        val errors = LoginErrors()
        assertNull(errors.email)
        assertNull(errors.password)
    }

    @Test
    fun `LoginErrors puede tener un error de email`() {
        val errors = LoginErrors(email = "Error de email")
        assertEquals("Error de email", errors.email)
        assertNull(errors.password)
    }

    @Test
    fun `LoginErrors puede tener un error de password`() {
        val errors = LoginErrors(password = "Error de password")
        assertEquals("Error de password", errors.password)
        assertNull(errors.email)
    }

    @Test
    fun `LoginErrors puede tener ambos errores`() {
        val errors = LoginErrors(email = "Error de email", password = "Error de password")
        assertEquals("Error de email", errors.email)
        assertEquals("Error de password", errors.password)
    }
}

