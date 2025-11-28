package com.grupo10.levelupgamer.ui.theme

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ColorTest {

    @Test
    fun `Purple80 tiene el valor correcto`() {
        assertEquals(Color(0xFFD0BCFF), Purple80)
    }

    @Test
    fun `PurpleGrey80 tiene el valor correcto`() {
        assertEquals(Color(0xFFCCC2DC), PurpleGrey80)
    }

    @Test
    fun `Pink80 tiene el valor correcto`() {
        assertEquals(Color(0xFFEFB8C8), Pink80)
    }

    @Test
    fun `Purple40 tiene el valor correcto`() {
        assertEquals(Color(0xFF6650a4), Purple40)
    }

    @Test
    fun `PurpleGrey40 tiene el valor correcto`() {
        assertEquals(Color(0xFF625b71), PurpleGrey40)
    }

    @Test
    fun `Pink40 tiene el valor correcto`() {
        assertEquals(Color(0xFF7D5260), Pink40)
    }

    @Test
    fun `GamerPurple tiene el valor correcto`() {
        assertEquals(Color(0xFF6200EE), GamerPurple)
    }

    @Test
    fun `GamerTeal tiene el valor correcto`() {
        assertEquals(Color(0xFF03DAC5), GamerTeal)
    }

    @Test
    fun `GamerGold tiene el valor correcto`() {
        assertEquals(Color(0xFFFFD700), GamerGold)
    }

    @Test
    fun `GamerDarkBlue tiene el valor correcto`() {
        assertEquals(Color(0xFF1A1A2E), GamerDarkBlue)
    }

    @Test
    fun `GamerNavyBlue tiene el valor correcto`() {
        assertEquals(Color(0xFF16213E), GamerNavyBlue)
    }

    @Test
    fun `GamerDeepBlue tiene el valor correcto`() {
        assertEquals(Color(0xFF0F3460), GamerDeepBlue)
    }

    @Test
    fun `GamerRed tiene el valor correcto`() {
        assertEquals(Color(0xFFE94560), GamerRed)
    }

    @Test
    fun `GamerWhite tiene el valor correcto`() {
        assertEquals(Color(0xFFF5F5F5), GamerWhite)
    }

    @Test
    fun `colores de tema claro son diferentes de tema oscuro`() {
        assertNotEquals(Purple80, Purple40)
        assertNotEquals(PurpleGrey80, PurpleGrey40)
        assertNotEquals(Pink80, Pink40)
    }

    @Test
    fun `colores personalizados son unicos`() {
        assertNotEquals(GamerPurple, GamerTeal)
        assertNotEquals(GamerPurple, GamerGold)
        assertNotEquals(GamerDarkBlue, GamerNavyBlue)
        assertNotEquals(GamerNavyBlue, GamerDeepBlue)
    }

    @Test
    fun `GamerGold es amarillo dorado`() {
        val gold = GamerGold
        // El color oro tiene alto valor en rojo y verde, bajo en azul
        assert(gold.red > 0.9f)
        assert(gold.green > 0.8f)
        assert(gold.blue == 0f)
    }

    @Test
    fun `GamerRed es predominantemente rojo`() {
        val red = GamerRed
        // El color rojo debe tener más componente rojo que los otros
        assert(red.red > red.green)
        assert(red.red > red.blue)
    }

    @Test
    fun `GamerTeal tiene componentes cyan`() {
        val teal = GamerTeal
        // Teal es una combinación de verde y azul
        assert(teal.green > 0.5f)
        assert(teal.blue > 0.5f)
        assert(teal.red < 0.5f)
    }

    @Test
    fun `tonos oscuros de gamer son mas oscuros que claros`() {
        // Los colores oscuros deben tener valores RGB más bajos
        assert(GamerDarkBlue.red < 0.2f)
        assert(GamerDarkBlue.green < 0.2f)
        assert(GamerDarkBlue.blue < 0.2f)
    }

    @Test
    fun `GamerWhite es casi blanco`() {
        val white = GamerWhite
        // Un blanco grisáceo debe tener valores altos en todos los componentes
        assert(white.red > 0.9f)
        assert(white.green > 0.9f)
        assert(white.blue > 0.9f)
    }

    @Test
    fun `colores morados tienen mayor componente azul`() {
        assert(Purple80.blue > Purple80.red)
        assert(Purple40.blue > Purple40.red)
        assert(GamerPurple.blue > GamerPurple.red)
    }

    @Test
    fun `colores rosa tienen componente rojo alto`() {
        assert(Pink80.red > Pink80.blue)
        assert(Pink40.red > Pink40.blue)
    }
}

