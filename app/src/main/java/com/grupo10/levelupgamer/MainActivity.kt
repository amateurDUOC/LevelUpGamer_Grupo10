package com.grupo10.levelupgamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.grupo10.levelupgamer.ui.screens.AnimatedSplashScreen
import com.grupo10.levelupgamer.ui.screens.HomeScreen
import com.grupo10.levelupgamer.ui.theme.LevelUpGamerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LevelUpGamerTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    AnimatedSplashScreen(
                        onSplashFinished = {
                            showSplash = false
                        }
                    )
                } else {
                    HomeScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

