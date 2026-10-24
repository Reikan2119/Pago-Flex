package com.example.pagoflex.ui.style

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.Primario

object EstilosCampo {
    val forma = RoundedCornerShape(Dimens.radioCampo)

    @Composable
    fun colores(): TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Primario,
        focusedLabelColor = Primario,
        cursorColor = Primario
    )
}
