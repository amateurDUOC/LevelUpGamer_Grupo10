package com.grupo10.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import com.grupo10.levelupgamer.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isUserAuthenticated = MutableStateFlow(false)
    val isUserAuthenticated: StateFlow<Boolean> = _isUserAuthenticated.asStateFlow()

    fun setCurrentUser(user: User) {
        _currentUser.value = user
        _isUserAuthenticated.value = true
    }

    fun updateUserAddress(address: String, latitude: Double, longitude: Double) {
        _currentUser.value?.let { user ->
            _currentUser.value = user.copy(
                address = address,
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    fun logout() {
        _currentUser.value = null
        _isUserAuthenticated.value = false
    }

    fun getCurrentUser(): User? = _currentUser.value
}

