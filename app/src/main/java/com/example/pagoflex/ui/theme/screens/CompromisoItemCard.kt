package com.example.pagoflex.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.ui.style.EstilosBoton
import com.example.pagoflex.ui.style.EstilosCampo

@Composable
fun CompromisoItemCard(
    compromiso: Compromiso,
    mostrarAccionPagar: Boolean = true,
    onVerDetalleClick: () -> Unit = {},
    onPagarClick: () -> Unit
) {
    val vencido = compromiso.estaVencido()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (vencido) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, if (vencido) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = compromiso.folio, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                val estadoTexto = when {
                    compromiso.idEstadoCompromiso == 3 -> "PAGADO"
                    vencido -> "ATRASADO"
                    else -> "PENDIENTE"
                }
                val colorEstado = when {
                    compromiso.idEstadoCompromiso == 3 -> MaterialTheme.colorScheme.primary
                    vencido -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.tertiary
                }
                Text(text = estadoTexto, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = colorEstado)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = compromiso.nombreEmpresa, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = compromiso.concepto, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Monto: $${compromiso.monto}", style = MaterialTheme.typography.bodyMedium)
            if (vencido) {
                Text(text = "Recargo por atraso: $${compromiso.recargoAtraso()}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                Text(text = "Total a pagar: $${compromiso.totalAPagar()}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Vence: ${compromiso.fechaVencimiento}", style = MaterialTheme.typography.bodyMedium)

            if (compromiso.idEstadoCompromiso != 3 && mostrarAccionPagar) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = onVerDetalleClick, modifier = Modifier.weight(1f)) { Text("Ver detalle") }
                    Button(onClick = onPagarClick, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("Pagar") }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = onVerDetalleClick, modifier = Modifier.fillMaxWidth()) { Text("Ver detalle") }
            }
        }
    }
}

