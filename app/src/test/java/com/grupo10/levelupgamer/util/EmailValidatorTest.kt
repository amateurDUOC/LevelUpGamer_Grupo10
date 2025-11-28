package com.grupo10.levelupgamer.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EmailValidatorTest {

    @Test
    fun `email valido con formato correcto retorna true`() {
        assertTrue(EmailValidator.isValidEmail("usuario@duoc.cl"))
        assertTrue(EmailValidator.isValidEmail("test@gmail.com"))
        assertTrue(EmailValidator.isValidEmail("nombre.apellido@empresa.com"))
        assertTrue(EmailValidator.isValidEmail("user123@dominio.cl"))
    }

    @Test
    fun `email sin arroba retorna false`() {
        assertFalse(EmailValidator.isValidEmail("usuarioduoc.cl"))
        assertFalse(EmailValidator.isValidEmail("testgmail.com"))
    }

    @Test
    fun `email sin dominio retorna false`() {
        assertFalse(EmailValidator.isValidEmail("usuario@"))
        assertFalse(EmailValidator.isValidEmail("test@.com"))
    }

    @Test
    fun `email sin usuario retorna false`() {
        assertFalse(EmailValidator.isValidEmail("@duoc.cl"))
        assertFalse(EmailValidator.isValidEmail("@gmail.com"))
    }

    @Test
    fun `email sin extension retorna false`() {
        assertFalse(EmailValidator.isValidEmail("usuario@duoc"))
        assertFalse(EmailValidator.isValidEmail("test@gmail"))
    }

    @Test
    fun `email vacio retorna false`() {
        assertFalse(EmailValidator.isValidEmail(""))
    }

    @Test
    fun `email con espacios retorna false`() {
        assertFalse(EmailValidator.isValidEmail("usuario @duoc.cl"))
        assertFalse(EmailValidator.isValidEmail("usuario@ duoc.cl"))
        assertFalse(EmailValidator.isValidEmail(" usuario@duoc.cl"))
        assertFalse(EmailValidator.isValidEmail("usuario@duoc.cl "))
    }

    @Test
    fun `email con caracteres especiales validos retorna true`() {
        assertTrue(EmailValidator.isValidEmail("user+tag@duoc.cl"))
        assertTrue(EmailValidator.isValidEmail("user.name@duoc.cl"))
        assertTrue(EmailValidator.isValidEmail("user_name@duoc.cl"))
        assertTrue(EmailValidator.isValidEmail("user-name@duoc.cl"))
    }

    @Test
    fun `email con multiples arrobas retorna false`() {
        assertFalse(EmailValidator.isValidEmail("usuario@@duoc.cl"))
        assertFalse(EmailValidator.isValidEmail("user@name@duoc.cl"))
    }

    @Test
    fun `email con extension corta retorna true`() {
        assertTrue(EmailValidator.isValidEmail("usuario@duoc.cl"))
        assertTrue(EmailValidator.isValidEmail("test@site.io"))
    }

    @Test
    fun `email con extension larga retorna true`() {
        assertTrue(EmailValidator.isValidEmail("usuario@dominio.com.mx"))
        assertTrue(EmailValidator.isValidEmail("test@universidad.edu"))
    }

    @Test
    fun `email con guion en dominio retorna true`() {
        assertTrue(EmailValidator.isValidEmail("usuario@mi-dominio.com"))
        assertTrue(EmailValidator.isValidEmail("test@sub-domain.site.cl"))
    }

    @Test
    fun `email con numeros retorna true`() {
        assertTrue(EmailValidator.isValidEmail("usuario123@duoc456.cl"))
        assertTrue(EmailValidator.isValidEmail("123@456.com"))
    }

    @Test
    fun `email con mayusculas y minusculas retorna true`() {
        assertTrue(EmailValidator.isValidEmail("Usuario@Duoc.CL"))
        assertTrue(EmailValidator.isValidEmail("TeSt@GmAiL.CoM"))
    }

    @Test
    fun `email con punto al final del usuario retorna true`() {
        assertTrue(EmailValidator.isValidEmail("usuario.@duoc.cl"))
    }

    @Test
    fun `email con caracteres especiales invalidos retorna false`() {
        assertFalse(EmailValidator.isValidEmail("usuario#@duoc.cl"))
        assertFalse(EmailValidator.isValidEmail("usuario$@duoc.cl"))
        assertFalse(EmailValidator.isValidEmail("usuario%@duoc.cl"))
        assertFalse(EmailValidator.isValidEmail("usuario&@duoc.cl"))
        assertFalse(EmailValidator.isValidEmail("usuario*@duoc.cl"))
    }

    @Test
    fun `email con solo arroba retorna false`() {
        assertFalse(EmailValidator.isValidEmail("@"))
    }

    @Test
    fun `email con punto al inicio retorna true`() {
        assertTrue(EmailValidator.isValidEmail(".usuario@duoc.cl"))
    }

    @Test
    fun `email con subdominios retorna true`() {
        assertTrue(EmailValidator.isValidEmail("usuario@mail.duoc.cl"))
        assertTrue(EmailValidator.isValidEmail("test@sub.mail.empresa.com"))
    }

    @Test
    fun `email con extension de un caracter retorna false`() {
        assertFalse(EmailValidator.isValidEmail("usuario@duoc.c"))
    }

    @Test
    fun `email con extension de dos caracteres retorna true`() {
        assertTrue(EmailValidator.isValidEmail("usuario@duoc.cl"))
    }

    @Test
    fun `emails comunmente usados son validos`() {
        // Emails de prueba comunes
        assertTrue(EmailValidator.isValidEmail("admin@example.com"))
        assertTrue(EmailValidator.isValidEmail("info@company.com"))
        assertTrue(EmailValidator.isValidEmail("support@service.cl"))
        assertTrue(EmailValidator.isValidEmail("no-reply@notifications.com"))
    }

    @Test
    fun `email con solo caracteres especiales en usuario retorna false`() {
        // El regex permite [A-Za-z0-9+_.-] pero debe haber al menos un carácter válido
        // Estos fallan porque tienen caracteres no permitidos o formato inválido
        assertFalse(EmailValidator.isValidEmail("@duoc.cl"))  // Sin usuario
        assertFalse(EmailValidator.isValidEmail("#@duoc.cl"))  // Carácter no permitido
        assertFalse(EmailValidator.isValidEmail("$@duoc.cl"))  // Carácter no permitido
    }

    @Test
    fun `email con dominio con numeros retorna true`() {
        assertTrue(EmailValidator.isValidEmail("usuario@duoc123.cl"))
        assertTrue(EmailValidator.isValidEmail("test@example456.com"))
    }

    @Test
    fun `email muy largo retorna true si formato es correcto`() {
        val longEmail = "a".repeat(50) + "@" + "b".repeat(50) + ".com"
        assertTrue(EmailValidator.isValidEmail(longEmail))
    }

    @Test
    fun `email con punto consecutivos en usuario retorna true`() {
        // El regex actual permite esto
        assertTrue(EmailValidator.isValidEmail("user..name@duoc.cl"))
    }

    @Test
    fun `email con dominio que termina en punto retorna false`() {
        assertFalse(EmailValidator.isValidEmail("usuario@duoc."))
    }
}

