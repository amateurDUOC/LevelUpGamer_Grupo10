package com.grupo10.levelupgamer.data.repository

import androidx.lifecycle.LiveData
import com.grupo10.levelupgamer.data.dao.NotificationDao
import com.grupo10.levelupgamer.model.Notification

class NotificationRepository(private val notificationDao: NotificationDao) {

    fun getNotifications(userId: Int): LiveData<List<Notification>> {
        return notificationDao.getNotifications(userId)
    }

    fun getUnreadCount(userId: Int): LiveData<Int> {
        return notificationDao.getUnreadCount(userId)
    }

    fun getUnreadNotifications(userId: Int): LiveData<List<Notification>> {
        return notificationDao.getUnreadNotifications(userId)
    }

    suspend fun addNotification(notification: Notification) {
        notificationDao.insert(notification)
    }

    suspend fun updateNotification(notification: Notification) {
        notificationDao.update(notification)
    }

    suspend fun deleteNotification(notification: Notification) {
        notificationDao.delete(notification)
    }

    suspend fun markAsRead(notificationId: Int) {
        notificationDao.markAsRead(notificationId)
    }

    suspend fun markAllAsRead(userId: Int) {
        notificationDao.markAllAsRead(userId)
    }

    suspend fun deleteAllUserNotifications(userId: Int) {
        notificationDao.deleteAllUserNotifications(userId)
    }

    suspend fun deleteReadNotifications(userId: Int) {
        notificationDao.deleteReadNotifications(userId)
    }
}

