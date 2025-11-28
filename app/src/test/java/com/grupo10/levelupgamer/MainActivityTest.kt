package com.grupo10.levelupgamer

import org.junit.Assert.assertTrue
import org.junit.Test

class MainActivityTest {

    @Test
    fun `MainActivity existe y es una clase valida`() {
        val activityClass = MainActivity::class.java
        assertTrue(activityClass.superclass != null)
    }

    @Test
    fun `MainActivity tiene constructor publico`() {
        val constructors = MainActivity::class.java.constructors
        assertTrue(constructors.isNotEmpty())
    }

    @Test
    fun `MainActivity nombre de clase es correcto`() {
        val activityClass = MainActivity::class.java
        assertTrue(activityClass.simpleName == "MainActivity")
    }

    @Test
    fun `MainActivity package es correcto`() {
        val activityClass = MainActivity::class.java
        assertTrue(activityClass.packageName.contains("levelupgamer"))
    }

    @Test
    fun `MainActivity es una clase concreta no abstracta`() {
        val activityClass = MainActivity::class.java
        assertTrue(!java.lang.reflect.Modifier.isAbstract(activityClass.modifiers))
    }

    @Test
    fun `MainActivity es publica`() {
        val activityClass = MainActivity::class.java
        assertTrue(java.lang.reflect.Modifier.isPublic(activityClass.modifiers))
    }

    @Test
    fun `MainActivity tiene metodo onCreate`() {
        val methods = MainActivity::class.java.declaredMethods
        val hasOnCreate = methods.any { it.name == "onCreate" }
        assertTrue(hasOnCreate)
    }

    @Test
    fun `MainActivity pertenece al paquete correcto`() {
        val packageName = MainActivity::class.java.`package`?.name
        assertTrue(packageName == "com.grupo10.levelupgamer")
    }

    @Test
    fun `MainActivity es instanciable`() {
        val activityClass = MainActivity::class.java
        assertTrue(activityClass.constructors.isNotEmpty())
    }

    @Test
    fun `MainActivity no es interfaz`() {
        val activityClass = MainActivity::class.java
        assertTrue(!activityClass.isInterface)
    }
}

