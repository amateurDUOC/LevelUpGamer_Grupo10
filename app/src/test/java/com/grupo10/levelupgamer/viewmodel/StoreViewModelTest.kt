package com.grupo10.levelupgamer.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.grupo10.levelupgamer.data.remote.dto.StoreDto
import com.grupo10.levelupgamer.data.repository.StoreRepository
import com.grupo10.levelupgamer.data.repository.StoreResult
import com.grupo10.levelupgamer.model.User
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
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
class StoreViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: StoreViewModel
    private lateinit var repository: StoreRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)

        coEvery { repository.getAllStores() } returns StoreResult.Success(emptyList())

        viewModel = StoreViewModel()

        val field = StoreViewModel::class.java.getDeclaredField("repository")
        field.isAccessible = true
        field.set(viewModel, repository)

        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `al cargar tiendas exitosamente actualiza el estado con las tiendas`() = runTest {
        val storesDto = listOf(
            StoreDto(
                id = 1,
                name = "Tienda Centro",
                address = "Calle 123",
                city = "Viña del Mar",
                latitude = -33.0245,
                longitude = -71.5516,
                phone = "+56 32 268 5000",
                hours = "Lun-Sab: 10:00 - 20:00",
                distance = null
            )
        )

        coEvery { repository.getAllStores() } returns StoreResult.Success(storesDto)

        viewModel.loadStores()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.allStores.size)
        assertEquals("Tienda Centro", state.allStores[0].name)
        assertEquals(false, state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `al cargar tiendas con error de red actualiza el estado con mensaje de error`() = runTest {
        coEvery { repository.getAllStores() } returns StoreResult.NetworkError

        viewModel.loadStores()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(true, state.allStores.isEmpty())
        assertEquals(false, state.isLoading)
        assertEquals("Error de conexión con el servidor", state.error)
    }

    @Test
    fun `al cargar tiendas con error del servidor actualiza el estado correctamente`() = runTest {
        coEvery { repository.getAllStores() } returns StoreResult.ServerError

        viewModel.loadStores()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertEquals("Error del servidor", state.error)
    }

    @Test
    fun `al cargar tiendas con error generico actualiza el estado con el mensaje`() = runTest {
        coEvery { repository.getAllStores() } returns StoreResult.Error("Error personalizado")

        viewModel.loadStores()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Error personalizado", state.error)
    }

    @Test
    fun `actualizar tienda mas cercana con usuario valido actualiza el estado`() = runTest {
        val user = User(
            id = 1,
            email = "test@test.com",
            name = "Test User",
            latitude = -33.0245,
            longitude = -71.5516
        )

        val storeDto = StoreDto(
            id = 1,
            name = "Tienda Cercana",
            address = "Calle 123",
            city = "Viña del Mar",
            latitude = -33.0250,
            longitude = -71.5520,
            phone = "+56 32 268 5000",
            hours = "Lun-Sab: 10:00 - 20:00",
            distance = 0.5
        )

        coEvery { repository.getNearestStore(user.latitude, user.longitude) } returns
            StoreResult.NearestStoreSuccess(storeDto)

        viewModel.updateNearestStore(user)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.nearestStore)
        assertEquals("Tienda Cercana", state.nearestStore?.name)
        assertEquals(0.5, state.distanceToNearest, 0.001)
    }

    @Test
    fun `actualizar tienda mas cercana con usuario nulo no actualiza el estado`() = runTest {
        val initialState = viewModel.uiState.value

        viewModel.updateNearestStore(null)
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertEquals(initialState.nearestStore, finalState.nearestStore)
    }

    @Test
    fun `actualizar tienda mas cercana con usuario sin ubicacion no actualiza el estado`() = runTest {
        coEvery { repository.getNearestStore(any(), any()) } returns StoreResult.Error("No debería llamarse")

        viewModel.updateNearestStore(null)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { repository.getNearestStore(any(), any()) }
    }

    @Test
    fun `formatear distancia menor a un kilometro muestra en metros`() {
        val formatted = viewModel.getFormattedDistance(0.5)
        assertEquals("500 m", formatted)
    }

    @Test
    fun `formatear distancia mayor a un kilometro muestra en kilometros`() {
        val formatted = viewModel.getFormattedDistance(2.5)
        assertTrue(formatted.contains("km"))
        assertTrue(formatted.contains("2.5") || formatted.contains("2,5"))
    }

    @Test
    fun `formatear distancia de exactamente un kilometro`() {
        val formatted = viewModel.getFormattedDistance(1.0)
        assertTrue(formatted.contains("km"))
    }

    @Test
    fun `estado inicial tiene valores predeterminados correctos`() {
        val state = StoreUIState()

        assertNull(state.nearestStore)
        assertTrue(state.allStores.isEmpty())
        assertEquals(0.0, state.distanceToNearest, 0.001)
        assertEquals(false, state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `durante la carga de tiendas el estado isLoading es verdadero`() = runTest {
        val deferred = CompletableDeferred<StoreResult>()
        coEvery { repository.getAllStores() } coAnswers { deferred.await() }

        viewModel.loadStores()
        testDispatcher.scheduler.runCurrent()

        assertEquals(true, viewModel.uiState.value.isLoading)

        deferred.complete(StoreResult.Success(emptyList()))
        testDispatcher.scheduler.advanceUntilIdle()
    }
}

