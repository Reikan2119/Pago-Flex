package com.example.pagoflex.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.activity.compose.BackHandler
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.pagoflex.ui.style.EstilosBoton
import com.example.pagoflex.ui.style.EstilosCampo
import com.example.pagoflex.ui.style.EstilosTarjeta
import com.example.pagoflex.viewmodel.PagoFlexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit,
    onVerComprobante: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val compromiso = uiState.compromisoSeleccionadoParaPago

    BackHandler {
        viewModel.limpiarPagoTarjeta()
        onVolver()
    }

    var mostrarDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pagar con Tarjeta - PagoFlex") }
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
                text = "Pago de Compromiso",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (compromiso == null) {
                Text(
                    text = "No hay un compromiso seleccionado para pagar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        viewModel.limpiarPagoTarjeta()
                        onVolver()
                    },
                    shape = EstilosCampo.forma,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver")
                }
                return@Column
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosTarjeta.forma,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = compromiso.concepto,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Folio: ${compromiso.folio}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Monto a pagar: $${compromiso.monto}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.numeroTarjeta,
                onValueChange = { viewModel.onNumeroTarjetaCambia(it) },
                label = { Text("Número de Tarjeta") },
                placeholder = { Text("1234567890123456") },
                isError = uiState.errorNumeroTarjeta != null,
                leadingIcon = { Icon(Icons.Filled.CreditCard, contentDescription = "Tarjeta") },
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                singleLine = true
            )
            if (uiState.errorNumeroTarjeta != null) {
                Text(
                    text = uiState.errorNumeroTarjeta!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.nombreTitularTarjeta,
                onValueChange = { viewModel.onNombreTitularTarjetaCambia(it) },
                label = { Text("Nombre del Titular") },
                isError = uiState.errorNombreTitularTarjeta != null,
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Titular") },
                modifier = Modifier.fillMaxWidth(),
                shape = EstilosCampo.forma,
                singleLine = true
            )
            if (uiState.errorNombreTitularTarjeta != null) {
                Text(
                    text = uiState.errorNombreTitularTarjeta!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { mostrarDatePicker = true },
                    modifier = Modifier.weight(1f),
                    shape = EstilosCampo.forma
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Fecha")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (uiState.fechaExpiracionTarjeta.isBlank()) "Vencimiento (MM/AA)" else uiState.fechaExpiracionTarjeta,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                OutlinedTextField(
                    value = uiState.cvvTarjeta,
                    onValueChange = { viewModel.onCvvTarjetaCambia(it) },
                    label = { Text("CVV") },
                    placeholder = { Text("123") },
                    isError = uiState.errorCvvTarjeta != null,
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "CVV") },
                    visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.weight(1f),
                    shape = EstilosCampo.forma,
                    singleLine = true
                )
            }

            if (uiState.errorFechaExpiracionTarjeta != null) {
                Text(
                    text = uiState.errorFechaExpiracionTarjeta!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp)
                )
            }
            if (uiState.errorCvvTarjeta != null) {
                Text(
                    text = uiState.errorCvvTarjeta!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp)
                )
            }

            if (mostrarDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { mostrarDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val formato = SimpleDateFormat("MM/yy", Locale.getDefault())
                                viewModel.onFechaExpiracionTarjetaCambia(formato.format(Date(millis)))
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
                onClick = { viewModel.onRealizarPagoTarjeta() },
                shape = EstilosCampo.forma,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(text = "Pagar", style = MaterialTheme.typography.titleMedium)
            }

            if (uiState.pagoTarjetaExitoso) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Pago realizado con éxito.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.limpiarPagoTarjeta()
                        onVolver()
                    },
                    shape = EstilosCampo.forma,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al Dashboard")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onVerComprobante,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver comprobante")
                }
            }
        }
    }
}



