package com.grupo10.levelupgamer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NavigationDrawerContent(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier.width(280.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            // Header del drawer
            DrawerHeader()

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )

            // Items del menú
            DrawerMenuItem(
                icon = Icons.Default.Person,
                label = "Mi Perfil",
                onClick = {
                    // TODO: Navegar a Mi Perfil
                    onDismiss()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.Home,
                label = "Inicio",
                onClick = {
                    // TODO: Navegar a Inicio
                    onDismiss()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.List,
                label = "Categorías",
                onClick = {
                    // TODO: Navegar a Categorías
                    onDismiss()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.ShoppingCart,
                label = "Mis Compras",
                onClick = {
                    // TODO: Navegar a Mis Compras
                    onDismiss()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.Email,
                label = "Contacto",
                onClick = {
                    // TODO: Navegar a Contacto
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Versión de la app (opcional)
            Text(
                text = "Level Up Gamer v1.0",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "🎮",
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Level Up Gamer",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "Tu tienda de videojuegos",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

