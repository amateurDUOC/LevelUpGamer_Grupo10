package com.grupo10.levelupgamer.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {
    private val _currentUserId = MutableStateFlow<Int?>(null)
    val currentUserId: StateFlow<Int?> = _currentUserId.asStateFlow()

    private val _isUserAuthenticated = MutableStateFlow(false)
    val isUserAuthenticated: StateFlow<Boolean> = _isUserAuthenticated.asStateFlow()

    fun setCurrentUser(userId: Int) {
        _currentUserId.value = userId
        _isUserAuthenticated.value = true
    }

    fun logout() {
        _currentUserId.value = null
        _isUserAuthenticated.value = false
    }

    fun getCurrentUserId(): Int? = _currentUserId.value
}

