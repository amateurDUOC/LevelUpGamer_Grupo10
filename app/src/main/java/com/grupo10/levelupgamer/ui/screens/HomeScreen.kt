package com.grupo10.levelupgamer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo10.levelupgamer.ui.components.HomeHeader
import com.grupo10.levelupgamer.ui.components.BottomNavigationBar
import com.grupo10.levelupgamer.ui.components.ProductCard
import com.grupo10.levelupgamer.ui.components.ProductGridCard
import com.grupo10.levelupgamer.model.ProductsData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var showNotificationDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    // Simular contador de notificaciones
    val notificationCount = remember { mutableStateOf(3) }

    // Simular contador de items en carrito
    val cartItemCount = remember { mutableStateOf(5) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            HomeHeader(
                modifier = Modifier,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onNotificationClick = {
                    showNotificationDialog = true
                    onNotificationClick()
                },
                notificationCount = notificationCount.value,
                onLogoutClick = onLogout
            )
        },
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                cartItemCount = cartItemCount.value
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Bienvenido a Level Up Gamer",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Text(
                    text = "Tu tienda de videojuegos favorita",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            // Sección de categorías
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Categorías",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(5) { index ->
                CategoryCard(
                    title = when(index) {
                        0 -> "Juegos"
                        1 -> "Consolas"
                        2 -> "Accesorios"
                        3 -> "Merchandising"
                        else -> "Ofertas"
                    }
                )
            }

            // Sección de Productos en Oferta
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "🔥 Ofertas Especiales",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Scroll horizontal de productos en oferta
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    val productsOnSale = ProductsData.sampleProducts.filter { it.hasDiscount }
                    items(productsOnSale.size) { index ->
                        ProductGridCard(
                            product = productsOnSale[index],
                            onProductClick = { product ->
                                // TODO: Navegar a detalle del producto
                            },
                            onAddToCart = { product ->
                                // Agregar al carrito
                                cartItemCount.value += 1
                            }
                        )
                    }
                }
            }

            // Sección de Todos los Productos
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Todos los Productos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Lista vertical de todos los productos
            items(ProductsData.sampleProducts.size) { index ->
                ProductCard(
                    product = ProductsData.sampleProducts[index],
                    onProductClick = { product ->
                        // TODO: Navegar a detalle del producto
                    },
                    onAddToCart = { product ->
                        // Agregar al carrito
                        cartItemCount.value += 1
                    }
                )
            }

            // Mostrar resultados de búsqueda si hay texto
            if (searchQuery.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Resultados para: \"$searchQuery\"",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                item {
                    Text(
                        text = "Mostrando productos relacionados...",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }

    // Diálogo de notificaciones
    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = {
                showNotificationDialog = false
                notificationCount.value = 0
            },
            title = {
                Text(text = "Notificaciones")
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NotificationItem(
                        title = "Nueva oferta disponible",
                        message = "¡50% de descuento en juegos seleccionados!"
                    )
                    Divider()
                    NotificationItem(
                        title = "Producto añadido",
                        message = "Se agregó nuevo stock de PlayStation 5"
                    )
                    Divider()
                    NotificationItem(
                        title = "Pedido enviado",
                        message = "Tu pedido #12345 está en camino"
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showNotificationDialog = false
                    notificationCount.value = 0
                }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun CategoryCard(
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = androidx.compose.ui.Alignment.CenterStart
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun NotificationItem(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = message,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}
