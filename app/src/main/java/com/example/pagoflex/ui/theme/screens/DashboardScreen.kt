package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.viewmodel.PagoFlexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: PagoFlexViewModel,
    onNavegarAReporte: () -> Unit,
    onNavegarARecomendar: () -> Unit,
    onNavegarARegistrarCompromiso: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val rolActual = uiState.rolSeleccionado ?: RolUsuario.USUARIO_FINAL
    val nombreUsuario = uiState.nombreLogin.ifBlank { "Usuario" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PagoFlex", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onCerrarSesion) {
                        Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "¡Hola, $nombreUsuario!",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Perfil activo: ${rolActual.etiqueta}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            when (rolActual) {
                RolUsuario.USUARIO_FINAL -> {
                    Text(
                        text = "Tus compromisos de pago:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(uiState.compromisos) { compromiso ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = compromiso.concepto,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = when (compromiso.idEstadoCompromiso) {
                                                3 -> MaterialTheme.colorScheme.primaryContainer
                                                2 -> MaterialTheme.colorScheme.errorContainer
                                                else -> MaterialTheme.colorScheme.secondaryContainer
                                            }
                                        ) {
                                            Text(
                                                text = when (compromiso.idEstadoCompromiso) {
                                                    3 -> "PAGADO"
                                                    2 -> "VENCIDO"
                                                    else -> "PENDIENTE"
                                                },
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                                    Text(text = "Folio: ${compromiso.folio}", style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        text = "Monto: \$${compromiso.monto}",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(text = "Vencimiento: ${compromiso.fechaVencimiento}", style = MaterialTheme.typography.bodySmall)

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = { viewModel.onPagarCompromiso(compromiso) },
                                        enabled = compromiso.idEstadoCompromiso != 3,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(if (compromiso.idEstadoCompromiso == 3) "Cuota Pagada" else "Pagar Cuota")
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onNavegarAReporte,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Reportar Problema")
                        }

                        OutlinedButton(
                            onClick = onNavegarARecomendar,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Recomendar Entidad")
                        }
                    }
                }

                RolUsuario.AGENTE -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Panel de Agente PagoFlex",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Aquí puedes gestionar las incidencias reportadas por los usuarios y verificar el estado de las cobranzas.")
                        }
                    }
                }

                RolUsuario.EJECUTIVO -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Panel de Ejecutivo de Empresa",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Aquí puedes emitir nuevos cobros para tus clientes y revisar el historial de cuotas recaudadas.")

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onNavegarARegistrarCompromiso,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Registrar Nuevo Compromiso")
                            }
                        }
                    }
                }
            }
        }
    }
}