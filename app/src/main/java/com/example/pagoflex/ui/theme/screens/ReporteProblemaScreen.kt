package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagoflex.viewmodel.PagoFlexViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReporteProblemaScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var expandedMotivo by remember { mutableStateOf(false) }
    val motivosDisponibles = listOf("Pago no reflejado", "Monto incorrecto", "Error en convenio", "Otro")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportar Problema - PagoFlex") },
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
                text = "Reportar una Incidencia",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            ExposedDropdownMenuBox(
                expanded = expandedMotivo,
                onExpandedChange = { expandedMotivo = !expandedMotivo },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = uiState.motivoReporteSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Motivo del Reporte") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMotivo) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = expandedMotivo,
                    onDismissRequest = { expandedMotivo = false }
                ) {
                    motivosDisponibles.forEach { motivo ->
                        DropdownMenuItem(
                            text = { Text(motivo) },
                            onClick = {
                                viewModel.onMotivoReporteCambia(motivo)
                                expandedMotivo = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Comentario
            OutlinedTextField(
                value = uiState.comentarioReporte,
                onValueChange = { viewModel.onComentarioReporteCambia(it) },
                label = { Text("Comentario o detalle (Obligatorio si es 'Otro')") },
                isError = uiState.errorComentarioReporte != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp)
            )
            if (uiState.errorComentarioReporte != null) {
                Text(
                    text = uiState.errorComentarioReporte!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onEnviarReporte() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Enviar Reporte", style = MaterialTheme.typography.titleMedium)
            }

            if (uiState.reporteExitoso) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "¡Reporte enviado exitosamente!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}