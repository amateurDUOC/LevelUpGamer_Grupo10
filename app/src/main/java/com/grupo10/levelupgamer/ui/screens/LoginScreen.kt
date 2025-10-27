package com.grupo10.levelupgamer.ui.screens

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.grupo10.levelupgamer.R
import com.grupo10.levelupgamer.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // --- Verificación de BiometricManager ---
    val biometricManager = remember { BiometricManager.from(context) }

    // --- Lógica de BiometricPrompt ---
    val biometricPrompt = remember {
        val activity = context as FragmentActivity
        val executor = ContextCompat.getMainExecutor(context)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                viewModel.onBiometricAuthSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // No mostramos el error si el usuario simplemente canceló
                if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON && errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
                    viewModel.onBiometricAuthError("Error de autenticación: $errString")
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                viewModel.onBiometricAuthError("La biometría no coincide. Inténtalo de nuevo.")
            }
        }
        BiometricPrompt(activity, executor, callback)
    }

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Inicio de Sesión Biométrico")
        .setSubtitle("Usa tu huella para iniciar sesión")
        .setNegativeButtonText("Cancelar")
        .build()

    // --- Efectos ---
    LaunchedEffect(state.showBiometricPrompt) {
        if (state.showBiometricPrompt) {
            // Verificar disponibilidad antes de mostrar el prompt
            when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or BIOMETRIC_WEAK)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    biometricPrompt.authenticate(promptInfo)
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    viewModel.onBiometricAuthError("Este dispositivo no tiene sensor biométrico")
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    viewModel.onBiometricAuthError("El sensor biométrico no está disponible")
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    viewModel.onBiometricAuthError("No hay huellas registradas. Por favor configura tu huella en Ajustes")
                }
                else -> {
                    viewModel.onBiometricAuthError("Autenticación biométrica no disponible")
                }
            }
            viewModel.onBiometricPromptHandled()
        }
    }

    LaunchedEffect(state.loginSuccess) {
        if (state.loginSuccess) {
            onLoginSuccess()
        }
    }

    LaunchedEffect(state.loginError) {
        state.loginError?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    // --- UI ---
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(color = Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Tienda Level-Up Gamer",
                modifier = Modifier
                    .padding(25.dp)
                    .height(200.dp)
                    .shadow(20.dp, RoundedCornerShape(150.dp), spotColor = Color.Cyan)
                    .clip(RoundedCornerShape(150.dp)),
                contentScale = ContentScale.Crop
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .shadow(1.dp, RoundedCornerShape(20.dp), spotColor = Color.Black)

            ) {
                Text(
                    "Inicio de Sesión",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp)
                        .padding(horizontal = 25.dp),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                OutlinedTextField(
                    value = state.email,
                    onValueChange = viewModel::onEmailChange,
                    label = { Text("Correo electrónico") },
                    isError = state.errors.email != null,
                    supportingText = {
                        state.errors.email?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                        .padding(top = 15.dp)
                )

                OutlinedTextField(
                    value = state.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = state.errors.password != null,
                    supportingText = {
                        state.errors.password?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                        .padding(bottom = 20.dp)
                )

                Button(
                    onClick = viewModel::login,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 125.dp)
                ) {
                    Text("Ingresar")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = viewModel::onBiometricLoginRequested,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp)
                        .padding(bottom = 20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Biometría",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Iniciar con Biometría")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 35.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("¿No tienes una cuenta?")
                    TextButton(
                        onClick = onNavigateToSignup
                    ) {
                        Text("Regístrate")
                    }
                }
            }
        }
    }
}

