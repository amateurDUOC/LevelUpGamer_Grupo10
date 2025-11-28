package com.grupo10.levelupgamer.ui.theme

import android.app.Activity
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertNotNull

class ThemeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `LevelUpGamerTheme se puede aplicar sin errores`() {
        composeTestRule.setContent {
            LevelUpGamerTheme {
                // Contenido vacío para verificar que el tema se aplica
            }
        }
    }

    @Test
    fun `LevelUpGamerTheme con modo oscuro se aplica correctamente`() {
        composeTestRule.setContent {
            LevelUpGamerTheme(darkTheme = true) {
                // Contenido vacío
            }
        }
    }

    @Test
    fun `LevelUpGamerTheme con modo claro se aplica correctamente`() {
        composeTestRule.setContent {
            LevelUpGamerTheme(darkTheme = false) {
                // Contenido vacío
            }
        }
    }

    @Test
    fun `LevelUpGamerTheme sin colores dinamicos funciona`() {
        composeTestRule.setContent {
            LevelUpGamerTheme(dynamicColor = false) {
                // Contenido vacío
            }
        }
    }

    @Test
    fun `LevelUpGamerTheme con colores dinamicos funciona`() {
        composeTestRule.setContent {
            LevelUpGamerTheme(dynamicColor = true) {
                // Contenido vacío
            }
        }
    }

    @Test
    fun `Typography se inicializa correctamente`() {
        assertNotNull(Typography)
        assertNotNull(Typography.bodyLarge)
        assertNotNull(Typography.bodyMedium)
        assertNotNull(Typography.bodySmall)
    }

    @Test
    fun `DarkColorScheme tiene colores validos`() {
        // Verificar que los colores no son null mediante reflexión
        val darkSchemeClass = Class.forName("com.grupo10.levelupgamer.ui.theme.ThemeKt")
        val darkSchemeField = darkSchemeClass.getDeclaredField("DarkColorScheme")
        darkSchemeField.isAccessible = true
        val darkScheme = darkSchemeField.get(null) as ColorScheme

        assertNotNull(darkScheme.primary)
        assertNotNull(darkScheme.secondary)
        assertNotNull(darkScheme.tertiary)
    }

    @Test
    fun `LightColorScheme tiene colores validos`() {
        // Verificar que los colores no son null mediante reflexión
        val lightSchemeClass = Class.forName("com.grupo10.levelupgamer.ui.theme.ThemeKt")
        val lightSchemeField = lightSchemeClass.getDeclaredField("LightColorScheme")
        lightSchemeField.isAccessible = true
        val lightScheme = lightSchemeField.get(null) as ColorScheme

        assertNotNull(lightScheme.primary)
        assertNotNull(lightScheme.secondary)
        assertNotNull(lightScheme.tertiary)
    }
}

