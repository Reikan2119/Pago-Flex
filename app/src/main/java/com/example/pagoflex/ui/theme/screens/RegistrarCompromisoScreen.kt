package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.components.CampoTextoPagoFlex
import com.example.pagoflex.viewmodel.PagoFlexViewModel

@Composable
fun RegistrarCompromisoScreen(
    viewModel: PagoFlexViewModel,
    onVolverAlDashboard: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Registrar Compromiso de Pago",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        CampoTextoPagoFlex(
            valor = uiState.rutClienteCompromiso,
            etiqueta = "RUT del Cliente (ej: 12345678-9)",
            onValorCambia = { viewModel.onRutClienteCompromisoCambia(it) },
            mensajeError = uiState.errorRutClienteCompromiso
        )

        CampoTextoPagoFlex(
            valor = uiState.conceptoCompromiso,
            etiqueta = "Concepto / Descripción",
            onValorCambia = { viewModel.onConceptoCompromisoCambia(it) },
            mensajeError = uiState.errorConceptoCompromiso
        )

        CampoTextoPagoFlex(
            valor = uiState.montoCompromiso,
            etiqueta = "Monto en CLP (Mayor a \$0)",
            onValorCambia = { viewModel.onMontoCompromisoCambia(it) },
            mensajeError = uiState.errorMontoCompromiso
        )

        CampoTextoPagoFlex(
            valor = uiState.fechaVencimientoCompromiso,
            etiqueta = "Fecha Vencimiento (DD-MM-AAAA)",
            onValorCambia = { viewModel.onFechaVencimientoCompromisoCambia(it) },
            mensajeError = uiState.errorFechaVencimientoCompromiso
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onRegistrarCompromiso() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Compromiso")
        }

        if (uiState.registroCompromisoExitoso) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¡Compromiso registrado exitosamente!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onVolverAlDashboard,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Panel Principal")
        }
    }
}