package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.ui.components.AvatarIniciales
import com.example.pagoflex.ui.components.InsigniaRol
import com.example.pagoflex.ui.components.TarjetaSeccion
import com.example.pagoflex.ui.theme.colorDeRol
import com.example.pagoflex.ui.style.EstilosBoton
import com.example.pagoflex.ui.style.EstilosCampo
import com.example.pagoflex.ui.style.EstilosTarjeta
import com.example.pagoflex.viewmodel.PagoFlexViewModel
import com.example.pagoflex.domain.CampoVisible
import com.example.pagoflex.domain.SeccionDatos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgenteHomeScreen(
    viewModel: PagoFlexViewModel,
    onNavegarAReportes: () -> Unit,
    onNavegarARecomendaciones: () -> Unit,
    onNavegarARegistrar: () -> Unit,
    onNavegarACartera: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú Agente", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                NavigationDrawerItem(label = { Text("Registrar compromiso") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }; onNavegarARegistrar()
                })
                NavigationDrawerItem(label = { Text("Reportes de problema") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }; onNavegarAReportes()
                })
                NavigationDrawerItem(label = { Text("Cartera de compromisos") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }; onNavegarACartera()
                })
                NavigationDrawerItem(label = { Text("Cerrar sesión", color = Color.Red) }, selected = false, onClick = {
                    scope.launch { drawerState.close() }; onCerrarSesion()
                })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Panel de Agente PagoFlex") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val rol = uiState.rolSeleccionado ?: RolUsuario.AGENTE
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AvatarIniciales(iniciales = uiState.nombreLogin.take(1).ifBlank { "A" }, color = colorDeRol(rol), modifier = Modifier.size(64.dp))
                    Column {
                        Text(uiState.nombreLogin.ifBlank { "Agente" }, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                        InsigniaRol(rol = rol)
                    }
                }

                TarjetaSeccion(
                    seccion = SeccionDatos(
                        titulo = "Resumen operativo",
                        campos = listOf(
                            CampoVisible("Reportes pendientes", uiState.reportesAgente.count { it.estado == "Pendiente" }.toString()),
                            CampoVisible("Reportes en gestión", uiState.reportesAgente.count { it.estado == "En gestión" }.toString()),
                            CampoVisible("Compromisos totales", uiState.compromisos.size.toString())
                        )
                    )
                )

                Button(onClick = onNavegarAReportes, modifier = Modifier.fillMaxWidth()) { Text("Gestionar reportes") }
                Button(onClick = onNavegarARecomendaciones, modifier = Modifier.fillMaxWidth()) { Text("Gestionar recomendaciones") }
                Button(onClick = onNavegarARegistrar, modifier = Modifier.fillMaxWidth()) { Text("Registrar compromiso") }
                OutlinedButton(onClick = onNavegarACartera, modifier = Modifier.fillMaxWidth()) { Text("Ver cartera") }
            }
        }
    }
}





