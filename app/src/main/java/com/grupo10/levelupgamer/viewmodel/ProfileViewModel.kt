package com.grupo10.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo10.levelupgamer.data.remote.RetrofitClient
import com.grupo10.levelupgamer.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUIState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUIState())
    val uiState: StateFlow<ProfileUIState> = _uiState.asStateFlow()

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val response = RetrofitClient.authApi.getCurrentUser()

                if (response.isSuccessful && response.body()?.success == true) {
                    val userWrapper = response.body()!!.data!!
                    val user = User(
                        id = userWrapper.user.id.toIntOrNull() ?: 0,
                        email = userWrapper.user.email,
                        name = userWrapper.user.name,
                        address = userWrapper.user.address,
                        latitude = userWrapper.user.latitude,
                        longitude = userWrapper.user.longitude
                    )
                    _uiState.value = _uiState.value.copy(
                        user = user,
                        isLoading = false,
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No se pudo cargar el perfil"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error de conexión: ${e.message}"
                )
            }
        }
    }

    fun refreshProfile() {
        loadUserProfile()
    }
}

