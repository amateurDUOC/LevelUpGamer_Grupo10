package com.grupo10.levelupgamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import kotlinx.coroutines.launch
import com.grupo10.levelupgamer.model.ProductsData
import com.grupo10.levelupgamer.ui.components.BottomNavigationBar
import com.grupo10.levelupgamer.ui.components.HomeHeader
import com.grupo10.levelupgamer.ui.components.ProductCard
import com.grupo10.levelupgamer.ui.components.ProductGridCard
import com.grupo10.levelupgamer.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToQRScanner: () -> Unit = {},
    cartViewModel: CartViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var showNotificationDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    var selectedProduct by remember { mutableStateOf<com.grupo10.levelupgamer.model.Product?>(null) }

    // Estado para controlar el drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Simular contador de notificaciones
    val notificationCount = remember { mutableStateOf(3) }

    // Observar contador de items en carrito desde el ViewModel
    val cartItems by cartViewModel.getCartItems()?.observeAsState(emptyList()) ?: remember { mutableStateOf(emptyList()) }
    val cartItemCount = cartItems.sumOf { it.quantity }

    // Observar resultado de agregar al carrito
    val addToCartResult by cartViewModel.addToCartResult.observeAsState()

    // Snackbar para mostrar mensaje
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar Snackbar cuando se agrega un producto
    LaunchedEffect(addToCartResult) {
        if (addToCartResult != null) {
            snackbarHostState.showSnackbar(
                message = addToCartResult!!,
                duration = SnackbarDuration.Short
            )
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            com.grupo10.levelupgamer.ui.components.NavigationDrawerContent(
                onDismiss = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                    onTabSelected = { tab ->
                        when (tab) {
                            0 -> selectedTab = 0 // Inicio - ya estamos en home
                            1 -> onNavigateToQRScanner() // QR Scanner tab
                            2 -> onNavigateToCart() // Carrito tab
                            3 -> {
                                selectedTab = 3 // Menú
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        }
                    },
                    cartItemCount = cartItemCount
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
                                selectedProduct = product
                            },
                            onAddToCart = { product ->
                                cartViewModel.addToCart(product)
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
                        selectedProduct = product
                    },
                    onAddToCart = { product ->
                        cartViewModel.addToCart(product)
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

        // Diálogo de detalles del producto
        selectedProduct?.let { product ->
        AlertDialog(
            onDismissRequest = { selectedProduct = null },
            title = {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Icono del producto
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎮", fontSize = 48.sp)
                    }

                    // Categoría
                    Text(
                        text = product.category.name,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Descripción
                    Text(
                        text = product.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Divider()

                    // Precio
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            if (product.hasDiscount) {
                                Text(
                                    text = "$${String.format("%,.0f", product.price)}",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    textDecoration = TextDecoration.LineThrough
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "$${String.format("%,.0f", product.finalPrice)}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Red
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color.Red)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "-${product.discount}%",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = "$${String.format("%,.0f", product.price)}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // Stock
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Stock:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (product.stock > 0) "${product.stock} disponibles" else "Sin stock",
                            fontSize = 14.sp,
                            color = if (product.stock > 0) MaterialTheme.colorScheme.tertiary else Color.Red
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        cartViewModel.addToCart(product)
                        selectedProduct = null
                    },
                    enabled = product.stock > 0
                ) {
                    Text("Agregar al carrito")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedProduct = null }) {
                    Text("Cerrar")
                }
            }
        )
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
    } // Cierre del ModalNavigationDrawer
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

