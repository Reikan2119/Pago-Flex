package com.example.pagoflex.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Flag
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import com.example.pagoflex.ui.style.EstilosBoton
import androidx.compose.ui.text.font.FontWeight
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pagoflex.ui.style.EstilosCampo
import com.example.pagoflex.ui.style.EstilosTarjeta
import com.example.pagoflex.viewmodel.PagoFlexViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReporteProblemaScreen(
    viewModel: PagoFlexViewModel,
    onVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var expandedMotivo by remember { mutableStateOf(false) }
    val motivosDisponibles = listOf("Pago no reflejado", "Monto incorrecto", "Error en convenio", "Otro")

    var fotoUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var fotoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val lanzadorFoto = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        fotoUri = uri
    }
    val lanzadorCamara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        fotoBitmap = bitmap
    }
    val context = LocalContext.current

    val permisoGaleriaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) lanzadorFoto.launch("image/*")
    }
    val permisoCamaraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) lanzadorCamara.launch(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportar Problema - PagoFlex") },
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
                text = "Reportar una Incidencia",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            ExposedDropdownMenuBox(
                expanded = expandedMotivo,
                onExpandedChange = { expandedMotivo = !expandedMotivo },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = uiState.motivoReporteSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Motivo del Reporte") },
                    leadingIcon = { Icon(Icons.Filled.Flag, contentDescription = "Motivo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMotivo) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = EstilosCampo.forma
                )

                ExposedDropdownMenu(
                    expanded = expandedMotivo,
                    onDismissRequest = { expandedMotivo = false }
                ) {
                    motivosDisponibles.forEach { motivo ->
                        DropdownMenuItem(
                            text = { Text(motivo) },
                            onClick = {
                                viewModel.onMotivoReporteCambia(motivo)
                                expandedMotivo = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            
            OutlinedTextField(
                value = uiState.comentarioReporte,
                onValueChange = { viewModel.onComentarioReporteCambia(it) },
                label = { Text("Comentario o detalle (Obligatorio si es 'Otro')") },
                leadingIcon = { Icon(Icons.Filled.ChatBubble, contentDescription = "Comentario") },
                isError = uiState.errorComentarioReporte != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = EstilosCampo.forma
            )
            if (uiState.errorComentarioReporte != null) {
                Text(
                    text = uiState.errorComentarioReporte!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                            lanzadorFoto.launch("image/*")
                        } else {
                            permisoGaleriaLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = EstilosCampo.forma
                ) {
                    Text(if (fotoUri == null) "Adjuntar foto" else "Foto seleccionada")
                }
                OutlinedButton(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            lanzadorCamara.launch(null)
                        } else {
                            permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = EstilosCampo.forma
                ) {
                    Text(if (fotoBitmap == null) "Tomar foto" else "Foto tomada")
                }
            }
            if (fotoBitmap != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    bitmap = fotoBitmap!!.asImageBitmap(),
                    contentDescription = "Foto tomada",
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onEnviarReporte() },
                shape = EstilosCampo.forma,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Enviar Reporte", style = MaterialTheme.typography.titleMedium)
            }

            if (uiState.reporteExitoso) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "¡Reporte enviado exitosamente!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}





