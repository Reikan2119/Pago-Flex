package com.example.pagoflex.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
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
import com.example.pagoflex.ui.components.CampoTextoPagoFlex
import com.example.pagoflex.viewmodel.PagoFlexViewModel

@Composable
fun LoginSimuladoScreen(
    viewModel: PagoFlexViewModel,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "PagoFlex",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Bienvenido a tu plataforma de pagos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                CampoTextoPagoFlex(
                    valor = uiState.nombreLogin,
                    etiqueta = "Nombre de Usuario",
                    onValorCambia = { viewModel.onNombreLoginCambia(it) },
                    mensajeError = uiState.errorNombreLogin
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Seleccione su perfil de acceso:",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )

                Column(
                    modifier = Modifier
                        .selectableGroup()
                        .fillMaxWidth()
                ) {
                    RolUsuario.entries.forEach { rol ->
                        val seleccionado = uiState.rolSeleccionado == rol
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (seleccionado) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { viewModel.onRolCambia(rol) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = seleccionado,
                                    onClick = { viewModel.onRolCambia(rol) }
                                )
                                Text(
                                    text = rol.etiqueta,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }

                if (uiState.errorRolLogin != null) {
                    Text(
                        text = uiState.errorRolLogin!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 12.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (viewModel.onIntentarLogin()) {
                            onLoginSuccess()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Ingresar a la Plataforma", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}