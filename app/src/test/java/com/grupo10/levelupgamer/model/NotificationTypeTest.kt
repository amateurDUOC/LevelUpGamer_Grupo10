package com.grupo10.levelupgamer.model

import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationTypeTest {

    @Test
    fun `NotificationType GENERAL existe`() {
        val type = NotificationType.GENERAL
        assertEquals(NotificationType.GENERAL, type)
    }

    @Test
    fun `NotificationType ORDER existe`() {
        val type = NotificationType.ORDER
        assertEquals(NotificationType.ORDER, type)
    }

    @Test
    fun `NotificationType PROMOTION existe`() {
        val type = NotificationType.PROMOTION
        assertEquals(NotificationType.PROMOTION, type)
    }

    @Test
    fun `NotificationType SYSTEM existe`() {
        val type = NotificationType.SYSTEM
        assertEquals(NotificationType.SYSTEM, type)
    }

    @Test
    fun `NotificationType todas las tipos son unicos`() {
        val types = NotificationType.values()

        assertEquals(4, types.size)
        assert(types.contains(NotificationType.GENERAL))
        assert(types.contains(NotificationType.ORDER))
        assert(types.contains(NotificationType.PROMOTION))
        assert(types.contains(NotificationType.SYSTEM))
    }

    @Test
    fun `NotificationType obtener por nombre enum`() {
        assertEquals(NotificationType.GENERAL, NotificationType.valueOf("GENERAL"))
        assertEquals(NotificationType.ORDER, NotificationType.valueOf("ORDER"))
        assertEquals(NotificationType.PROMOTION, NotificationType.valueOf("PROMOTION"))
        assertEquals(NotificationType.SYSTEM, NotificationType.valueOf("SYSTEM"))
    }

    @Test
    fun `NotificationType valores tienen orden correcto`() {
        val values = NotificationType.values()

        assertEquals(NotificationType.GENERAL, values[0])
        assertEquals(NotificationType.ORDER, values[1])
        assertEquals(NotificationType.PROMOTION, values[2])
        assertEquals(NotificationType.SYSTEM, values[3])
    }

    @Test
    fun `NotificationType comparacion por identidad`() {
        val type1 = NotificationType.GENERAL
        val type2 = NotificationType.GENERAL

        assertEquals(type1, type2)
        assert(type1 === type2)
    }

    @Test
    fun `NotificationType ordinal correcto`() {
        assertEquals(0, NotificationType.GENERAL.ordinal)
        assertEquals(1, NotificationType.ORDER.ordinal)
        assertEquals(2, NotificationType.PROMOTION.ordinal)
        assertEquals(3, NotificationType.SYSTEM.ordinal)
    }

    @Test
    fun `NotificationType comparar diferentes tipos`() {
        assert(NotificationType.GENERAL != NotificationType.ORDER)
        assert(NotificationType.ORDER != NotificationType.PROMOTION)
        assert(NotificationType.PROMOTION != NotificationType.SYSTEM)
        assert(NotificationType.GENERAL != NotificationType.SYSTEM)
    }

    @Test
    fun `NotificationType cada tipo tiene nombre unico`() {
        val types = NotificationType.values()
        val names = types.map { it.name }

        assertEquals(types.size, names.toSet().size)
    }
}

