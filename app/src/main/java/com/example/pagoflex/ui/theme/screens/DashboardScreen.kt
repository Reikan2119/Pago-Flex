package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.domain.VisibilidadPorRol
import com.example.pagoflex.model.RolUsuario
import androidx.compose.foundation.layout.Row
import com.example.pagoflex.ui.components.AvatarIniciales
import com.example.pagoflex.ui.components.InsigniaRol
import com.example.pagoflex.ui.theme.colorDeRol
import com.example.pagoflex.viewmodel.PagoFlexViewModel
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.example.pagoflex.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: PagoFlexViewModel,
    onNavegarAReporte: () -> Unit,
    onNavegarARecomendar: () -> Unit,
    onNavegarARegistrarCompromiso: () -> Unit,
    onNavegarAPago: () -> Unit,
    onNavegarAPerfil: () -> Unit,
    onNavegarADetalle: () -> Unit,
    onNavegarAReportesAgente: () -> Unit,
    onNavegarAResumenCobranza: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                NavigationDrawerItem(
                    label = { Text("Mi perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavegarAPerfil()
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Cerrar sesión") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onCerrarSesion()
                    }
                )
            }
        }
    ) {
        Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.dashboard_title)) },
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
            val esAgente = uiState.rolSeleccionado == RolUsuario.AGENTE

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AvatarIniciales(
                    iniciales = (uiState.nombreLogin.take(1).ifBlank { "U" }),
                    color = colorDeRol(uiState.rolSeleccionado ?: RolUsuario.USUARIO_FINAL)
                )
                Column {
                    Text(
                        text = if (esAgente) "Panel de Agente PagoFlex" else "Hola, ${uiState.nombreLogin.ifBlank { "Usuario" }}",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (esAgente) "Supervisión de cartera y registro de compromisos" else "Perfil: ${uiState.rolSeleccionado?.etiqueta ?: "Sin perfil"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            InsigniaRol(rol = uiState.rolSeleccionado)

            if (VisibilidadPorRol.esSupervisor(uiState.rolSeleccionado)) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (uiState.rolSeleccionado == RolUsuario.AGENTE) {
                        Button(onClick = onNavegarAReportesAgente, modifier = Modifier.weight(1f)) {
                            Text("Reportes")
                        }
                    }
                    if (uiState.rolSeleccionado == RolUsuario.EJECUTIVO) {
                        Button(onClick = onNavegarAResumenCobranza, modifier = Modifier.weight(1f)) {
                            Text("Cobranza")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val pendientes = uiState.compromisos.filter { it.idEstadoCompromiso != 3 }
                    val totalPorPagar = pendientes.sumOf { it.monto }
                    val proximo = pendientes.minByOrNull { it.fechaVencimiento }
                    Text(
                        text = if (pendientes.isNotEmpty())
                            "Tienes ${pendientes.size} compromiso(s) pendiente(s). Total por pagar: $$totalPorPagar"
                        else
                            "Estás al día. No tienes compromisos pendientes.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (proximo != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Próximo vencimiento: ${proximo.fechaVencimiento} (${proximo.nombreEmpresa})",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onNavegarAReporte,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(stringResource(R.string.btn_reportar), style = MaterialTheme.typography.labelMedium)
                }
                Button(
                    onClick = onNavegarARecomendar,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(stringResource(R.string.btn_recomendar), style = MaterialTheme.typography.labelMedium)
                }
                if (VisibilidadPorRol.puedeRegistrarCompromiso(uiState.rolSeleccionado)) {
                    Button(
                        onClick = onNavegarARegistrarCompromiso,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(stringResource(R.string.btn_registrar), style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (esAgente) "Cartera de Compromisos (Supervisión)" else "Tus Compromisos de Pago",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.compromisos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay compromisos registrados.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.compromisos) { compromiso ->
                        CompromisoItemCard(
                            compromiso = compromiso,
                            mostrarAccionPagar = VisibilidadPorRol.puedePagar(uiState.rolSeleccionado),
                            onVerDetalleClick = {
                                viewModel.onSeleccionarCompromisoParaPago(compromiso)
                                onNavegarADetalle()
                            },
                            onPagarClick = {
                                viewModel.onSeleccionarCompromisoParaPago(compromiso)
                                onNavegarAPago()
                            }
                        )
                    }
                }
            }
        }
    }
}
}
