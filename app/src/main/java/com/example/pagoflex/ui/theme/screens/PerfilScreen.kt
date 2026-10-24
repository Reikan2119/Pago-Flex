package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.style.EstilosBoton
import com.example.pagoflex.ui.style.EstilosCampo
import com.example.pagoflex.ui.style.EstilosTarjeta
import com.example.pagoflex.viewmodel.PagoFlexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil - PagoFlex") },
                
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Personalizar datos",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = uiState.perfilRut,
                onValueChange = { viewModel.onPerfilRutCambia(it) },
                label = { Text("RUT") },
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                colors = EstilosCampo.colores(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.perfilNombre,
                onValueChange = { viewModel.onPerfilNombreCambia(it) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.perfilCorreo,
                onValueChange = { viewModel.onPerfilCorreoCambia(it) },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.perfilComuna,
                onValueChange = { viewModel.onPerfilComunaCambia(it) },
                label = { Text("Comuna") },
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                singleLine = true
            )

            Button(
                onClick = { viewModel.onGuardarPerfil() },
                shape = EstilosCampo.forma,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Guardar cambios", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}




