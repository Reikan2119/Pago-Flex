package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import com.example.pagoflex.viewmodel.PagoFlexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesAgenteScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.cargarReportesAgente() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportes de Problema (Agente)") },
                navigationIcon = { TextButton(onClick = onVolver) { Text("Volver") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (uiState.reportesAgente.isEmpty()) {
                Text("No hay reportes.")
                return@Column
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.reportesAgente) { r ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("${r.motivo} - ${r.estado}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                            Text("Usuario ${r.idUsuario} · ${r.fecha}")
                            Text(r.comentario)
                            if (r.respuesta != null) Text("Respuesta: ${r.respuesta}", style = MaterialTheme.typography.bodySmall)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (r.estado == "Pendiente") {
                                    Button(onClick = { viewModel.tomarReporte(r.id) }) { Text("Tomar") }
                                }
                                if (r.estado == "En gestión") {
                                    OutlinedTextField(
                                        value = uiState.respuestaReporte,
                                        onValueChange = { viewModel.onRespuestaReporteCambia(it) },
                                        label = { Text("Respuesta") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    Button(onClick = { viewModel.resolverReporte(r.id) }) { Text("Resolver") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
