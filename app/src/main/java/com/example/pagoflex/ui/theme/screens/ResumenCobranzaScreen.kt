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
fun ResumenCobranzaScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val idEmpresa = uiState.idUsuarioLogueado.let { /* ejecutivo empresa */ 1 } // simplificado
    val (pagados, pendientes, vencidos) = viewModel.resumenCobranzaEmpresa(idEmpresa)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen de Cobranza") },
                navigationIcon = { TextButton(onClick = onVolver) { Text("Volver") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Resumen del mes", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Compromisos pagados: $pagados")
                    Text("Compromisos pendientes: $pendientes")
                    Text("Compromisos vencidos: $vencidos")
                    val total = pagados + pendientes + vencidos
                    val tasa = if (total > 0) (pagados * 100 / total) else 0
                    Text("Tasa de cobranza: $tasa%", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
