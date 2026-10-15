package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagoflex.viewmodel.PagoFlexViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarCompromisoScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Compromiso - PagoFlex") },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("Volver")
                    }
                }
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
                text = "Nuevo Compromiso de Pago",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.rutClienteCompromiso,
                onValueChange = { viewModel.onRutClienteCompromisoCambia(it) },
                label = { Text("RUT del Cliente") },
                leadingIcon = { Icon(Icons.Filled.Badge, contentDescription = "RUT") },
                isError = uiState.errorRutClienteCompromiso != null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            if (uiState.errorRutClienteCompromiso != null) {
                Text(
                    text = uiState.errorRutClienteCompromiso!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.conceptoCompromiso,
                onValueChange = { viewModel.onConceptoCompromisoCambia(it) },
                label = { Text("Concepto (ej. Cuota GC)") },
                leadingIcon = { Icon(Icons.Filled.Description, contentDescription = "Concepto") },
                isError = uiState.errorConceptoCompromiso != null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            if (uiState.errorConceptoCompromiso != null) {
                Text(
                    text = uiState.errorConceptoCompromiso!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.montoCompromiso,
                onValueChange = { viewModel.onMontoCompromisoCambia(it) },
                label = { Text("Monto (\$ mayor a 0) (RN-20)") },
                leadingIcon = { Icon(Icons.Filled.AttachMoney, contentDescription = "Monto") },
                isError = uiState.errorMontoCompromiso != null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            if (uiState.errorMontoCompromiso != null) {
                Text(
                    text = uiState.errorMontoCompromiso!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.fechaVencimientoCompromiso,
                onValueChange = { viewModel.onFechaVencimientoCompromisoCambia(it) },
                label = { Text("Fecha de Vencimiento (ej. 30-11-2026)") },
                leadingIcon = { Icon(Icons.Filled.DateRange, contentDescription = "Fecha") },
                isError = uiState.errorFechaVencimientoCompromiso != null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            if (uiState.errorFechaVencimientoCompromiso != null) {
                Text(
                    text = uiState.errorFechaVencimientoCompromiso!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onRegistrarCompromiso() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Registrar Compromiso", style = MaterialTheme.typography.titleMedium)
            }

            if (uiState.registroCompromisoExitoso) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "¡Compromiso registrado con éxito!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}