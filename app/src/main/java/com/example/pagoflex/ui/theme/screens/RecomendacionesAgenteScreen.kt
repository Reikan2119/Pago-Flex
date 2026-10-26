package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.style.EstilosBoton
import com.example.pagoflex.ui.style.EstilosCampo
import com.example.pagoflex.ui.style.EstilosTarjeta
import com.example.pagoflex.viewmodel.PagoFlexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecomendacionesAgenteScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.cargarRecomendacionesAgente() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recomendaciones (Agente)") },
            navigationIcon = { IconButton(onClick = onVolver) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") } },
                
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (uiState.recomendacionesAgente.isEmpty()) {
                Text("No hay recomendaciones.")
                return@Column
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.recomendacionesAgente) { r ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("${r.nombre} - ${r.estado}", style = MaterialTheme.typography.titleSmall)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { viewModel.cambiarEstadoRecomendacion(r.id, "Aceptada") }) { Text("Aceptar") }
                                Button(onClick = { viewModel.cambiarEstadoRecomendacion(r.id, "Descartada") }) { Text("Descartar") }
                            }
                        }
                    }
                }
            }
        }
    }
}






