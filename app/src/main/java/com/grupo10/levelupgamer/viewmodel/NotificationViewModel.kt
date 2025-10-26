package com.grupo10.levelupgamer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.grupo10.levelupgamer.data.database.LevelUpDatabase
import com.grupo10.levelupgamer.model.Notification
import com.grupo10.levelupgamer.data.repository.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotificationRepository
    private val _currentUserId = MutableLiveData<Int>()

    init {
        val notificationDao = LevelUpDatabase.getDatabase(application).notificationDao()
        repository = NotificationRepository(notificationDao)
    }

    fun setCurrentUser(userId: Int) {
        _currentUserId.value = userId
    }

    fun getNotifications(): LiveData<List<Notification>>? {
        return _currentUserId.value?.let { userId ->
            repository.getNotifications(userId)
        }
    }

    fun getUnreadCount(): LiveData<Int>? {
        return _currentUserId.value?.let { userId ->
            repository.getUnreadCount(userId)
        }
    }

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            repository.markAsRead(notificationId)
        }
    }

    fun markAllAsRead() {
        _currentUserId.value?.let { userId ->
            viewModelScope.launch {
                repository.markAllAsRead(userId)
            }
        }
    }

    fun addNotification(title: String, message: String) {
        _currentUserId.value?.let { userId ->
            viewModelScope.launch {
                val notification = Notification(
                    title = title,
                    message = message,
                    userId = userId
                )
                repository.addNotification(notification)
            }
        }
    }
}

