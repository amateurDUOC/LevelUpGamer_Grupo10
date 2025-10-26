package com.grupo10.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import com.grupo10.levelupgamer.model.Product
import com.grupo10.levelupgamer.model.ProductsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class QRScannerUIState(
    val scannedProduct: Product? = null,
    val isScanning: Boolean = true,
    val errorMessage: String? = null
)

class QRScannerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QRScannerUIState())
    val uiState: StateFlow<QRScannerUIState> = _uiState.asStateFlow()

    /**
     * Procesa el código QR escaneado
     * En una app real, el QR contendría una URL o ID del producto
     * Para la demo, simulamos que el QR contiene el ID del producto
     */
    fun onQRCodeScanned(qrContent: String) {
        try {
            // Intentar extraer el ID del producto del QR
            // Formato esperado: "PRODUCT_ID_X" donde X es el número
            val productId = extractProductId(qrContent)

            if (productId != null) {
                val product = ProductsData.sampleProducts.find { it.id == productId }
                if (product != null) {
                    _uiState.value = QRScannerUIState(
                        scannedProduct = product,
                        isScanning = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = QRScannerUIState(
                        scannedProduct = null,
                        isScanning = true,
                        errorMessage = "Producto no encontrado (ID: $productId)"
                    )
                }
            } else {
                _uiState.value = QRScannerUIState(
                    scannedProduct = null,
                    isScanning = true,
                    errorMessage = "Código QR no válido"
                )
            }
        } catch (e: Exception) {
            _uiState.value = QRScannerUIState(
                scannedProduct = null,
                isScanning = true,
                errorMessage = "Error al procesar QR: ${e.message}"
            )
        }
    }

    /**
     * Extrae el ID del producto del contenido del QR
     * Soporta formatos: "PRODUCT_ID_1", "1", "https://levelupgamer.com/product/1"
     */
    private fun extractProductId(qrContent: String): Int? {
        return try {
            when {
                // Formato: PRODUCT_ID_X
                qrContent.startsWith("PRODUCT_ID_", ignoreCase = true) -> {
                    qrContent.substringAfter("PRODUCT_ID_").toIntOrNull()
                }
                // Formato: URL con ID al final
                qrContent.contains("/product/") -> {
                    qrContent.substringAfterLast("/").toIntOrNull()
                }
                // Formato: Solo número
                else -> qrContent.toIntOrNull()
            }
        } catch (e: Exception) {
            null
        }
    }

    fun resetScanner() {
        _uiState.value = QRScannerUIState(
            scannedProduct = null,
            isScanning = true,
            errorMessage = null
        )
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

