package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.style.EstilosBoton
import com.example.pagoflex.ui.style.EstilosCampo
import com.example.pagoflex.ui.style.EstilosTarjeta
import com.example.pagoflex.viewmodel.PagoFlexViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesAgenteScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.cargarReportesAgente() }

    var filtroEstado by remember { mutableStateOf("Todos") }
    val estados = listOf("Todos", "Pendiente", "En gestión", "Resuelto")
    val reportesFiltrados = uiState.reportesAgente.filter { r ->
        filtroEstado == "Todos" || r.estado == filtroEstado
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportes de Problema (Agente)") },
            navigationIcon = { IconButton(onClick = onVolver) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") } },
                
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Filtro por estado
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = filtroEstado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filtrar por estado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    estados.forEach { e ->
                        DropdownMenuItem(text = { Text(e) }, onClick = { filtroEstado = e; expanded = false })
                    }
                }
            }

            if (reportesFiltrados.isEmpty()) {
                Text("No hay reportes para el filtro seleccionado.")
                return@Column
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(reportesFiltrados) { r ->
                    val urgente = r.estado == "Pendiente" && diasSinGestionar(r.fecha) >= 2
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = if (urgente) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface)) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("${r.motivo} - ${r.estado}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                if (urgente) {
                                    Text("⚠ >2 días", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                            }
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

private fun diasSinGestionar(fecha: String): Int {
    return try {
        val f = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val d = f.parse(fecha) ?: return 0
        val diff = (Date().time - d.time) / (1000 * 60 * 60 * 24)
        diff.toInt()
    } catch (e: Exception) { 0 }
}






