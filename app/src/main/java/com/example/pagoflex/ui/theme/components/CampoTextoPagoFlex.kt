package com.example.pagoflex.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CampoTextoPagoFlex(
    valor: String,
    etiqueta: String,
    onValorCambia: (String) -> Unit,
    mensajeError: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(bottom = 12.dp)) {
        OutlinedTextField(
            value = valor,
            onValueChange = onValorCambia,
            label = { Text(etiqueta) },
            isError = mensajeError != null,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        if (mensajeError != null) {
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}

