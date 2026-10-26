package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.style.EstilosBoton
import com.example.pagoflex.ui.style.EstilosCampo
import com.example.pagoflex.ui.style.EstilosTarjeta
import com.example.pagoflex.viewmodel.PagoFlexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialPagosScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagados = uiState.compromisos.filter { it.idEstadoCompromiso == 3 }

    var filtroEmpresa by remember { mutableStateOf("Todas") }
    var filtroPeriodo by remember { mutableStateOf("Todos") }
    val empresas = remember { (listOf("Todas") + pagados.map { it.nombreEmpresa }.distinct()) }
    val periodos = remember { (listOf("Todos") + pagados.map { it.fechaVencimiento.substring(3) }.distinct()) }

    val filtrados = pagados.filter { p ->
        (filtroEmpresa == "Todas" || p.nombreEmpresa == filtroEmpresa) &&
        (filtroPeriodo == "Todos" || p.fechaVencimiento.substring(3) == filtroPeriodo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Pagos") },
            navigationIcon = { IconButton(onClick = onVolver) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") } },
                
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                
                var expandedE by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expandedE, onExpandedChange = { expandedE = !expandedE }, modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = filtroEmpresa,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Empresa") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedE) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedE, onDismissRequest = { expandedE = false }) {
                        empresas.forEach { e ->
                            DropdownMenuItem(text = { Text(e) }, onClick = { filtroEmpresa = e; expandedE = false })
                        }
                    }
                }
                
                var expandedP by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expandedP, onExpandedChange = { expandedP = !expandedP }, modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = filtroPeriodo,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Período") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedP) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedP, onDismissRequest = { expandedP = false }) {
                        periodos.forEach { p ->
                            DropdownMenuItem(text = { Text(p) }, onClick = { filtroPeriodo = p; expandedP = false })
                        }
                    }
                }
            }

            if (filtrados.isEmpty()) {
                Text("No hay pagos para los filtros seleccionados.")
                return@Column
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtrados) { c ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(c.concepto, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Text("Folio: ${c.folio}")
                            Text("Empresa: ${c.nombreEmpresa}")
                            Text("Monto: $${c.monto}")
                            Text("Vencimiento: ${c.fechaVencimiento}")
                            Text("Estado: Pagado", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}






