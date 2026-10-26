package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.style.EstilosBoton
import com.example.pagoflex.viewmodel.AuthViewModel
import com.example.pagoflex.ui.style.EstilosCampo
import com.example.pagoflex.ui.style.estiloAltoBoton
import androidx.compose.ui.res.stringResource
import com.example.pagoflex.R
import com.example.pagoflex.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginSimuladoScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Iniciar Sesión - PagoFlex") },
            navigationIcon = { IconButton(onClick = onVolver) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") } },
                
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.bienvenido),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = uiState.correoLogin,
                onValueChange = { viewModel.onCorreoLoginCambia(it) },
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Correo") },
                isError = uiState.errorCorreoLogin != null,
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                colors = EstilosCampo.colores(),
                singleLine = true
            )
            if (uiState.errorCorreoLogin != null) {
                Text(
                    text = uiState.errorCorreoLogin!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.contrasenaLogin,
                onValueChange = { viewModel.onContrasenaLoginCambia(it) },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
                isError = uiState.errorContrasenaLogin != null,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                colors = EstilosCampo.colores(),
                singleLine = true
            )
            if (uiState.errorContrasenaLogin != null) {
                Text(
                    text = uiState.errorContrasenaLogin!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val exito = viewModel.onIntentarLogin()
                    if (exito) onLoginSuccess()
                },
                shape = EstilosBoton.forma,
                colors = EstilosBoton.coloresPrincipal(),
                elevation = EstilosBoton.elevacion(),
                modifier = Modifier
                    .fillMaxWidth()
                    .estiloAltoBoton()
            ) {
                Text(text = stringResource(R.string.ingresar), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}






