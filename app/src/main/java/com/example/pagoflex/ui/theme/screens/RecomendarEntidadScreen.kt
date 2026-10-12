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
fun RecomendarEntidadScreen(
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
            text = "Recomendar Organización",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        CampoTextoPagoFlex(
            valor = uiState.nombreEntidadRecomendada,
            etiqueta = "Nombre de la Organización",
            onValorCambia = { viewModel.onNombreEntidadCambia(it) },
            mensajeError = uiState.errorNombreEntidad
        )

        CampoTextoPagoFlex(
            valor = uiState.rubroEntidadRecomendada,
            etiqueta = "Rubro / Área (ej: Educación, Servicios)",
            onValorCambia = { viewModel.onRubroEntidadCambia(it) },
            mensajeError = uiState.errorRubroEntidad
        )

        CampoTextoPagoFlex(
            valor = uiState.comunaEntidadRecomendada,
            etiqueta = "Comuna",
            onValorCambia = { viewModel.onComunaEntidadCambia(it) },
            mensajeError = uiState.errorComunaEntidad
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onEnviarRecomendacion() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar Recomendación")
        }

        if (uiState.recomendacionExitosa) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¡Gracias por tu recomendación! Será evaluada.",
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