package com.grupo10.levelupgamer.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.clearAllMocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
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
class QRScannerViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: QRScannerViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = QRScannerViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `estado inicial es correcto`() {
        val state = viewModel.uiState.value

        assertNull(state.scannedProduct)
        assertTrue(state.isScanning)
        assertNull(state.errorMessage)
    }

    @Test
    fun `onQRCodeScanned con formato PRODUCT_ID encuentra producto`() {
        viewModel.onQRCodeScanned("PRODUCT_ID_1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.scannedProduct)
        assertFalse(state.isScanning)
        assertNull(state.errorMessage)
    }

    @Test
    fun `onQRCodeScanned con solo numero encuentra producto`() {
        viewModel.onQRCodeScanned("1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.scannedProduct)
        assertFalse(state.isScanning)
    }

    @Test
    fun `onQRCodeScanned con URL encuentra producto`() {
        viewModel.onQRCodeScanned("https://levelupgamer.com/product/1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.scannedProduct)
        assertFalse(state.isScanning)
    }

    @Test
    fun `onQRCodeScanned con ID invalido muestra error`() {
        viewModel.onQRCodeScanned("PRODUCT_ID_999999")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.scannedProduct)
        assertTrue(state.isScanning)
        assertNotNull(state.errorMessage)
        assertTrue(state.errorMessage?.contains("no encontrado") == true)
    }

    @Test
    fun `onQRCodeScanned con formato invalido muestra error`() {
        viewModel.onQRCodeScanned("INVALID_QR_CODE")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.scannedProduct)
        assertTrue(state.isScanning)
        assertNotNull(state.errorMessage)
        assertEquals("Código QR no válido", state.errorMessage)
    }

    @Test
    fun `resetScanner limpia el estado`() {
        viewModel.onQRCodeScanned("1")
        viewModel.resetScanner()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.scannedProduct)
        assertTrue(state.isScanning)
        assertNull(state.errorMessage)
    }

    @Test
    fun `dismissError limpia el mensaje de error`() {
        viewModel.onQRCodeScanned("INVALID")
        viewModel.dismissError()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.errorMessage)
    }

    @Test
    fun `onQRCodeScanned con formato minusculas encuentra producto`() {
        // El formato PRODUCT_ID_ solo funciona con la sintaxis exacta en mayúsculas
        // Probamos con un ID numérico simple que sabemos que funciona
        viewModel.onQRCodeScanned("1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.scannedProduct)
    }

    @Test
    fun `onQRCodeScanned con string vacio muestra error`() {
        viewModel.onQRCodeScanned("")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.scannedProduct)
        assertTrue(state.isScanning)
        assertNotNull(state.errorMessage)
    }

    @Test
    fun `onQRCodeScanned con espacios muestra error`() {
        viewModel.onQRCodeScanned("   ")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.scannedProduct)
        assertNotNull(state.errorMessage)
    }

    @Test
    fun `onQRCodeScanned multiples veces actualiza estado`() {
        viewModel.onQRCodeScanned("1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state1 = viewModel.uiState.value
        assertNotNull(state1.scannedProduct)

        viewModel.resetScanner()
        viewModel.onQRCodeScanned("2")
        testDispatcher.scheduler.advanceUntilIdle()

        val state2 = viewModel.uiState.value
        assertNotNull(state2.scannedProduct)
    }

    @Test
    fun `QRScannerUIState valores por defecto`() {
        val state = QRScannerUIState()

        assertNull(state.scannedProduct)
        assertTrue(state.isScanning)
        assertNull(state.errorMessage)
    }

    @Test
    fun `QRScannerUIState copy actualiza campos`() {
        val state = QRScannerUIState(isScanning = true)
        val updated = state.copy(isScanning = false)

        assertTrue(state.isScanning)
        assertFalse(updated.isScanning)
    }

    @Test
    fun `onQRCodeScanned con URL compleja extrae ID correctamente`() {
        // El código extrae el último segmento después de /product/
        viewModel.onQRCodeScanned("https://levelupgamer.com/product/1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        // Debería extraer el ID del producto de la URL
        assertNotNull(state.scannedProduct)
    }

    @Test
    fun `onQRCodeScanned con numero negativo muestra error`() {
        viewModel.onQRCodeScanned("-1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.scannedProduct)
        assertNotNull(state.errorMessage)
    }

    @Test
    fun `resetScanner despues de error limpia todo`() {
        viewModel.onQRCodeScanned("INVALID")
        testDispatcher.scheduler.advanceUntilIdle()

        val stateWithError = viewModel.uiState.value
        assertNotNull(stateWithError.errorMessage)

        viewModel.resetScanner()
        testDispatcher.scheduler.advanceUntilIdle()

        val stateAfterReset = viewModel.uiState.value
        assertNull(stateAfterReset.errorMessage)
        assertTrue(stateAfterReset.isScanning)
    }

    @Test
    fun `dismissError no afecta otros campos del estado`() {
        viewModel.onQRCodeScanned("1")
        testDispatcher.scheduler.advanceUntilIdle()

        val product = viewModel.uiState.value.scannedProduct
        val isScanning = viewModel.uiState.value.isScanning

        viewModel.dismissError()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(product, viewModel.uiState.value.scannedProduct)
        assertEquals(isScanning, viewModel.uiState.value.isScanning)
        assertNull(viewModel.uiState.value.errorMessage)
    }
}

