package com.example.pagoflex.ui.screens
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagoflex.domain.CampoVisible
import com.example.pagoflex.domain.SeccionDatos
import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.ui.components.AvatarIniciales
import com.example.pagoflex.ui.components.InsigniaRol
import com.example.pagoflex.ui.components.TarjetaSeccion
import com.example.pagoflex.ui.theme.colorDeRol
import com.example.pagoflex.ui.style.EstilosBoton
import com.example.pagoflex.ui.style.EstilosCampo
import com.example.pagoflex.ui.style.EstilosTarjeta
import com.example.pagoflex.viewmodel.PagoFlexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePerfilScreen(
    viewModel: PagoFlexViewModel,
    onNavegarAPagar: () -> Unit,
    onNavegarAHistorial: () -> Unit,
    onNavegarAEditarPerfil: () -> Unit,
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
                NavigationDrawerItem(label = { Text("Editar perfil") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }; onNavegarAEditarPerfil()
                })
                NavigationDrawerItem(label = { Text("Pagar") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }; onNavegarAPagar()
                })
                NavigationDrawerItem(label = { Text("Historial de pagos") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }; onNavegarAHistorial()
                })
                NavigationDrawerItem(
                    label = { Text("Cerrar sesión", color = Color.Red) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }; onCerrarSesion()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Mi Perfil") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val rol = uiState.rolSeleccionado ?: RolUsuario.USUARIO_FINAL
                AvatarIniciales(
                    iniciales = uiState.nombreLogin.take(1).ifBlank { "U" },
                    color = colorDeRol(rol),
                    modifier = Modifier.size(96.dp)
                )
                Text(
                    uiState.nombreLogin.ifBlank { "Usuario" },
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
                InsigniaRol(rol = rol)

                TarjetaSeccion(
                    seccion = SeccionDatos(
                        titulo = "Datos personales",
                        campos = listOf(
                            CampoVisible("ID de usuario", uiState.idUsuarioLogueado.toString()),
                            CampoVisible("RUT", uiState.perfilRut.ifBlank { "No registrado" }),
                            CampoVisible("Rol", rol.etiqueta),
                            CampoVisible("Correo", uiState.perfilCorreo.ifBlank { uiState.correoLogin }),
                            CampoVisible("Comuna", uiState.perfilComuna.ifBlank { "No registrada" })
                        )
                    )
                )
            }
        }
    }
}





