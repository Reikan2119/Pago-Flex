package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.style.EstilosBoton
import com.example.pagoflex.ui.style.EstilosCampo
import com.example.pagoflex.ui.style.EstilosTarjeta
import com.example.pagoflex.viewmodel.PagoFlexViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarCompromisoScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.cargarClientes() }

    var mostrarDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Compromiso - PagoFlex") },
            navigationIcon = { IconButton(onClick = onVolver) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") } },
                
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nuevo Compromiso de Pago",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.clientesDisponibles.isEmpty()) {
                OutlinedTextField(
                    value = uiState.rutClienteCompromiso,
                    onValueChange = { viewModel.onRutClienteCompromisoCambia(it) },
                    label = { Text("RUT del Cliente") },
                    isError = uiState.errorRutClienteCompromiso != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = EstilosCampo.forma,
                    colors = EstilosCampo.colores(),
                    singleLine = true
                )
                if (uiState.errorRutClienteCompromiso != null) {
                    Text(
                        text = uiState.errorRutClienteCompromiso!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp)
                    )
                }
            } else {
                OutlinedButton(
                    onClick = { viewModel.onMostrarListaClientes(true) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = EstilosCampo.forma
                ) {
                    Text(if (uiState.rutClienteCompromiso.isBlank()) "Buscar cliente (RUT)" else "RUT: ${uiState.rutClienteCompromiso}")
                }
                if (uiState.errorRutClienteCompromiso != null) {
                    Text(
                        text = uiState.errorRutClienteCompromiso!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp)
                    )
                }

                if (uiState.mostrarListaClientes) {
                    AlertDialog(
                        onDismissRequest = { viewModel.onMostrarListaClientes(false) },
                        title = { Text("Seleccionar cliente") },
                        text = {
                            Column {
                                uiState.clientesDisponibles.forEach { c ->
                                    TextButton(onClick = { viewModel.onSeleccionarCliente(c) }, modifier = Modifier.fillMaxWidth()) {
                                        Text("${c.nombre} - ${c.rut}")
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { viewModel.onMostrarListaClientes(false) }) { Text("Cerrar") }
                        }
                    )
                }

                if (uiState.confirmarCliente && uiState.clienteSeleccionado != null) {
                    AlertDialog(
                        onDismissRequest = { viewModel.onCancelarCliente() },
                        title = { Text("Confirmar cliente") },
                        text = { Text("¿Es ${uiState.clienteSeleccionado!!.nombre} el usuario final al que registrarás el compromiso?") },
                        confirmButton = {
                            TextButton(onClick = { viewModel.onConfirmarCliente() }) { Text("Sí") }
                        },
                        dismissButton = {
                            TextButton(onClick = { viewModel.onCancelarCliente() }) { Text("No") }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.conceptoCompromiso,
                onValueChange = { viewModel.onConceptoCompromisoCambia(it) },
                label = { Text("Concepto (ej. Cuota GC)") },
                leadingIcon = { Icon(Icons.Filled.Description, contentDescription = "Concepto") },
                isError = uiState.errorConceptoCompromiso != null,
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                singleLine = true
            )
            if (uiState.errorConceptoCompromiso != null) {
                Text(
                    text = uiState.errorConceptoCompromiso!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.montoCompromiso,
                onValueChange = { viewModel.onMontoCompromisoCambia(it) },
                label = { Text("Monto ($ mayor a 0)") },
                leadingIcon = { Icon(Icons.Filled.AttachMoney, contentDescription = "Monto") },
                isError = uiState.errorMontoCompromiso != null,
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                singleLine = true
            )
            if (uiState.errorMontoCompromiso != null) {
                Text(
                    text = uiState.errorMontoCompromiso!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { mostrarDatePicker = true },
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma
            ) {
                Icon(Icons.Filled.DateRange, contentDescription = "Fecha")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (uiState.fechaVencimientoCompromiso.isBlank()) "Fecha de Vencimiento" else uiState.fechaVencimientoCompromiso,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            if (uiState.errorFechaVencimientoCompromiso != null) {
                Text(
                    text = uiState.errorFechaVencimientoCompromiso!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            if (mostrarDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { mostrarDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val formato = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                viewModel.onFechaVencimientoCompromisoCambia(formato.format(Date(millis)))
                            }
                            mostrarDatePicker = false
                        }) { Text("Aceptar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarDatePicker = false }) { Text("Cancelar") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onRegistrarCompromiso() },
                shape = EstilosCampo.forma,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Registrar Compromiso", style = MaterialTheme.typography.titleMedium)
            }

            if (uiState.registroCompromisoExitoso) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "¡Compromiso registrado con éxito!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}






