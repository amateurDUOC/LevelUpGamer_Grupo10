package com.grupo10.levelupgamer.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.grupo10.levelupgamer.model.User
import io.mockk.clearAllMocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
class NavigationViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: NavigationViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = NavigationViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `estado inicial es correcto`() {
        assertNull(viewModel.currentUser.value)
        assertFalse(viewModel.isUserAuthenticated.value)
    }

    @Test
    fun `setCurrentUser actualiza el usuario y estado de autenticacion`() {
        val user = User(
            id = 1,
            email = "test@duoc.cl",
            name = "Test User",
            address = "Calle Falsa 123",
            latitude = -33.4489,
            longitude = -70.6693
        )

        viewModel.setCurrentUser(user)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(user, viewModel.currentUser.value)
        assertTrue(viewModel.isUserAuthenticated.value)
    }

    @Test
    fun `updateUserAddress actualiza la direccion del usuario`() {
        val user = User(
            id = 1,
            email = "test@duoc.cl",
            name = "Test User",
            address = "Calle Falsa 123",
            latitude = -33.4489,
            longitude = -70.6693
        )

        viewModel.setCurrentUser(user)
        viewModel.updateUserAddress("Nueva Direccion", -33.5, -70.7)
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedUser = viewModel.currentUser.value
        assertNotNull(updatedUser)
        assertEquals("Nueva Direccion", updatedUser?.address)
        assertEquals(-33.5, updatedUser?.latitude ?: 0.0, 0.001)
        assertEquals(-70.7, updatedUser?.longitude ?: 0.0, 0.001)
    }

    @Test
    fun `updateUserAddress sin usuario actual no hace nada`() {
        viewModel.updateUserAddress("Nueva Direccion", -33.5, -70.7)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.currentUser.value)
    }

    @Test
    fun `logout limpia el usuario y estado de autenticacion`() {
        val user = User(
            id = 1,
            email = "test@duoc.cl",
            name = "Test User",
            address = "Calle Falsa 123",
            latitude = -33.4489,
            longitude = -70.6693
        )

        viewModel.setCurrentUser(user)
        viewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.currentUser.value)
        assertFalse(viewModel.isUserAuthenticated.value)
    }

    @Test
    fun `getCurrentUser retorna el usuario actual`() {
        val user = User(
            id = 1,
            email = "test@duoc.cl",
            name = "Test User",
            address = "Calle Falsa 123",
            latitude = -33.4489,
            longitude = -70.6693
        )

        viewModel.setCurrentUser(user)
        testDispatcher.scheduler.advanceUntilIdle()

        val currentUser = viewModel.getCurrentUser()
        assertEquals(user, currentUser)
    }

    @Test
    fun `getCurrentUser retorna null cuando no hay usuario`() {
        val currentUser = viewModel.getCurrentUser()
        assertNull(currentUser)
    }

    @Test
    fun `setCurrentUser preserva todos los campos del usuario`() {
        val user = User(
            id = 123,
            email = "usuario@gmail.com",
            name = "Juan Pérez",
            address = "Av. Principal 456",
            latitude = -33.123,
            longitude = -70.456
        )

        viewModel.setCurrentUser(user)
        testDispatcher.scheduler.advanceUntilIdle()

        val storedUser = viewModel.currentUser.value
        assertNotNull(storedUser)
        assertEquals(123, storedUser?.id)
        assertEquals("usuario@gmail.com", storedUser?.email)
        assertEquals("Juan Pérez", storedUser?.name)
        assertEquals("Av. Principal 456", storedUser?.address)
        assertEquals(-33.123, storedUser?.latitude ?: 0.0, 0.001)
        assertEquals(-70.456, storedUser?.longitude ?: 0.0, 0.001)
    }

    @Test
    fun `updateUserAddress preserva los demas campos del usuario`() {
        val user = User(
            id = 1,
            email = "test@duoc.cl",
            name = "Test User",
            address = "Calle Falsa 123",
            latitude = -33.4489,
            longitude = -70.6693
        )

        viewModel.setCurrentUser(user)
        viewModel.updateUserAddress("Direccion Actualizada", -34.0, -71.0)
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedUser = viewModel.currentUser.value
        assertEquals(1, updatedUser?.id)
        assertEquals("test@duoc.cl", updatedUser?.email)
        assertEquals("Test User", updatedUser?.name)
        assertEquals("Direccion Actualizada", updatedUser?.address)
    }

    @Test
    fun `multiples llamadas a setCurrentUser actualizan correctamente`() {
        val user1 = User(
            id = 1,
            email = "user1@duoc.cl",
            name = "User 1",
            address = "Address 1",
            latitude = -33.0,
            longitude = -70.0
        )

        val user2 = User(
            id = 2,
            email = "user2@duoc.cl",
            name = "User 2",
            address = "Address 2",
            latitude = -34.0,
            longitude = -71.0
        )

        viewModel.setCurrentUser(user1)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(user1, viewModel.currentUser.value)

        viewModel.setCurrentUser(user2)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(user2, viewModel.currentUser.value)
    }
}

