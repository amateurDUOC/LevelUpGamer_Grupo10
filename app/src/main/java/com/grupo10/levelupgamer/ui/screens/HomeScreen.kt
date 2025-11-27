package com.grupo10.levelupgamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import kotlinx.coroutines.launch
import com.grupo10.levelupgamer.ui.components.BottomNavigationBar
import com.grupo10.levelupgamer.ui.components.HomeHeader
import com.grupo10.levelupgamer.ui.components.ProductCard
import com.grupo10.levelupgamer.ui.components.ProductGridCard
import com.grupo10.levelupgamer.viewmodel.CartViewModel
import com.grupo10.levelupgamer.viewmodel.HomeViewModel
import com.grupo10.levelupgamer.viewmodel.StoreViewModel
import com.grupo10.levelupgamer.model.User
import com.grupo10.levelupgamer.ui.components.StoreRecommendationCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToQRScanner: () -> Unit = {},
    onNavigateToStores: () -> Unit = {},
    cartViewModel: CartViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    storeViewModel: StoreViewModel = viewModel(),
    currentUser: User? = null
) {
    val homeState by homeViewModel.uiState.collectAsState()
    val storeState by storeViewModel.uiState.collectAsState()
    var showNotificationDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    // Actualizar la tienda más cercana cuando cambia el usuario
    LaunchedEffect(currentUser) {
        storeViewModel.updateNearestStore(currentUser)
    }

    // Estado para controlar el drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                    searchQuery = homeState.searchQuery,
                    onSearchQueryChange = { homeViewModel.onSearchQueryChange(it) },
                    onNotificationClick = {
                        showNotificationDialog = true
                        onNotificationClick()
                    },
                    notificationCount = homeState.notificationCount,
                    onLogoutClick = onLogout
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    modifier = Modifier,
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        when (tab) {
                            0 -> selectedTab = 0
                            1 -> onNavigateToQRScanner()
                            2 -> onNavigateToCart()
                            3 -> {
                                selectedTab = 3
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

                // Tarjeta de recomendación de tienda cercana
                storeState.nearestStore?.let { store ->
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        StoreRecommendationCard(
                            store = store,
                            distance = storeViewModel.getFormattedDistance(storeState.distanceToNearest),
                            onViewStoresClick = onNavigateToStores
                        )
                    }
                }

                // Estado de Loading
                if (homeState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(48.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Cargando productos...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }

                // Estado de Error
                if (!homeState.isLoading && homeState.error != null) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = if (homeState.isNetworkError)
                                        Icons.Default.Info
                                    else
                                        Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = if (homeState.isNetworkError)
                                        "Sin conexión"
                                    else
                                        "Error del servidor",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = homeState.error ?: "Error desconocido",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    textAlign = TextAlign.Center
                                )
                                Button(
                                    onClick = { homeViewModel.loadProducts() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                }

                // Sección de Productos en Oferta
                if (!homeState.isLoading && homeState.error == null && homeState.productsOnSale.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "🔥 Ofertas Especiales",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            items(homeState.productsOnSale.size) { index ->
                                ProductGridCard(
                                    product = homeState.productsOnSale[index],
                                    onProductClick = { product ->
                                        homeViewModel.selectProduct(product)
                                    },
                                    onAddToCart = { product ->
                                        cartViewModel.addToCart(product)
                                    }
                                )
                            }
                        }
                    }
                }

                // Sección de Todos los Productos
                if (!homeState.isLoading && homeState.error == null && homeState.filteredProducts.isNotEmpty()) {
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

                    items(homeState.filteredProducts.size) { index ->
                        ProductCard(
                            product = homeState.filteredProducts[index],
                            onProductClick = { product ->
                                homeViewModel.selectProduct(product)
                            },
                            onAddToCart = { product ->
                                cartViewModel.addToCart(product)
                            }
                        )
                    }

                    if (homeState.searchQuery.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Resultados para: \"${homeState.searchQuery}\"",
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
        }

        // Diálogo de detalles del producto
        homeState.selectedProduct?.let { product ->
            AlertDialog(
                onDismissRequest = { homeViewModel.selectProduct(null) },
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

                        Text(
                            text = product.category.name,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = product.description,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )

                        Divider()

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
                            homeViewModel.selectProduct(null)
                        },
                        enabled = product.stock > 0
                    ) {
                        Text("Agregar al carrito")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { homeViewModel.selectProduct(null) }) {
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
                    homeViewModel.clearNotifications()
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
                        homeViewModel.clearNotifications()
                    }) {
                        Text("Cerrar")
                    }
                }
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

