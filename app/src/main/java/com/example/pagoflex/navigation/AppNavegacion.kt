package com.example.pagoflex.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pagoflex.ui.screens.BienvenidaScreen
import com.example.pagoflex.ui.screens.DashboardScreen
import com.example.pagoflex.ui.screens.LoginSimuladoScreen
import com.example.pagoflex.ui.screens.PagoScreen
import com.example.pagoflex.ui.screens.RecomendarEntidadScreen
import com.example.pagoflex.ui.screens.RegistroScreen
import com.example.pagoflex.ui.screens.ReporteProblemaScreen
import com.example.pagoflex.ui.screens.RegistrarCompromisoScreen
import com.example.pagoflex.viewmodel.PagoFlexViewModel


@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    val viewModel: PagoFlexViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Ruta.Bienvenida.ruta
    ) {

        composable(Ruta.Bienvenida.ruta) {
            BienvenidaScreen(
                onNavegarALogin = { navController.navigate(Ruta.Login.ruta) },
                onNavegarARegistro = { navController.navigate(Ruta.Registro.ruta) }
            )
        }

        composable(Ruta.Login.ruta) {
            LoginSimuladoScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Ruta.Dashboard.ruta) {
                        popUpTo(Ruta.Bienvenida.ruta) { inclusive = true }
                    }
                },
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.Registro.ruta) {
            RegistroScreen(
                viewModel = viewModel,
                onRegistroSuccess = {
                    navController.navigate(Ruta.Dashboard.ruta) {
                        popUpTo(Ruta.Bienvenida.ruta) { inclusive = true }
                    }
                },
                onVolver = { navController.navigateUp() }
            )
        }

        composable(Ruta.Dashboard.ruta) {
            DashboardScreen(
                viewModel = viewModel,
                onNavegarAReporte = { navController.navigate(Ruta.Reporte.ruta) },
                onNavegarARecomendar = { navController.navigate(Ruta.Recomendar.ruta) },
                onNavegarARegistrarCompromiso = { navController.navigate(Ruta.RegistrarCompromiso.ruta) },
                onNavegarAPago = { navController.navigate(Ruta.Pago.ruta) },
                onCerrarSesion = {
                    viewModel.cerrarSesion()
                    navController.navigate(Ruta.Bienvenida.ruta) {
                        popUpTo(Ruta.Dashboard.ruta) { inclusive = true }
                    }
                }
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
                onVolver = { navController.navigateUp() }
            )
        }
    }
}