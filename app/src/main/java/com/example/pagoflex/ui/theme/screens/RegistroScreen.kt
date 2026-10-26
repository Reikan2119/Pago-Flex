package com.example.pagoflex.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.style.EstilosBoton
import com.example.pagoflex.ui.style.estiloAltoBoton
import com.example.pagoflex.ui.style.EstilosCampo
import com.example.pagoflex.ui.style.EstilosTarjeta
import com.example.pagoflex.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    viewModel: AuthViewModel,
    onRegistroSuccess: () -> Unit,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta - PagoFlex") },
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
                text = "Regístrate en PagoFlex",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.nombreRegistro,
                onValueChange = { viewModel.onNombreRegistroCambia(it) },
                label = { Text("Nombre Completo") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Nombre") },
                isError = uiState.errorNombreRegistro != null,
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                singleLine = true
            )
            if (uiState.errorNombreRegistro != null) {
                Text(
                    text = uiState.errorNombreRegistro!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.rutRegistro,
                onValueChange = { viewModel.onRutRegistroCambia(it) },
                label = { Text("RUT (sin puntos con guion)") },
                isError = uiState.errorRutRegistro != null,
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                colors = EstilosCampo.colores(),
                singleLine = true
            )
            if (uiState.errorRutRegistro != null) {
                Text(
                    text = uiState.errorRutRegistro!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.comunaRegistro,
                onValueChange = { viewModel.onComunaRegistroCambia(it) },
                label = { Text("Comuna") },
                isError = uiState.errorComunaRegistro != null,
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                colors = EstilosCampo.colores(),
                singleLine = true
            )
            if (uiState.errorComunaRegistro != null) {
                Text(
                    text = uiState.errorComunaRegistro!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.correoRegistro,
                onValueChange = { viewModel.onCorreoRegistroCambia(it) },
                label = { Text("Correo Electrónico") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Correo") },
                isError = uiState.errorCorreoRegistro != null,
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                singleLine = true
            )
            if (uiState.errorCorreoRegistro != null) {
                Text(
                    text = uiState.errorCorreoRegistro!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.contrasenaRegistro,
                onValueChange = { viewModel.onContrasenaRegistroCambia(it) },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
                isError = uiState.errorContrasenaRegistro != null,
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                singleLine = true
            )
            if (uiState.errorContrasenaRegistro != null) {
                Text(
                    text = uiState.errorContrasenaRegistro!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.contrasenaConfirmacionRegistro,
                onValueChange = { viewModel.onContrasenaConfirmacionRegistroCambia(it) },
                label = { Text("Confirmar Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
                isError = uiState.errorContrasenaConfirmacionRegistro != null,
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                colors = EstilosCampo.colores(),
                singleLine = true
            )
            if (uiState.errorContrasenaConfirmacionRegistro != null) {
                Text(
                    text = uiState.errorContrasenaConfirmacionRegistro!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onRegistrarUsuario() },
                shape = EstilosBoton.forma,
                colors = EstilosBoton.coloresPrincipal(),
                modifier = Modifier.fillMaxWidth().estiloAltoBoton()
            ) {
                Text(text = "Registrarse", style = MaterialTheme.typography.titleMedium)
            }

            AnimatedVisibility(visible = uiState.registroUsuarioExitoso) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Usuario creado con éxito. Ya puedes iniciar sesión.",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onVolver) {
                        Text("Volver a iniciar sesión")
                    }
                }
            }
        }
    }

}

