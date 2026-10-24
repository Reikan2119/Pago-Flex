package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.style.EstilosBoton
import com.example.pagoflex.ui.style.EstilosCampo
import com.example.pagoflex.ui.style.EstilosTarjeta
import com.example.pagoflex.viewmodel.PagoFlexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecomendarEntidadScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recomendar Entidad - PagoFlex") },
                
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
                text = "Recomienda una Organización",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            var expandedLista by remember { mutableStateOf(false) }

            OutlinedButton(
                onClick = { viewModel.onMostrarListaEntidades(true) },
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma
            ) {
                Text(if (uiState.nombreEntidadRecomendada.isBlank()) "Buscar entidad (opcional)" else "Entidad: ${uiState.nombreEntidadRecomendada}")
            }

            if (uiState.mostrarListaEntidades) {
                AlertDialog(
                    onDismissRequest = { viewModel.onMostrarListaEntidades(false) },
                    title = { Text("Seleccionar entidad") },
                    text = {
                        Column {
                            uiState.entidadesDisponibles.forEach { e ->
                                TextButton(onClick = { viewModel.onSeleccionarEntidad(e) }, modifier = Modifier.fillMaxWidth()) {
                                    Text("${e.nombre} - ${e.rubro} (${e.comuna})")
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { viewModel.onMostrarListaEntidades(false) }) { Text("Cerrar") }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.rubroEntidadRecomendada,
                onValueChange = { viewModel.onRubroEntidadCambia(it) },
                label = { Text("Rubro (ej. Municipalidad, Club, etc.)") },
                leadingIcon = { Icon(Icons.Filled.Category, contentDescription = "Rubro") },
                isError = uiState.errorRubroEntidad != null,
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                singleLine = true
            )
            if (uiState.errorRubroEntidad != null) {
                Text(
                    text = uiState.errorRubroEntidad!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.comunaEntidadRecomendada,
                onValueChange = { viewModel.onComunaEntidadCambia(it) },
                label = { Text("Comuna") },
                leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = "Comuna") },
                isError = uiState.errorComunaEntidad != null,
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                singleLine = true
            )
            if (uiState.errorComunaEntidad != null) {
                Text(
                    text = uiState.errorComunaEntidad!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onEnviarRecomendacion() },
                shape = EstilosCampo.forma,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Enviar Recomendación", style = MaterialTheme.typography.titleMedium)
            }

            if (uiState.recomendacionExitosa) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "¡Recomendación enviada con éxito!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}



