package com.example.levelupgamer_grupo10

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelupgamer_grupo10.viewmodel.LoginViewModel

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val loginViewModel : LoginViewModel = viewModel()

            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    com.example.levelupgamer_grupo10.view.LoginScreen(navController, loginViewModel)
                }
                composable("welcome") {
                    com.example.levelupgamer_grupo10.view.WelcomeScreen(navController)
                }
            }
        }
    }
}