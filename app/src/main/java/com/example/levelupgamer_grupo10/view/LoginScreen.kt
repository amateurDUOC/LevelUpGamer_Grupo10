package com.example.levelupgamer_grupo10.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import com.example.levelupgamer_grupo10.R
import com.example.levelupgamer_grupo10.viewmodel.LoginViewModel

@Composable
fun loginScreen(navController: NavController, viewModel: LoginViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
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
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .shadow(1.dp, RoundedCornerShape(20.dp), spotColor = Color.Black)

        ) {
            Text("Inicio de Sesión",
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
                onClick = {
                    if (viewModel.validateLoginForm()) {
                        navController.navigate("")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 125.dp)
                    .padding(bottom = 25.dp)
            ) {
                Text("Ingresar")
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
                    onClick = {
                        /*navController.navigate("")*/
                    }
                ) {
                    Text("Regístrate")
                }
            }
        }
    }
}