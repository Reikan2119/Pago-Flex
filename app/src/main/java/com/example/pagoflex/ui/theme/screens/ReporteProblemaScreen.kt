package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.components.CampoTextoPagoFlex
import com.example.pagoflex.viewmodel.PagoFlexViewModel

@Composable
fun ReporteProblemaScreen(
    viewModel: PagoFlexViewModel,
    onVolverAlDashboard: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val motivosDisponibles = listOf("Pago no reflejado", "Monto incorrecto", "Problema con la plataforma", "Otro")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Reportar un Problema",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Seleccione el motivo de su incidencia:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(modifier = Modifier.selectableGroup().padding(bottom = 16.dp)) {
            motivosDisponibles.forEach { motivo ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.motivoReporteSeleccionado == motivo,
                        onClick = { viewModel.onMotivoReporteCambia(motivo) }
                    )
                    Text(text = motivo, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        CampoTextoPagoFlex(
            valor = uiState.comentarioReporte,
            etiqueta = if (uiState.motivoReporteSeleccionado == "Otro") "Comentario (Obligatorio)" else "Comentario adicional (Opcional)",
            onValorCambia = { viewModel.onComentarioReporteCambia(it) },
            mensajeError = uiState.errorComentarioReporte
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onEnviarReporte() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar Reporte")
        }

        if (uiState.reporteExitoso) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¡Su reporte ha sido enviado con éxito a los agentes!",
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