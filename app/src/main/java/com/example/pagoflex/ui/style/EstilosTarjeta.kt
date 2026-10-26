package com.example.pagoflex.ui.style

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.pagoflex.ui.theme.Dimens

object EstilosTarjeta {
    val forma = RoundedCornerShape(Dimens.radioTarjeta)

    @Composable
    fun elevacion(): CardElevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevacionTarjeta)
}

