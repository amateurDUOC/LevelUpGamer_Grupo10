package com.grupo10.levelupgamer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grupo10.levelupgamer.ui.screens.AnimatedSplashScreen
import com.grupo10.levelupgamer.ui.screens.CartScreen
import com.grupo10.levelupgamer.ui.screens.HomeScreen
import com.grupo10.levelupgamer.ui.screens.LoginScreen
import com.grupo10.levelupgamer.ui.screens.ProfileScreen
import com.grupo10.levelupgamer.ui.screens.QRScannerScreen
import com.grupo10.levelupgamer.ui.screens.SignupScreen
import com.grupo10.levelupgamer.ui.screens.StoresScreen
import com.grupo10.levelupgamer.ui.theme.LevelUpGamerTheme
import com.grupo10.levelupgamer.viewmodel.CartViewModel
import com.grupo10.levelupgamer.viewmodel.LoginViewModel
import com.grupo10.levelupgamer.viewmodel.NavigationViewModel
import com.grupo10.levelupgamer.viewmodel.QRScannerViewModel
import com.grupo10.levelupgamer.viewmodel.SignupViewModel

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LevelUpGamerTheme {
                var showSplash by remember { mutableStateOf(true) }
                val navController = rememberNavController()
                val cartViewModel: CartViewModel = viewModel()
                val navigationViewModel: NavigationViewModel = viewModel()

                if (showSplash) {
                    AnimatedSplashScreen(
                        onSplashFinished = {
                            showSplash = false
                        }
                    )
                } else {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            val loginViewModel: LoginViewModel = viewModel()
                            val loginState by loginViewModel.state.collectAsState()
                            val loginUser by loginViewModel.currentUser.collectAsState()

                            LoginScreen(
                                viewModel = loginViewModel,
                                onLoginSuccess = {
                                    loginUser?.let { user ->
                                        // Limpiar carrito del usuario anterior
                                        cartViewModel.clearCartOnLogout()
                                        // Establecer nuevo usuario
                                        navigationViewModel.setCurrentUser(user)
                                        cartViewModel.setCurrentUser(user.id)
                                    }
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToSignup = {
                                    navController.navigate("signup")
                                }
                            )
                        }

                        composable("signup") {
                            val signupViewModel: SignupViewModel = viewModel()
                            val signupState by signupViewModel.state.collectAsState()
                            val signupUser by signupViewModel.registeredUser.collectAsState()

                            SignupScreen(
                                viewModel = signupViewModel,
                                onSignupSuccess = {
                                    signupUser?.let { user ->
                                        // Limpiar carrito del usuario anterior
                                        cartViewModel.clearCartOnLogout()
                                        // Establecer nuevo usuario
                                        navigationViewModel.setCurrentUser(user)
                                        cartViewModel.setCurrentUser(user.id)
                                    }
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("home") {
                            val currentUser by navigationViewModel.currentUser.collectAsState()

                            HomeScreen(
                                modifier = Modifier.fillMaxSize(),
                                cartViewModel = cartViewModel,
                                navigationViewModel = navigationViewModel,
                                currentUser = currentUser,
                                onLogout = {
                                    cartViewModel.clearCartOnLogout()
                                    navigationViewModel.logout()
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                },
                                onNavigateToCart = {
                                    navController.navigate("cart")
                                },
                                onNavigateToQRScanner = {
                                    navController.navigate("qr_scanner")
                                },
                                onNavigateToStores = {
                                    navController.navigate("stores")
                                },
                                onNavigateToProfile = {
                                    navController.navigate("profile")
                                }
                            )
                        }

                        composable("cart") {
                            CartScreen(
                                cartViewModel = cartViewModel,
                                onBackPressed = {
                                    navController.popBackStack()
                                },
                                onCheckoutClick = {
                                    // TODO: Implementar navegación a checkout
                                }
                            )
                        }

                        composable("qr_scanner") {
                            val qrViewModel: QRScannerViewModel = viewModel()
                            QRScannerScreen(
                                viewModel = qrViewModel,
                                cartViewModel = cartViewModel,
                                onBackPressed = {
                                    navController.popBackStack()
                                },
                                onProductFound = { productId ->
                                    // Simplemente regresar al home sin pasar el productId
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("stores") {
                            val currentUser by navigationViewModel.currentUser.collectAsState()
                            StoresScreen(
                                onBackPressed = {
                                    navController.popBackStack()
                                },
                                userLatitude = currentUser?.latitude ?: -33.0243,
                                userLongitude = currentUser?.longitude ?: -71.5518
                            )
                        }

                        composable("profile") {
                            ProfileScreen(
                                onBackPressed = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

