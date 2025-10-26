package com.grupo10.levelupgamer.util

import java.util.regex.Pattern

object EmailValidator {
    // Regex mejorado para validar emails: debe tener formato usuario@dominio.extension
    private const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"

    fun isValidEmail(email: String): Boolean {
        return Pattern.matches(EMAIL_REGEX, email)
    }
}

