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
import com.grupo10.levelupgamer.ui.screens.HomeScreen
import com.grupo10.levelupgamer.ui.screens.LoginScreen
import com.grupo10.levelupgamer.ui.screens.QRScannerScreen
import com.grupo10.levelupgamer.ui.screens.SignupScreen
import com.grupo10.levelupgamer.ui.theme.LevelUpGamerTheme
import com.grupo10.levelupgamer.viewmodel.LoginViewModel
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
                            LoginScreen(
                                viewModel = loginViewModel,
                                onLoginSuccess = {
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
                            SignupScreen(
                                viewModel = signupViewModel,
                                onSignupSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = "home?productId={productId}",
                            arguments = listOf(
                                navArgument("productId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getInt("productId") ?: -1
                            HomeScreen(
                                modifier = Modifier.fillMaxSize(),
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                },
                                onNavigateToQRScanner = {
                                    navController.navigate("qr_scanner")
                                },
                                scannedProductId = if (productId > 0) productId else null
                            )
                        }

                        composable("qr_scanner") {
                            val qrViewModel: QRScannerViewModel = viewModel()
                            QRScannerScreen(
                                viewModel = qrViewModel,
                                onBackPressed = {
                                    navController.popBackStack()
                                },
                                onProductFound = { productId ->
                                    navController.navigate("home?productId=$productId") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

