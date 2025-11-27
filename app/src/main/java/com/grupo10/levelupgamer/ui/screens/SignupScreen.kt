package com.grupo10.levelupgamer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo10.levelupgamer.R
import com.grupo10.levelupgamer.viewmodel.SignupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    viewModel: SignupViewModel,
    onSignupSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val addressSuggestions by viewModel.addressSuggestions.collectAsState()
    val isLoadingSuggestions by viewModel.isLoadingSuggestions.collectAsState()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    // --- Efectos ---
    LaunchedEffect(state.signupSuccess) {
        if (state.signupSuccess) {
            onSignupSuccess()
        }
    }

    LaunchedEffect(state.signupError) {
        state.signupError?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    // --- UI ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onNavigateToLogin) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                        Text(
                            "Iniciar sesión",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Tienda Level-Up Gamer",
                modifier = Modifier
                    .padding(25.dp)
                    .height(150.dp)
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
                    "Registro de Usuario",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp)
                        .padding(horizontal = 25.dp),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChange,
                    label = { Text("Nombre") },
                    isError = state.errors.name != null,
                    supportingText = {
                        state.errors.name?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                        .padding(top = 15.dp)
                )

                OutlinedTextField(
                    value = state.lastName,
                    onValueChange = viewModel::onLastNameChange,
                    label = { Text("Apellido") },
                    isError = state.errors.lastName != null,
                    supportingText = {
                        state.errors.lastName?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                )

                OutlinedTextField(
                    value = state.rut,
                    onValueChange = viewModel::onRutChange,
                    label = { Text("Rut") },
                    isError = state.errors.rut != null,
                    supportingText = {
                        state.errors.rut?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
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
                )

                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    label = { Text("Confirmar contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = state.errors.confirmPassword != null,
                    supportingText = {
                        state.errors.confirmPassword?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                )

                // Campo de dirección con autocompletado
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                ) {
                    OutlinedTextField(
                        value = state.address,
                        onValueChange = viewModel::onAddressChange,
                        label = { Text("Dirección") },
                        placeholder = { Text("Ej: Alvarez 1130, Viña del Mar") },
                        isError = state.errors.address != null,
                        supportingText = {
                            state.errors.address?.let {
                                Text(it, color = MaterialTheme.colorScheme.error)
                            } ?: if (state.address.length < 3) {
                                Text("Escribe al menos 3 caracteres para ver sugerencias", fontSize = 12.sp)
                            } else null
                        },
                        trailingIcon = {
                            if (isLoadingSuggestions) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Lista de sugerencias
                    if (addressSuggestions.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                addressSuggestions.forEach { suggestion ->
                                    Text(
                                        text = suggestion.displayName,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.selectAddressSuggestion(suggestion)
                                            }
                                            .padding(12.dp),
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (suggestion != addressSuggestions.last()) {
                                        Divider()
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = viewModel::signup,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 125.dp)
                        .padding(bottom = 35.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Registrarse")
                    }
                }
            }
        }
    }
}
