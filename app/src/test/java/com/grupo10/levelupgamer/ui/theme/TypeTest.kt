package com.grupo10.levelupgamer.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.junit.Assert.assertEquals
import org.junit.Test

class TypeTest {

    @Test
    fun `Typography bodyLarge tiene configuracion correcta`() {
        val bodyLarge = Typography.bodyLarge

        assertEquals(FontFamily.Default, bodyLarge.fontFamily)
        assertEquals(FontWeight.Normal, bodyLarge.fontWeight)
        assertEquals(16.sp, bodyLarge.fontSize)
        assertEquals(24.sp, bodyLarge.lineHeight)
        assertEquals(0.5.sp, bodyLarge.letterSpacing)
    }

    @Test
    fun `Typography bodyLarge usa FontWeight Normal`() {
        assertEquals(FontWeight.Normal, Typography.bodyLarge.fontWeight)
    }

    @Test
    fun `Typography bodyLarge usa FontFamily Default`() {
        assertEquals(FontFamily.Default, Typography.bodyLarge.fontFamily)
    }

    @Test
    fun `Typography bodyLarge tiene fontSize de 16sp`() {
        assertEquals(16.sp, Typography.bodyLarge.fontSize)
    }

    @Test
    fun `Typography bodyLarge tiene lineHeight de 24sp`() {
        assertEquals(24.sp, Typography.bodyLarge.lineHeight)
    }

    @Test
    fun `Typography bodyLarge tiene letterSpacing de 0_5sp`() {
        assertEquals(0.5.sp, Typography.bodyLarge.letterSpacing)
    }

    @Test
    fun `Typography es instancia valida`() {
        assert(Typography != null)
    }

    @Test
    fun `Typography bodyLarge no es null`() {
        assert(Typography.bodyLarge != null)
    }

    @Test
    fun `Typography tiene estilos predeterminados`() {
        // Verificar que los estilos predeterminados no son null
        assert(Typography.displayLarge != null)
        assert(Typography.displayMedium != null)
        assert(Typography.displaySmall != null)
        assert(Typography.headlineLarge != null)
        assert(Typography.headlineMedium != null)
        assert(Typography.headlineSmall != null)
        assert(Typography.titleLarge != null)
        assert(Typography.titleMedium != null)
        assert(Typography.titleSmall != null)
        assert(Typography.bodyLarge != null)
        assert(Typography.bodyMedium != null)
        assert(Typography.bodySmall != null)
        assert(Typography.labelLarge != null)
        assert(Typography.labelMedium != null)
        assert(Typography.labelSmall != null)
    }
}

