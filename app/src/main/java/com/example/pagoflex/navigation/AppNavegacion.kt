package com.example.pagoflex.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pagoflex.ui.screens.BienvenidaScreen
import com.example.pagoflex.ui.screens.DashboardScreen
import com.example.pagoflex.ui.screens.AgenteHomeScreen
import com.example.pagoflex.ui.screens.HistorialPagosScreen
import com.example.pagoflex.ui.screens.HomePerfilScreen
import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.ui.screens.MisReportesScreen
import com.example.pagoflex.ui.screens.LoginSimuladoScreen
import com.example.pagoflex.ui.screens.PagoScreen
import com.example.pagoflex.ui.screens.PerfilScreen
import com.example.pagoflex.ui.screens.ComprobanteScreen
import com.example.pagoflex.ui.screens.DetalleCompromisoScreen
import com.example.pagoflex.ui.screens.RecomendacionesAgenteScreen
import com.example.pagoflex.ui.screens.ReportesAgenteScreen
import com.example.pagoflex.ui.screens.ResumenCobranzaScreen
import com.example.pagoflex.ui.screens.RecomendarEntidadScreen
import com.example.pagoflex.ui.screens.RegistroScreen
import com.example.pagoflex.ui.screens.ReporteProblemaScreen
import com.example.pagoflex.ui.screens.RegistrarCompromisoScreen
import com.example.pagoflex.viewmodel.AuthViewModel
import com.example.pagoflex.viewmodel.PagoFlexViewModel


@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    val viewModel: PagoFlexViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Ruta.Bienvenida.ruta,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {

        composable(Ruta.Bienvenida.ruta) {
            BienvenidaScreen(
                onNavegarALogin = { navController.navigate(Ruta.Login.ruta) },
                onNavegarARegistro = { navController.navigate(Ruta.Registro.ruta) }
            )
        }

        composable(Ruta.Login.ruta) {
            LoginSimuladoScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    val u = authViewModel.uiState.value
                    viewModel.iniciarSesion(u.idUsuarioLogueado, u.nombreLogin, u.perfilCorreo, u.perfilRut, u.perfilComuna, u.rolSeleccionado ?: RolUsuario.USUARIO_FINAL)
                    val destino = if (u.rolSeleccionado == RolUsuario.AGENTE) Ruta.AgenteHome.ruta else Ruta.HomePerfil.ruta
                    navController.navigate(destino) {
                        popUpTo(Ruta.Bienvenida.ruta) { inclusive = true }
                    }
                },
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.Registro.ruta) {
            RegistroScreen(
                viewModel = authViewModel,
                onRegistroSuccess = {
                    navController.navigate(Ruta.Bienvenida.ruta) {
                        popUpTo(Ruta.Registro.ruta) { inclusive = true }
                    }
                },
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.AgenteHome.ruta) {
            AgenteHomeScreen(
                viewModel = viewModel,
                onNavegarAReportes = { navController.navigate(Ruta.ReportesAgente.ruta) { launchSingleTop = true } },
                onNavegarARecomendaciones = { navController.navigate(Ruta.RecomendacionesAgente.ruta) { launchSingleTop = true } },
                onNavegarARegistrar = { navController.navigate(Ruta.RegistrarCompromiso.ruta) { launchSingleTop = true } },
                onNavegarACartera = { navController.navigate(Ruta.Pagar.ruta) { launchSingleTop = true } },
                onCerrarSesion = {
                    authViewModel.cerrarSesion()
                    viewModel.cerrarSesion()
                    navController.navigate(Ruta.Bienvenida.ruta) { popUpTo(Ruta.AgenteHome.ruta) { inclusive = true } }
                }
            )
        }

        composable(Ruta.HomePerfil.ruta) {
            HomePerfilScreen(
                viewModel = viewModel,
                onNavegarAPagar = { navController.navigate(Ruta.Pagar.ruta) { launchSingleTop = true; popUpTo(Ruta.HomePerfil.ruta) { inclusive = false } } },
                onNavegarAHistorial = { navController.navigate(Ruta.HistorialPagos.ruta) { launchSingleTop = true; popUpTo(Ruta.HomePerfil.ruta) { inclusive = false } } },
                onNavegarAMisReportes = { navController.navigate(Ruta.MisReportes.ruta) { launchSingleTop = true; popUpTo(Ruta.HomePerfil.ruta) { inclusive = false } } },
                onNavegarAEditarPerfil = { navController.navigate(Ruta.Perfil.ruta) { launchSingleTop = true; popUpTo(Ruta.HomePerfil.ruta) { inclusive = false } } },
                onCerrarSesion = {
                    authViewModel.cerrarSesion()
                    viewModel.cerrarSesion()
                    navController.navigate(Ruta.Bienvenida.ruta) { popUpTo(Ruta.HomePerfil.ruta) { inclusive = true } }
                }
            )
        }

        composable(Ruta.Pagar.ruta) {
            DashboardScreen(
                viewModel = viewModel,
                onNavegarAReporte = { navController.navigate(Ruta.Reporte.ruta) { launchSingleTop = true } },
                onNavegarARecomendar = { navController.navigate(Ruta.Recomendar.ruta) { launchSingleTop = true } },
                onNavegarARegistrarCompromiso = { navController.navigate(Ruta.RegistrarCompromiso.ruta) { launchSingleTop = true } },
                onNavegarAPago = { navController.navigate(Ruta.Pago.ruta) { launchSingleTop = true } },
                onNavegarAPerfil = { navController.navigate(Ruta.HomePerfil.ruta) { launchSingleTop = true; popUpTo(Ruta.Pagar.ruta) { inclusive = true } } },
                onNavegarADetalle = { navController.navigate(Ruta.DetalleCompromiso.ruta) { launchSingleTop = true } },
                onNavegarAReportesAgente = { navController.navigate(Ruta.ReportesAgente.ruta) { launchSingleTop = true } },
                onNavegarAResumenCobranza = { navController.navigate(Ruta.ResumenCobranza.ruta) { launchSingleTop = true } },
                onCerrarSesion = {
                    authViewModel.cerrarSesion()
                    viewModel.cerrarSesion()
                    navController.navigate(Ruta.Bienvenida.ruta) { popUpTo(Ruta.Pagar.ruta) { inclusive = true } }
                }
            )
        }

        composable(Ruta.HistorialPagos.ruta) {
            HistorialPagosScreen(
                viewModel = viewModel,
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.MisReportes.ruta) {
            MisReportesScreen(
                viewModel = viewModel,
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.Reporte.ruta) {
            ReporteProblemaScreen(
                viewModel = viewModel,
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.Recomendar.ruta) {
            RecomendarEntidadScreen(
                viewModel = viewModel,
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.RegistrarCompromiso.ruta) {
            RegistrarCompromisoScreen(
                viewModel = viewModel,
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.Pago.ruta) {
            PagoScreen(
                viewModel = viewModel,
                onVolver = { navController.navigateUp() },
                onVerComprobante = { navController.navigate(Ruta.Comprobante.ruta) { launchSingleTop = true } }
            )
        }

        composable(Ruta.Perfil.ruta) {
            PerfilScreen(
                viewModel = viewModel,
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.DetalleCompromiso.ruta) {
            DetalleCompromisoScreen(
                viewModel = viewModel,
                onVolver = { navController.navigateUp() },
                onPagar = { navController.navigate(Ruta.Pago.ruta) { launchSingleTop = true } }
            )
        }

        composable(Ruta.Comprobante.ruta) {
            ComprobanteScreen(
                viewModel = viewModel,
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.ReportesAgente.ruta) {
            ReportesAgenteScreen(
                viewModel = viewModel,
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.RecomendacionesAgente.ruta) {
            RecomendacionesAgenteScreen(
                viewModel = viewModel,
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.ResumenCobranza.ruta) {
            ResumenCobranzaScreen(
                viewModel = viewModel,
                onVolver = { navController.navigateUp() }
            )
        }
    }
}

