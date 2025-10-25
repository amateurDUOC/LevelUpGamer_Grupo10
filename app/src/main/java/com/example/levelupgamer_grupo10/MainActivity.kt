package com.example.levelupgamer_grupo10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelupgamer_grupo10.view.homeScreen
import com.example.levelupgamer_grupo10.view.loginScreen
import com.example.levelupgamer_grupo10.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val loginViewModel : LoginViewModel = viewModel()

            NavHost(navController = navController, startDestination = "LoginScreen") {
                composable("LoginScreen") {
                    loginScreen(navController, loginViewModel)
                }
            }
        }
    }
}