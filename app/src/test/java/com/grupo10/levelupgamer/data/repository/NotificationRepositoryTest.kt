package com.grupo10.levelupgamer.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.grupo10.levelupgamer.data.dao.NotificationDao
import com.grupo10.levelupgamer.model.Notification
import com.grupo10.levelupgamer.model.NotificationType
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: NotificationRepository
    private lateinit var notificationDao: NotificationDao

    @Before
    fun setup() {
        notificationDao = mockk(relaxed = true)
        repository = NotificationRepository(notificationDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getNotifications retorna LiveData del dao`() {
        val mockLiveData = MutableLiveData<List<Notification>>()
        every { notificationDao.getNotifications(any()) } returns mockLiveData

        val result = repository.getNotifications(123)

        assertNotNull(result)
        assertEquals(mockLiveData, result)
        verify { notificationDao.getNotifications(123) }
    }

    @Test
    fun `getUnreadCount retorna LiveData del dao`() {
        val mockLiveData = MutableLiveData<Int>()
        every { notificationDao.getUnreadCount(any()) } returns mockLiveData

        val result = repository.getUnreadCount(123)

        assertNotNull(result)
        assertEquals(mockLiveData, result)
        verify { notificationDao.getUnreadCount(123) }
    }

    @Test
    fun `getUnreadNotifications retorna LiveData del dao`() {
        val mockLiveData = MutableLiveData<List<Notification>>()
        every { notificationDao.getUnreadNotifications(any()) } returns mockLiveData

        val result = repository.getUnreadNotifications(123)

        assertNotNull(result)
        assertEquals(mockLiveData, result)
        verify { notificationDao.getUnreadNotifications(123) }
    }

    @Test
    fun `addNotification llama al dao insert`() = runTest {
        val notification = Notification(
            id = 1,
            title = "Test",
            message = "Message",
            userId = 123,
            type = NotificationType.ORDER
        )

        coEvery { notificationDao.insert(any()) } just Runs

        repository.addNotification(notification)

        coVerify { notificationDao.insert(notification) }
    }

    @Test
    fun `updateNotification llama al dao update`() = runTest {
        val notification = Notification(
            id = 1,
            title = "Test",
            message = "Message",
            userId = 123,
            isRead = true
        )

        coEvery { notificationDao.update(any()) } just Runs

        repository.updateNotification(notification)

        coVerify { notificationDao.update(notification) }
    }

    @Test
    fun `deleteNotification llama al dao delete`() = runTest {
        val notification = Notification(
            id = 1,
            title = "Test",
            message = "Message",
            userId = 123
        )

        coEvery { notificationDao.delete(any()) } just Runs

        repository.deleteNotification(notification)

        coVerify { notificationDao.delete(notification) }
    }

    @Test
    fun `markAsRead llama al dao con el ID correcto`() = runTest {
        coEvery { notificationDao.markAsRead(any()) } just Runs

        repository.markAsRead(1)

        coVerify { notificationDao.markAsRead(1) }
    }

    @Test
    fun `markAllAsRead llama al dao con el userId correcto`() = runTest {
        coEvery { notificationDao.markAllAsRead(any()) } just Runs

        repository.markAllAsRead(123)

        coVerify { notificationDao.markAllAsRead(123) }
    }

    @Test
    fun `deleteAllUserNotifications llama al dao con el userId correcto`() = runTest {
        coEvery { notificationDao.deleteAllUserNotifications(any()) } just Runs

        repository.deleteAllUserNotifications(123)

        coVerify { notificationDao.deleteAllUserNotifications(123) }
    }

    @Test
    fun `deleteReadNotifications llama al dao con el userId correcto`() = runTest {
        coEvery { notificationDao.deleteReadNotifications(any()) } just Runs

        repository.deleteReadNotifications(123)

        coVerify { notificationDao.deleteReadNotifications(123) }
    }

    @Test
    fun `multiples llamadas a getNotifications con diferentes usuarios`() {
        val mockLiveData1 = MutableLiveData<List<Notification>>()
        val mockLiveData2 = MutableLiveData<List<Notification>>()

        every { notificationDao.getNotifications(100) } returns mockLiveData1
        every { notificationDao.getNotifications(200) } returns mockLiveData2

        repository.getNotifications(100)
        repository.getNotifications(200)

        verify { notificationDao.getNotifications(100) }
        verify { notificationDao.getNotifications(200) }
    }

    @Test
    fun `addNotification con diferentes tipos de notificaciones`() = runTest {
        val orderNotification = Notification(
            id = 1,
            title = "Order",
            message = "Order placed",
            userId = 123,
            type = NotificationType.ORDER
        )

        val promoNotification = Notification(
            id = 2,
            title = "Promo",
            message = "New promotion",
            userId = 123,
            type = NotificationType.PROMOTION
        )

        coEvery { notificationDao.insert(any()) } just Runs

        repository.addNotification(orderNotification)
        repository.addNotification(promoNotification)

        coVerify { notificationDao.insert(orderNotification) }
        coVerify { notificationDao.insert(promoNotification) }
    }

    @Test
    fun `markAllAsRead con diferentes usuarios`() = runTest {
        coEvery { notificationDao.markAllAsRead(any()) } just Runs

        repository.markAllAsRead(100)
        repository.markAllAsRead(200)
        repository.markAllAsRead(300)

        coVerify(exactly = 1) { notificationDao.markAllAsRead(100) }
        coVerify(exactly = 1) { notificationDao.markAllAsRead(200) }
        coVerify(exactly = 1) { notificationDao.markAllAsRead(300) }
    }

    @Test
    fun `updateNotification modifica estado de lectura`() = runTest {
        val notification = Notification(
            id = 1,
            title = "Test",
            message = "Message",
            userId = 123,
            isRead = false
        )

        val updatedNotification = notification.copy(isRead = true)

        coEvery { notificationDao.update(any()) } just Runs

        repository.updateNotification(updatedNotification)

        coVerify { notificationDao.update(match { it.isRead }) }
    }

    @Test
    fun `deleteReadNotifications solo afecta notificaciones leidas`() = runTest {
        coEvery { notificationDao.deleteReadNotifications(any()) } just Runs

        repository.deleteReadNotifications(123)

        coVerify(exactly = 1) { notificationDao.deleteReadNotifications(123) }
    }
}

