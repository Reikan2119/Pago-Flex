package com.example.pagoflex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pagoflex.ui.screens.DashboardScreen
import com.example.pagoflex.ui.screens.LoginSimuladoScreen
import com.example.pagoflex.ui.screens.RecomendarEntidadScreen
import com.example.pagoflex.ui.screens.RegistrarCompromisoScreen
import com.example.pagoflex.ui.screens.ReporteProblemaScreen
import com.example.pagoflex.ui.theme.PagoFlexTheme 
import com.example.pagoflex.viewmodel.PagoFlexViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PagoFlexTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavegacion()
                }
            }
        }
    }
}

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val viewModel: PagoFlexViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginSimuladoScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                viewModel = viewModel,
                onNavegarAReporte = { navController.navigate("reporte") },
                onNavegarARecomendar = { navController.navigate("recomendar") },
                onNavegarARegistrarCompromiso = { navController.navigate("registrar_compromiso") },
                onCerrarSesion = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }

        composable("reporte") {
            ReporteProblemaScreen(
                viewModel = viewModel,
                onVolverAlDashboard = { navController.popBackStack() }
            )
        }

        composable("recomendar") {
            RecomendarEntidadScreen(
                viewModel = viewModel,
                onVolverAlDashboard = { navController.popBackStack() }
            )
        }

        composable("registrar_compromiso") {
            RegistrarCompromisoScreen(
                viewModel = viewModel,
                onVolverAlDashboard = { navController.popBackStack() }
            )
        }
    }
}