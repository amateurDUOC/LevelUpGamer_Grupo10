package com.grupo10.levelupgamer.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotificationTest {

    @Test
    fun `crear notificacion con valores predeterminados`() {
        val notification = Notification(
            title = "Test Title",
            message = "Test Message",
            userId = 123
        )

        assertEquals("Test Title", notification.title)
        assertEquals("Test Message", notification.message)
        assertEquals(123, notification.userId)
        assertFalse(notification.isRead)
        assertEquals(NotificationType.GENERAL, notification.type)
    }

    @Test
    fun `crear notificacion con tipo ORDER`() {
        val notification = Notification(
            title = "Pedido realizado",
            message = "Tu pedido ha sido confirmado",
            userId = 123,
            type = NotificationType.ORDER
        )

        assertEquals(NotificationType.ORDER, notification.type)
    }

    @Test
    fun `crear notificacion con tipo PROMOTION`() {
        val notification = Notification(
            title = "Oferta especial",
            message = "50% de descuento",
            userId = 123,
            type = NotificationType.PROMOTION
        )

        assertEquals(NotificationType.PROMOTION, notification.type)
    }

    @Test
    fun `crear notificacion con tipo SYSTEM`() {
        val notification = Notification(
            title = "Actualización del sistema",
            message = "Nueva versión disponible",
            userId = 123,
            type = NotificationType.SYSTEM
        )

        assertEquals(NotificationType.SYSTEM, notification.type)
    }

    @Test
    fun `crear notificacion marcada como leida`() {
        val notification = Notification(
            title = "Test",
            message = "Message",
            userId = 123,
            isRead = true
        )

        assertTrue(notification.isRead)
    }

    @Test
    fun `modificar notificacion usando copy`() {
        val notification = Notification(
            id = 1,
            title = "Original",
            message = "Original message",
            userId = 123,
            isRead = false
        )

        val modified = notification.copy(
            title = "Modified",
            isRead = true
        )

        assertEquals("Modified", modified.title)
        assertTrue(modified.isRead)
        assertEquals("Original message", modified.message)
        assertEquals(1, modified.id)
    }

    @Test
    fun `dos notificaciones con mismo contenido son iguales`() {
        val notification1 = Notification(
            id = 1,
            title = "Test",
            message = "Message",
            userId = 123
        )

        val notification2 = Notification(
            id = 1,
            title = "Test",
            message = "Message",
            userId = 123
        )

        assertEquals(notification1, notification2)
    }

    @Test
    fun `crear notificacion con timestamp`() {
        val timestamp = System.currentTimeMillis()
        val notification = Notification(
            title = "Test",
            message = "Message",
            userId = 123,
            timestamp = timestamp
        )

        assertEquals(timestamp, notification.timestamp)
    }

    @Test
    fun `notificacion con diferentes usuarios`() {
        val notification1 = Notification(
            title = "Test",
            message = "Message",
            userId = 100
        )

        val notification2 = Notification(
            title = "Test",
            message = "Message",
            userId = 200
        )

        assertEquals(100, notification1.userId)
        assertEquals(200, notification2.userId)
    }

    @Test
    fun `notificacion con titulo largo`() {
        val longTitle = "T".repeat(200)
        val notification = Notification(
            title = longTitle,
            message = "Message",
            userId = 123
        )

        assertEquals(longTitle, notification.title)
    }

    @Test
    fun `notificacion con mensaje largo`() {
        val longMessage = "M".repeat(1000)
        val notification = Notification(
            title = "Title",
            message = longMessage,
            userId = 123
        )

        assertEquals(longMessage, notification.message)
    }

    @Test
    fun `notificacion con titulo y mensaje vacios`() {
        val notification = Notification(
            title = "",
            message = "",
            userId = 123
        )

        assertEquals("", notification.title)
        assertEquals("", notification.message)
    }

    @Test
    fun `cambiar estado de lectura usando copy`() {
        val notification = Notification(
            id = 1,
            title = "Test",
            message = "Message",
            userId = 123,
            isRead = false
        )

        val read = notification.copy(isRead = true)

        assertFalse(notification.isRead)
        assertTrue(read.isRead)
    }

    @Test
    fun `cambiar tipo de notificacion usando copy`() {
        val notification = Notification(
            id = 1,
            title = "Test",
            message = "Message",
            userId = 123,
            type = NotificationType.GENERAL
        )

        val order = notification.copy(type = NotificationType.ORDER)
        val promo = notification.copy(type = NotificationType.PROMOTION)
        val system = notification.copy(type = NotificationType.SYSTEM)

        assertEquals(NotificationType.GENERAL, notification.type)
        assertEquals(NotificationType.ORDER, order.type)
        assertEquals(NotificationType.PROMOTION, promo.type)
        assertEquals(NotificationType.SYSTEM, system.type)
    }
}

