package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.ui.style.EstilosBoton
import com.example.pagoflex.ui.style.EstilosCampo
import com.example.pagoflex.ui.style.EstilosTarjeta
import com.example.pagoflex.viewmodel.PagoFlexViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCompromisoScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit,
    onPagar: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val compromiso = uiState.compromisoSeleccionadoParaPago

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Compromiso") },
            navigationIcon = { IconButton(onClick = onVolver) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") } },
                
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (compromiso == null) {
                Text("No hay compromiso seleccionado.")
                return@Column
            }
            val vencido = try {
                val f = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val d = f.parse(compromiso.fechaVencimiento)
                d != null && d.before(Date())
            } catch (e: Exception) { false }
            val recargo = if (vencido) (compromiso.monto * 0.05).toInt() else 0
            val total = compromiso.monto + recargo

            Text(compromiso.concepto, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
            Text("Folio: ${compromiso.folio}")
            Text("Empresa: ${compromiso.nombreEmpresa}")
            Text("Cuota: ${compromiso.cuota}")
            Text("Monto: $${compromiso.monto}")
            if (vencido) {
                Text("Recargo por atraso: $$recargo", color = MaterialTheme.colorScheme.error)
            }
            Text("Total a pagar: $$total", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
            Text("Vencimiento: ${compromiso.fechaVencimiento}")
            Text("Estado: ${if (compromiso.idEstadoCompromiso == 3) "Pagado" else "Pendiente"}")

            Spacer(modifier = Modifier.height(24.dp))
            if (compromiso.idEstadoCompromiso != 3) {
                Button(onClick = onPagar, modifier = Modifier.fillMaxWidth().height(50.dp), shape = EstilosCampo.forma) {
                    Text("Pagar")
                }
            }
        }
    }
}






