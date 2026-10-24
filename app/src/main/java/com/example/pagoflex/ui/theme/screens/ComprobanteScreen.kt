package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagoflex.viewmodel.PagoFlexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComprobanteScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comprobante de Pago") },
                navigationIcon = { TextButton(onClick = onVolver) { Text("Volver") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (!uiState.mostrarComprobante) {
                Text("No hay comprobante disponible.")
                return@Column
            }
            Text("Comprobante de Pago", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
            Text("Folio: ${uiState.comprobanteFolio}")
            Text("Fecha de pago: ${uiState.comprobanteFecha}")
            Text("Monto pagado: $${uiState.comprobanteMonto}")
            Text("Estado: Pagado", color = MaterialTheme.colorScheme.primary)
        }
    }
}
